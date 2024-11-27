package ru.tinkoff.kotea.core.impl

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.Update
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Kotea store implementation
 * Important: the list of initial commands will be converted to a cold flow,
 * so the commands will be re-emitted on each collection.
 *
 * @param initialCommands commands for initial emission.
 **/
internal class StoreImpl<State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any>(
    initialState: State,
    private val initialCommands: List<Command> = emptyList(),
    private val commandsFlowHandlers: List<CommandsFlowHandler<Command, Event>>,
    private val update: Update<State, Event, Command, News>,
) : Store<State, UiEvent, News> {

    override val state = MutableStateFlow(initialState)

    private val newsDelegate: NewsDelegate<News> = StoreScopeNews()

    private val eventChannel = Channel<Event>(capacity = UNLIMITED)

    private val isLaunched = AtomicBoolean(false)

    override fun launchIn(coroutineScope: CoroutineScope) {
        if (isLaunched.getAndSet(true)) error("Store has already been launched")

        val handlerCommandSourceChannels: List<Channel<Command>> = commandsFlowHandlers.map { commandsFlowHandler ->
            // Create a personal command source channel for each CommandHandler to prevent loss of commands before
            // a subscription
            Channel<Command>(capacity = UNLIMITED).also { handlerCommandSourceChannel ->
                subscribeCommandHandler(
                    coroutineScope,
                    handlerCommandSourceChannel,
                    commandsFlowHandler,
                    resultEventsCollector = eventChannel::send
                )
            }
        }

        val commandsSendChannel = Channel<Command>(capacity = BUFFERED)

        commandsSendChannel.collectToHandlersChannels(handlerCommandSourceChannels, coroutineScope)
        coroutineScope.launchStoreLoop(commandsSendChannel)
        newsDelegate.initNews(coroutineScope)
    }

    override val news: Flow<News> = newsDelegate.news

    override fun dispatch(event: UiEvent) {
        eventChannel.trySend(event)
    }

    private fun subscribeCommandHandler(
        coroutineScope: CoroutineScope,
        handlerCommandSourceChannel: Channel<Command>,
        commandsFlowHandler: CommandsFlowHandler<Command, Event>,
        resultEventsCollector: FlowCollector<Event>
    ) {
        // Use shareIn to support multiple subscribers within CommandsFlowHandler and SharingStarted.Lazily to preserve
        // buffered commands
        val lazilySharedCommands: Flow<Command> = handlerCommandSourceChannel.consumeAsFlow()
            .shareIn(coroutineScope, SharingStarted.Lazily)
            .onSubscription { emitAll(initialCommands.asFlow()) }
        commandsFlowHandler.handle(lazilySharedCommands)
            .collectResultEventsWithExceptionHandling(
                coroutineScope,
                resultEventsCollector,
                flowHandlerClass = commandsFlowHandler::class.java
            )
    }

    private fun Flow<Event>.collectResultEventsWithExceptionHandling(
        coroutineScope: CoroutineScope,
        resultEventsCollector: FlowCollector<Event>,
        flowHandlerClass: Class<out CommandsFlowHandler<Command, Event>>
    ) {
        coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
            catch { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                } else {
                    throw CommandsFlowHandlerException(flowHandlerClass, throwable)
                }
            }.collect(resultEventsCollector)
        }
    }

    private fun Channel<Command>.collectToHandlersChannels(
        handlerCommandSourceChannels: List<Channel<Command>>,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            consumeEach { command ->
                handlerCommandSourceChannels.forEach { channel ->
                    // Assume there is no suspension because of UNLIMITED buffer capacity
                    channel.send(command)
                }
            }
        }
    }

    private fun CoroutineScope.launchStoreLoop(commandsSendChannel: Channel<Command>) {
        launch {
            while (isActive) {
                val event = eventChannel.receive()
                val next = update.update(state.value, event)
                if (next.state != null) {
                    state.value = next.state
                }
                for (command in next.commands) {
                    commandsSendChannel.send(command)
                }
                for (news in next.news) {
                    newsDelegate.emit(news)
                }
            }
        }
    }
}
