package ru.tinkoff.kotea.core.dsl

import ru.tinkoff.kotea.core.Next
import ru.tinkoff.kotea.core.Update

/**
 * DSL version of [Update].
 *
 * Usage example:
 * ```kotlin
 * class SampleUpdate : DslUpdate<State, Event, Command, News>() {
 *     override fun NextBuilder.update(event: Event) = when (event) {
 *         UiEvent.OnRefresh -> {
 *             state { copy(value = "loading") }
 *             commands(
 *                 Command.LoadItems.takeIf { initialState.value != "loading" }
 *             )
 *         }
 *         Event.LoadItemsSuccess -> {
 *             state { copy(value = "<some items>") }
 *         }
 *         Event.LoadItemsError -> {
 *             state { copy(value = "failed") }
 *             news(News("showErrorDialog"))
 *         }
 *     }
 * }
 * ```
 */
abstract class DslUpdate<State : Any, Event : Any, Command : Any, News : Any> : Update<State, Event, Command, News> {

    override fun update(state: State, event: Event): Next<State, Command, News> {
        return NextBuilder(state).apply { update(event) }.build()
    }

    protected abstract fun NextBuilder.update(event: Event)

    // inner class to eliminate generics in update
    protected inner class NextBuilder(initialState: State) : DslNextBuilder<State, Command, News>(initialState)
}
