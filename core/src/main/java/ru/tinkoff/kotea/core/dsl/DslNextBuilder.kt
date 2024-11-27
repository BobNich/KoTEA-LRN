package ru.tinkoff.kotea.core.dsl

import ru.tinkoff.kotea.core.Next

/**
 * Builder class with DSL methods for [Next] creation.
 */
abstract class DslNextBuilder<State : Any, Command : Any, News : Any>(initialState: State) {
    var state: State = initialState
        private set

    private val commands = mutableListOf<Command>()

    private val news = mutableListOf<News>()

    /**
     * Allows to change a [state]. Changes can be applied incrementally.
     *
     * Example:
     * ```kotlin
     *   println(state) // State(one = 0, two = 0)
     *   state { copy(one = 1) }
     *   println(state) // State(one = 1, two = 0)
     *   state { copy(two = 2) }
     *   println(state) // State(one = 1, two = 2)
     * ```
     */
    inline fun state(block: State.() -> State) {
        setState(state.block())
    }

    /** Commands which can be handled in a [CommandsFlowHandler] to trigger some side effects */
    fun commands(vararg commands: Command?) {
        for (item in commands) {
            if (item != null) {
                this.commands.add(item)
            }
        }
    }

    /**
     * One-off commands for UI (e.g. `ShowErrorDialog`)
     * @see [Store.news]
     */
    fun news(vararg news: News?) {
        for (item in news) {
            if (item != null) {
                this.news.add(item)
            }
        }
    }

    @PublishedApi
    internal fun setState(state: State) {
        this.state = state
    }

    internal fun build(): Next<State, Command, News> = Next(state, commands, news)
}