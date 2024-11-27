package ru.tinkoff.kotea.compose

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 * Utility method for subscribing to flow with lifecycle state in lifecycle scope.
 *
 *  Sample usage:
 *
 *  In Activity:
 * ```kotlin
 * class MyActivity : AppCompatActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          flowOf(1, 2, 3)
 *              .collectOnLifecycle(this) {
 *                  println(it)
 *              }
 *      }
 * }
 * ```
 *
 * In Fragment:
 *
 * ```kotlin
 * class MyFragment : Fragment() {
 *      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *          super.onViewCreated(view, savedInstanceState)
 *          flowOf(1, 2, 3).collectOnLifecycle(viewLifecycleOwner) {
 *              println(it)
 *          }
 *      }
 * }
 * ```
 */
inline fun <reified T> Flow<T>.collectOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.RESUMED,
    collector: FlowCollector<T>
) {
    lifecycleOwner.lifecycleScope.launch {
        this@collectOnLifecycle
            .flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
            .collect(collector)
    }
}
