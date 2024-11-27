package ru.tinkoff.kotea.android.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

internal inline fun <reified T : ViewModel> ViewModelStore.get(key: String, crossinline factory: () -> T): T {
    val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = factory() as VM
    }
    return ViewModelProvider(this, viewModelFactory)[key, T::class.java]
}
