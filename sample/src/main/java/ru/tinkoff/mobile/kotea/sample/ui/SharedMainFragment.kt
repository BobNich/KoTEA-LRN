package ru.tinkoff.mobile.kotea.sample.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.mobile.kotea.sample.R
import ru.tinkoff.mobile.kotea.sample.databinding.FragmentSharedMainBinding
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent.CounterUiEvent
import ru.tinkoff.mobile.kotea.sample.presentation.CounterNews
import ru.tinkoff.mobile.kotea.sample.presentation.CounterStore

class SharedMainFragment : Fragment(R.layout.fragment_shared_main) {

    private lateinit var binding: FragmentSharedMainBinding

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("SharedMainFragment", throwable.message, throwable)
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
            newsCollector = ::handle
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSharedMainBinding.bind(view)
        binding.buttonStart.setOnClickListener { store.dispatch(CounterUiEvent.StartClicked) }
        binding.buttonStop.setOnClickListener { store.dispatch(CounterUiEvent.StopClicked) }
        binding.buttonReset.setOnClickListener { store.dispatch(CounterUiEvent.ResetClicked) }
        binding.buttonDetails.setOnClickListener {
            (activity as? SharedMainFragmentListener)?.onDetailsClicked()
        }
    }

    private fun collect(state: CounterUiState) {
        binding.counterTextView.text = state.countText
        binding.counterProgressView.text = state.progressText
    }

    private fun handle(news: CounterNews) {
        when (news) {
            is CounterNews.ShowToast -> Toast.makeText(requireActivity(), news.text, Toast.LENGTH_SHORT).show()
        }
    }

    interface SharedMainFragmentListener {

        fun onDetailsClicked()
    }
}