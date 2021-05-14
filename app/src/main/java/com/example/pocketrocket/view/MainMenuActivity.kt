package com.example.pocketrocket.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketrocket.databinding.ActivityMainMenuBinding
import com.example.pocketrocket.managers.GameManager
import com.example.pocketrocket.managers.MainMenuECS

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GameManager(applicationContext, binding.mainMenuSurfaceView.holder, MainMenuECS())

        binding.exitButton.setOnClickListener { finish() }
    }
}