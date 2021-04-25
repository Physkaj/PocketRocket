package com.example.pocketrocket.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pocketrocket.control.GameMaster
import com.example.pocketrocket.control.MainMenuMaster
import com.example.pocketrocket.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainMenuMaster(binding.mainMenuSurfaceView.holder, applicationContext)

        binding.exitButton.setOnClickListener { finish() }
    }
}