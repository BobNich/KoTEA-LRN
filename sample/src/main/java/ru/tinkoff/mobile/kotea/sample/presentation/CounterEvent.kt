package ru.tinkoff.mobile.kotea.sample.presentation

sealed interface CounterEvent {

    sealed interface CounterUiEvent : CounterEvent {

        object StartClicked : CounterUiEvent

        object StopClicked : CounterUiEvent

        object ResetClicked : CounterUiEvent
    }

    sealed interface CounterCommandsResultEvent : CounterEvent {

        object CounterTick : CounterCommandsResultEvent
    }
}