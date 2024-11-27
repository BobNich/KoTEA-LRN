package ru.tinkoff.mobile.kotea.sample.presentation

import ru.tinkoff.kotea.core.dsl.DslUpdate
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent.CounterCommandsResultEvent
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent.CounterUiEvent
import ru.tinkoff.mobile.kotea.sample.presentation.analytics.AnalyticsTracker

class CounterUpdate(
    private val analyticsTracker: AnalyticsTracker,
) : DslUpdate<CounterState, CounterEvent, CounterCommand, CounterNews>() {

    override fun NextBuilder.update(event: CounterEvent) {
        analyticsTracker(state, event)
        when (event) {
            is CounterCommandsResultEvent -> handleResult(event)
            is CounterUiEvent -> handleUiEvent(event)
        }
    }

    private fun NextBuilder.handleResult(event: CounterCommandsResultEvent) {
        when (event) {
            is CounterCommandsResultEvent.CounterTick -> state { copy(count = state.count + 1) }
        }
    }

    private fun NextBuilder.handleUiEvent(event: CounterUiEvent) {
        when (event) {
            CounterUiEvent.ResetClicked -> resetCounter()

            CounterUiEvent.StartClicked -> {
                commands(CounterCommand.Start)
                state { copy(isProgress = true) }
                news(CounterNews.ShowToast("Counter started"))
            }

            CounterUiEvent.StopClicked -> {
                commands(CounterCommand.Stop)
                state { copy(isProgress = false) }
                news(CounterNews.ShowToast("Counter stopped"))
            }
        }
    }
}