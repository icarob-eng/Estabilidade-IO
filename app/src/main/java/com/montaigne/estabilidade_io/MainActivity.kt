package com.montaigne.estabilidade_io

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.montaigne.estabilidade_io.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding.buttonSubmit.setOnClickListener { binding.textOut.text = binding.inputMultiLine.text }
    }
}