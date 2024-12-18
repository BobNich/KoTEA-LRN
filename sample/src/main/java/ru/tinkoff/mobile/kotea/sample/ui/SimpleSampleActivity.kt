package ru.tinkoff.mobile.kotea.sample.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.mobile.kotea.sample.databinding.ActivitySimpleBinding
import ru.tinkoff.mobile.kotea.sample.presentation.CounterEvent.CounterUiEvent
import ru.tinkoff.mobile.kotea.sample.presentation.CounterNews
import ru.tinkoff.mobile.kotea.sample.presentation.CounterStore

class SimpleSampleActivity : AppCompatActivity() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("SimpleSampleActivity", throwable.message, throwable)
    }

    private val store by storeViaViewModel(coroutineContext = Dispatchers.Default + coroutineExceptionHandler) {
        CounterStore()
    }

    private lateinit var binding: ActivitySimpleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        store.collectOnCreate(
            activity = this,
            uiStateMapper = CounterUiStateMapper(),
            stateCollector = ::collect,
            newsCollector = ::handle
        )
        binding.buttonStart.setOnClickListener { store.dispatch(CounterUiEvent.StartClicked) }
        binding.buttonStop.setOnClickListener { store.dispatch(CounterUiEvent.StopClicked) }
        binding.buttonReset.setOnClickListener { store.dispatch(CounterUiEvent.ResetClicked) }
    }

    private fun collect(state: CounterUiState) {
        binding.counterTextView.text = state.countText
        binding.counterProgressView.text = state.progressText
    }

    private fun handle(news: CounterNews) {
        when (news) {
            is CounterNews.ShowToast -> Toast.makeText(this, news.text, Toast.LENGTH_SHORT).show()
        }
    }
}