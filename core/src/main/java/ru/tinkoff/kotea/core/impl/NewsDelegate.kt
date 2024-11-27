package ru.tinkoff.kotea.core.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal interface NewsDelegate<News : Any> {

    val news: Flow<News>

    fun initNews(scope: CoroutineScope)

    suspend fun emit(news: News)
}

internal class StoreScopeNews<News : Any> : NewsDelegate<News> {

    private val initMutex = Mutex()
    private val newsChannel = Channel<News>(capacity = Channel.UNLIMITED)
    private var continuations = mutableListOf<Continuation<Unit>>()
    private lateinit var flow: SharedFlow<News>

    override val news
        get() = flow {
            awaitInit()
            flow.collect(this)
        }

    private suspend fun awaitInit() {
        initMutex.lock()
        if (::flow.isInitialized) {
            initMutex.unlock()
            return
        }
        suspendCoroutine {
            continuations.add(it)
            initMutex.unlock()
        }
    }

    override fun initNews(scope: CoroutineScope) {
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            initMutex.withLock {
                flow = newsChannel
                    .receiveAsFlow()
                    .shareIn(scope, started = SharingStarted.WhileSubscribed())
                continuations.forEach { it.resume(Unit) }
            }
        }
    }

    override suspend fun emit(news: News) {
        newsChannel.send(news)
    }
}
