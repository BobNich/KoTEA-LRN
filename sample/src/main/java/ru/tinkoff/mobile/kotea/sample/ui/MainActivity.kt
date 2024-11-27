package ru.tinkoff.mobile.kotea.sample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.tinkoff.mobile.kotea.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.buttonSimple.setOnClickListener {
            Intent(this, SimpleSampleActivity::class.java).also(::startActivity)
        }
        binding.buttonShared.setOnClickListener {
            Intent(this, SharedSampleActivity::class.java).also(::startActivity)
        }
        setContentView(binding.root)
    }
}