package com.example.pocketrocket.view

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketrocket.databinding.ActivityMainMenuBinding
import com.example.pocketrocket.managers.GameManager
import com.example.pocketrocket.managers.MainMenuECS


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val height = Resources.getSystem().displayMetrics.heightPixels
        val width = Resources.getSystem().displayMetrics.widthPixels
        val gameManager = GameManager(applicationContext, binding.mainMenuSurfaceView.holder, width, height)
        gameManager.addECS(MainMenuECS(gameManager))

        binding.exitButton.setOnClickListener {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
        }
    }
}