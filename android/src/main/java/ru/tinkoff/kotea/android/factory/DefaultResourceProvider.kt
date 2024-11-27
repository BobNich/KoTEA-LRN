package ru.tinkoff.kotea.android.factory

import android.content.Context
import ru.tinkoff.kotea.android.ui.ContextResourcesProvider
import ru.tinkoff.kotea.android.ui.ResourcesProvider

/**
 * Default implementation of [ResourcesProvider]
 */
fun defaultResourceProvider(context: Context): ResourcesProvider {
    return ContextResourcesProvider(context)
}