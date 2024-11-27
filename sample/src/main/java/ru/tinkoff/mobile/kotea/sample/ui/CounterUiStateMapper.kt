package ru.tinkoff.mobile.kotea.sample.ui

import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import ru.tinkoff.mobile.kotea.sample.R
import ru.tinkoff.mobile.kotea.sample.presentation.CounterState

class CounterUiStateMapper : UiStateMapper<CounterState, CounterUiState> {

    override fun ResourcesProvider.map(state: CounterState): CounterUiState {
        val progressTextRes: Int = if (state.isProgress) {
            R.string.counter_started
        } else {
            R.string.counter_stopped
        }

        return CounterUiState(
            countText = getString(R.string.counter_title, state.count),
            progressText = getString(progressTextRes)
        )
    }
}