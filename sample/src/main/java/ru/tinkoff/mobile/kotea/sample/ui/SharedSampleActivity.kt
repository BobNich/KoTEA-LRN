package ru.tinkoff.mobile.kotea.sample.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.mobile.kotea.sample.R
import ru.tinkoff.mobile.kotea.sample.presentation.CounterStore
import ru.tinkoff.mobile.kotea.sample.ui.SharedMainFragment.SharedMainFragmentListener

class SharedSampleActivity : AppCompatActivity(R.layout.activity_shared), SharedMainFragmentListener {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("SharedSampleActivity", throwable.message, throwable)
    }

    @Suppress("unused")
    private val store by storeViaViewModel(
        sharedViewModelKey = "shared_store_key",
        coroutineContext = Dispatchers.Default + coroutineExceptionHandler,
    ) {
        CounterStore()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val transition = supportFragmentManager.beginTransaction()
            transition.replace(R.id.shared_fragment_container, SharedMainFragment())
            transition.commit()
        }
    }

    override fun onDetailsClicked() {
        val transition = supportFragmentManager.beginTransaction()
        transition.replace(R.id.shared_fragment_container, SharedDetailsFragment())
        transition.addToBackStack(null)
        transition.commit()
    }
}