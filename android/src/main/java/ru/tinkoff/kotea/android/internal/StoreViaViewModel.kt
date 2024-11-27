package ru.tinkoff.kotea.android.internal

import androidx.lifecycle.ViewModelStoreOwner
import ru.tinkoff.kotea.core.Store
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class StoreViaViewModel<T : Store<*, *, *>>(
    private val viewModelKey: String? = null,
    private val viewModelStoreOwnerProvider: (() -> ViewModelStoreOwner)? = null,
    private val factory: () -> T,
    private val coroutineContext: CoroutineContext,
) : ReadOnlyProperty<ViewModelStoreOwner, T> {
    private var value: T? = null

    override fun getValue(thisRef: ViewModelStoreOwner, property: KProperty<*>): T {
        return value ?: run {
            val key = viewModelKey ?: keyFromProperty(thisRef, property)
            val viewModelStore = (viewModelStoreOwnerProvider?.invoke() ?: thisRef).viewModelStore
            val vm = viewModelStore.get(key) { StoreViewModel(factory(), coroutineContext) }
            vm.store.also { value = it }
        }
    }

    private fun keyFromProperty(thisRef: ViewModelStoreOwner, property: KProperty<*>): String {
        return thisRef::class.java.canonicalName!! + "#" + property.name
    }
}
