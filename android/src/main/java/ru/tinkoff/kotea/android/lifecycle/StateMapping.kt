package ru.tinkoff.kotea.android.lifecycle

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import ru.tinkoff.kotea.android.ui.ContextResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import ru.tinkoff.kotea.android.ui.map

@ExperimentalCoroutinesApi
fun <State : Any, UiState : Any> StateFlow<State>.mapState(
    context: Context,
    scope: CoroutineScope,
    uiStateMapper: UiStateMapper<State, UiState>,
): StateFlow<UiState> {
    val resourcesProvidersDelegate = ContextResourcesProvider(context)
    return this@mapState.mapLatest { state -> uiStateMapper.map(resourcesProvidersDelegate, state) }
        .stateIn(scope, SharingStarted.Eagerly, uiStateMapper.map(resourcesProvidersDelegate, value))
}
