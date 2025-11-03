package com.example.ping_pong_frontend

import android.content.Context

/**
 * PUBLIC_INTERFACE
 * GameSettings
 * Holds user-selectable settings for game difficulty and speed.
 */
data class GameSettings(
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val speed: Speed = Speed.NORMAL
)

/**
 * PUBLIC_INTERFACE
 * Difficulty
 * EASY: larger paddles and slower AI, HARD: smaller paddles and faster AI.
 */
enum class Difficulty { EASY, MEDIUM, HARD }

/**
 * PUBLIC_INTERFACE
 * Speed
 * Controls ball and paddle base speeds.
 */
enum class Speed { SLOW, NORMAL, FAST }

/**
 * PUBLIC_INTERFACE
 * SettingsRepository
 * Provides persistence for GameSettings using SharedPreferences.
 */
class SettingsRepository(private val context: Context) {

    private val prefs by lazy {
        context.getSharedPreferences("pong_settings", Context.MODE_PRIVATE)
    }

    fun load(): GameSettings {
        val diff = prefs.getString("difficulty", Difficulty.MEDIUM.name)!!
        val speed = prefs.getString("speed", Speed.NORMAL.name)!!
        return GameSettings(
            difficulty = Difficulty.valueOf(diff),
            speed = Speed.valueOf(speed)
        )
    }

    fun save(settings: GameSettings) {
        prefs.edit()
            .putString("difficulty", settings.difficulty.name)
            .putString("speed", settings.speed.name)
            .apply()
    }
}
