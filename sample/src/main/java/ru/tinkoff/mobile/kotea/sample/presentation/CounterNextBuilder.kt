package ru.tinkoff.mobile.kotea.sample.presentation

import ru.tinkoff.kotea.core.dsl.DslNextBuilder

typealias CounterNextBuilder = DslNextBuilder<CounterState, CounterCommand, CounterNews>

// Can be used from any DslUpdate with same generic parameters
fun CounterNextBuilder.resetCounter() {
    val currentCount = state.count // Access state
    state { copy(count = 0) } // Update state
    news(CounterNews.ShowToast("Counter reset, count was $currentCount")) // Send news
}