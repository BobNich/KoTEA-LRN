package ru.tinkoff.kotea.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.tinkoff.kotea.android.internal.StoreViaViewModel
import ru.tinkoff.kotea.core.Store
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty

/**
 * Lazily gets kotea [Store] from the [ViewModelStore].
 * Launches it in the [viewModelScope][ViewModel.viewModelScope] with the specified [CoroutineContext] on first access.
 *
 * By default uses a containing class and property names as a [ViewModel] key,
 * but you can share [Store] across a screens by specifying the [sharedViewModelKey].
 *
 * @param coroutineContext [CoroutineContext] to launch [Store] in.
 * @param sharedViewModelKey key to share [Store] across a screens.
 * @param factory factory to create [Store]
 */
fun <T : Store<*, *, *>> storeViaViewModel(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    sharedViewModelKey: String? = null,
    factory: () -> T
): ReadOnlyProperty<ViewModelStoreOwner, T> {
    return StoreViaViewModel(
        viewModelKey = sharedViewModelKey,
        coroutineContext = coroutineContext,
        factory = factory,
    )
}

/**
 * Lazily gets kotea [Store] from the [ViewModelStore].
 * Launches it in the [viewModelScope][ViewModel.viewModelScope] with the specified [CoroutineContext] on first access.
 *
 * By default uses a containing class and property names as a [ViewModel] key,
 * but you can share [Store] across a screens by specifying the [sharedViewModelKey].
 *
 * @param coroutineContext [CoroutineContext] to launch [Store] in.
 * @param sharedViewModelKey key to share [Store] across a screens.
 * @param sharedViewModelStoreOwner that [Store] will associate with.
 * @param factory factory to create [Store]
 */
fun <T : Store<*, *, *>> storeViaViewModel(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    sharedViewModelKey: String,
    sharedViewModelStoreOwner: () -> ViewModelStoreOwner,
    factory: () -> T,
): ReadOnlyProperty<ViewModelStoreOwner, T> {
    return StoreViaViewModel(
        viewModelKey = sharedViewModelKey,
        viewModelStoreOwnerProvider = sharedViewModelStoreOwner,
        coroutineContext = coroutineContext,
        factory = factory,
    )
}
