package com.example.ping_pong_frontend

import android.os.Bundle
import android.view.KeyEvent
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import com.example.ping_pong_frontend.databinding.ActivitySettingsBinding

/**
 * PUBLIC_INTERFACE
 * SettingsActivity
 * A simple TV-friendly settings screen for difficulty and speed.
 * Uses SharedPreferences for persistence via SettingsRepository.
 */
class SettingsActivity : FragmentActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var repo: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = SettingsRepository(this)
        val current = repo.load()

        // Populate spinners
        val difficulties = Difficulty.values().map { it.name }
        val speeds = Speed.values().map { it.name }

        binding.difficultySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, difficulties)
        binding.speedSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, speeds)

        binding.difficultySpinner.setSelection(Difficulty.values().indexOf(current.difficulty))
        binding.speedSpinner.setSelection(Speed.values().indexOf(current.speed))

        binding.saveButton.setOnClickListener {
            val newSettings = GameSettings(
                difficulty = Difficulty.valueOf(binding.difficultySpinner.selectedItem as String),
                speed = Speed.valueOf(binding.speedSpinner.selectedItem as String)
            )
            repo.save(newSettings)
            finish()
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
