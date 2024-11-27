package ru.tinkoff.mobile.kotea.sample.presentation.analytics

import android.util.Log
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent.CounterCommandsResultEvent.CounterTick
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent.CounterUiEvent
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent.CounterUiEvent.ResetClicked
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent.CounterUiEvent.StartClicked
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent.CounterUiEvent.StopClicked
import ru.tinkoff.mobile.kotea.sample.presentation.CounterState

class AnalyticsTracker {

    operator fun invoke(state: CounterState, event: CounterEvent) {
        when (event) {
            CounterTick -> Unit
            is CounterUiEvent -> trackUiEvent(state, event)
        }
    }

    private fun trackUiEvent(state: CounterState, event: CounterUiEvent) {
        when (event) {
            ResetClicked -> Log.d("CounterAnalytics", "Counter reset")
            StartClicked -> Log.d("CounterAnalytics", "Counter started with value: ${state.count}")
            StopClicked -> Log.d("CounterAnalytics", "Counter stopped with value: ${state.count}")
        }
    }
}