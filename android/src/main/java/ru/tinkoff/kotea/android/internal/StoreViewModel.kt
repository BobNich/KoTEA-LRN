package ru.tinkoff.kotea.android.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.plus
import ru.tinkoff.kotea.core.Store
import kotlin.coroutines.CoroutineContext

internal class StoreViewModel<T : Store<*, *, *>>(
    val store: T,
    coroutineContext: CoroutineContext
) : ViewModel() {
    init {
        store.launchIn(viewModelScope + coroutineContext)
    }
}
