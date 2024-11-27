package ru.tinkoff.mobile.kotea.sample.presentation

sealed interface CounterCommand {

    object Start : CounterCommand

    object Stop : CounterCommand
}