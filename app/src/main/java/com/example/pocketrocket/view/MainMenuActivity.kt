package com.example.pocketrocket.view

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketrocket.databinding.ActivityMainMenuBinding
import com.example.pocketrocket.managers.GameManager
import com.example.pocketrocket.managers.MainMenuECS
import com.example.pocketrocket.utils.Vec2D


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var screenSize = Rect(0, 0, 0, 0)
        when (Resources.getSystem().configuration.orientation) {
            ORIENTATION_LANDSCAPE -> {
                screenSize.right = Resources.getSystem().displayMetrics.heightPixels
                screenSize.bottom = Resources.getSystem().displayMetrics.widthPixels
            }
            ORIENTATION_PORTRAIT -> {
                screenSize.bottom = Resources.getSystem().displayMetrics.heightPixels
                screenSize.right = Resources.getSystem().displayMetrics.widthPixels
            }
        }

        val gameManager = GameManager(applicationContext, binding.mainMenuSurfaceView.holder, screenSize)
        gameManager.addECS(MainMenuECS(gameManager))

        binding.exitButton.setOnClickListener {
            gameManager.stopGame()
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
        }
    }
}