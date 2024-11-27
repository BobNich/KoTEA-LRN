package ru.tinkoff.mobile.kotea.sample.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.mobile.kotea.sample.R
import ru.tinkoff.mobile.kotea.sample.databinding.FragmentSharedDetailsBinding
import ru.tinkoff.mobile.kotea.sample.presentation.CounterStore

class SharedDetailsFragment : Fragment(R.layout.fragment_shared_details) {

    private lateinit var binding: FragmentSharedDetailsBinding

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("SharedDetailsFragment", throwable.message, throwable)
    }

    private val store by storeViaViewModel(
        sharedViewModelStoreOwner = ::requireActivity,
        sharedViewModelKey = "shared_store_key",
        coroutineContext = Dispatchers.Default + coroutineExceptionHandler,
    ) {
        CounterStore()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store.collectOnCreate(
            fragment = this,
            uiStateMapper = CounterUiStateMapper(),
            stateCollector = ::collect,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSharedDetailsBinding.bind(view)
    }

    private fun collect(state: CounterUiState) {
        binding.counterTextView.text = state.countText
    }
}