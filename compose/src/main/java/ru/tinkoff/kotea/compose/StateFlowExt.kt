package ru.tinkoff.kotea.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import ru.tinkoff.kotea.android.lifecycle.mapState
import ru.tinkoff.kotea.android.ui.UiStateMapper

/**
 * Map Store `state` to `ui state` with [uiStateMapper]
 *
 * @param [uiStateMapper] - маппер state в ui state
 * @return [StateFlow] - StateFlow с UiState
 */
@Composable
@ExperimentalCoroutinesApi
fun <State : Any, UiState : Any> StateFlow<State>.mapState(
    uiStateMapper: UiStateMapper<State, UiState>,
): StateFlow<UiState> {
    return this@mapState.mapState(
        context = LocalContext.current,
        scope = rememberCoroutineScope(),
        uiStateMapper = uiStateMapper
    )
}

/**
 * Collect `state` from [StateFlow] and map it to [UiState] with [uiStateMapper]
 *
 * @param [uiStateMapper] - state mapper to ui state
 * @return [StateFlow] - StateFlow with UiState
 */
@Composable
@ExperimentalCoroutinesApi
fun <StoreState : Any, UiState : Any> StateFlow<StoreState>.collectState(
    uiStateMapper: UiStateMapper<StoreState, UiState>,
): State<UiState> {
    return this@collectState.mapState(uiStateMapper)
        .collectAsStateWithLifecycle()
}

/**
 * Collect `state` from [StateFlow] and map it to [StoreState]
 *
 * @return [StateFlow] - StateFlow с UiState
 */
@Composable
fun <StoreState : Any> StateFlow<StoreState>.collectState(): State<StoreState> {
    return this@collectState.collectAsStateWithLifecycle()
}
