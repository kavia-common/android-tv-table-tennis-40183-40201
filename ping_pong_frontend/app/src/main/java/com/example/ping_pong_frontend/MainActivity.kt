package com.example.ping_pong_frontend

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import com.example.ping_pong_frontend.databinding.ActivityMainBinding

/**
 * PUBLIC_INTERFACE
 * MainActivity
 * Entry point for the Android TV Ping Pong game with Ocean Professional theme.
 * Hosts the GameView and overlays; handles DPAD input and navigation to Settings.
 */
class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize game view and UI
        gameView = binding.gameView

        // Initial UI text
        binding.bottomHints.text = "DPAD ↑/↓ move • OK = Pause • ▶ Settings"
        binding.scoreText.text = "0 : 0"

        // Top bar buttons
        binding.menuButton.setOnClickListener { togglePause() }
        binding.settingsButton.setOnClickListener { openSettings() }

        // Pause overlay actions
        binding.pauseResume.setOnClickListener { togglePause() }
        binding.pauseSettings.setOnClickListener { openSettings() }

        // Observe game state
        gameView.onScoreChanged = { left, right ->
            binding.scoreText.text = "$left : $right"
        }
        gameView.onPausedChanged = { paused ->
            binding.pauseOverlay.alpha = if (paused) 1f else 0f
            binding.pauseOverlay.isClickable = paused
            binding.pauseOverlay.isFocusable = paused
            binding.pauseText.text = if (paused) "Paused" else ""
        }
    }

    private fun togglePause() {
        gameView.togglePause()
    }

    private fun openSettings() {
        gameView.pause()
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        gameView.applySettings(SettingsRepository(this).load())
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                gameView.movePlayerPaddle(-1)
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                gameView.movePlayerPaddle(1)
                true
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                togglePause()
                true
            }
            KeyEvent.KEYCODE_MENU -> {
                openSettings()
                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (gameView.isPaused()) {
                    openSettings()
                    true
                } else false
            }
            KeyEvent.KEYCODE_BACK -> {
                if (!gameView.isPaused()) {
                    togglePause()
                    true
                } else {
                    super.onKeyDown(keyCode, event)
                }
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}
