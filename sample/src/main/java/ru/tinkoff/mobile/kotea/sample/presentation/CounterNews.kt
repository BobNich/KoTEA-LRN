package ru.tinkoff.mobile.kotea.sample.presentation

sealed interface CounterNews {

    data class ShowToast(
        val text: String,
    ) : CounterNews
}