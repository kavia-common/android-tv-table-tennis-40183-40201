package com.example.ping_pong_frontend

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.random.Random

/**
 * PUBLIC_INTERFACE
 * GameView
 * A custom Canvas-based Pong implementation optimized for Android TV.
 * - Left paddle: Player via D-pad (UP/DOWN)
 * - Right paddle: Simple AI following the ball
 * - Physics loop with stable delta timing (~60fps)
 * - Score tracking, pause/resume controls
 * Styling follows Ocean Professional theme.
 */
class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    // Visuals
    private val surfaceColor = Color.WHITE
    private val primaryColor = ContextCompat.getColor(context, R.color.primary)
    private val secondaryColor = ContextCompat.getColor(context, R.color.secondary)
    private val textColor = ContextCompat.getColor(context, R.color.textPrimary)

    private val ballPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = primaryColor
        style = Paint.Style.FILL
    }
    private val paddlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0F172A") // deep slate for paddles
        style = Paint.Style.FILL
    }
    private val netPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#D1D5DB")
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        textSize = 42f
        textAlign = Paint.Align.CENTER
    }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#22000000")
    }

    // Game state
    private var ballX = 0f
    private var ballY = 0f
    private var ballVX = 0f
    private var ballVY = 0f
    private var ballRadius = 14f

    private var paddleWidth = 18f
    private var paddleHeight = 120f
    private var leftPaddleX = 0f
    private var leftPaddleY = 0f
    private var rightPaddleX = 0f
    private var rightPaddleY = 0f

    private var leftScore = 0
    private var rightScore = 0

    private var runningJob: Job? = null
    private var paused = false

    // Settings
    private var settings = GameSettings()
    private var aiLag = 0.12f // fraction of ball speed to follow; lower = harder
    private var paddleSpeed = 520f // px/sec

    // Callbacks for UI
    var onScoreChanged: ((Int, Int) -> Unit)? = null
    var onPausedChanged: ((Boolean) -> Unit)? = null

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        resetRound()
    }

    // PUBLIC_INTERFACE
    fun applySettings(newSettings: GameSettings) {
        /**
         * Applies difficulty and speed settings to game physics.
         */
        this.settings = newSettings
        when (newSettings.difficulty) {
            Difficulty.EASY -> {
                aiLag = 0.22f
                paddleHeight = 150f
            }
            Difficulty.MEDIUM -> {
                aiLag = 0.14f
                paddleHeight = 130f
            }
            Difficulty.HARD -> {
                aiLag = 0.10f
                paddleHeight = 115f
            }
        }
        paddleSpeed = when (newSettings.speed) {
            Speed.SLOW -> 420f
            Speed.NORMAL -> 520f
            Speed.FAST -> 640f
        }
        invalidate()
    }

    // PUBLIC_INTERFACE
    fun movePlayerPaddle(direction: Int) {
        /**
         * Moves the player's paddle. direction: -1 up, +1 down.
         */
        if (paused) return
        val dt = 1f / 60f
        leftPaddleY += direction * paddleSpeed * dt
        clampPaddles()
        invalidate()
    }

    // PUBLIC_INTERFACE
    fun togglePause() {
        /**
         * Toggles the pause state and notifies overlay.
         */
        if (paused) resume() else pause()
    }

    // PUBLIC_INTERFACE
    fun pause() {
        /**
         * Pauses the game loop.
         */
        paused = true
        onPausedChanged?.invoke(true)
    }

    // PUBLIC_INTERFACE
    fun resume() {
        /**
         * Resumes the game loop if not running.
         */
        paused = false
        onPausedChanged?.invoke(false)
        if (runningJob?.isActive != true) {
            startLoop()
        }
    }

    // PUBLIC_INTERFACE
    fun isPaused(): Boolean = paused

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startLoop()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        runningJob?.cancel()
    }

    private fun startLoop() {
        runningJob?.cancel()
        runningJob = CoroutineScope(Dispatchers.Main.immediate).launch {
            var last = System.nanoTime()
            while (isActive) {
                val now = System.nanoTime()
                val dt = ((now - last) / 1_000_000_000.0).toFloat()
                last = now
                if (!paused) {
                    update(dt.coerceIn(0f, 0.033f))
                    invalidate()
                }
                delay(1000L / 60L) // target 60 FPS
            }
        }
    }

    private fun clampPaddles() {
        val half = paddleHeight / 2f
        leftPaddleY = min(height - half - 16f, max(half + 16f, leftPaddleY))
        rightPaddleY = min(height - half - 16f, max(half + 16f, rightPaddleY))
    }

    private fun resetRound(scoredLeft: Boolean = false, scoredRight: Boolean = false) {
        if (scoredLeft) leftScore++ else if (scoredRight) rightScore++
        onScoreChanged?.invoke(leftScore, rightScore)

        // Place ball to center
        ballX = width / 2f
        ballY = height / 2f
        ballRadius = 14f

        // Random initial direction favoring last scorer
        val baseSpeed = when (settings.speed) {
            Speed.SLOW -> 380f
            Speed.NORMAL -> 460f
            Speed.FAST -> 560f
        }
        val dirX = if (scoredLeft) -1 else if (scoredRight) 1 else if (Random.nextBoolean()) 1 else -1
        ballVX = dirX * baseSpeed
        ballVY = (Random.nextFloat() - 0.5f) * baseSpeed
    }

    private fun update(dt: Float) {
        // Initial placement if first layout pass
        if (leftPaddleX == 0f && width > 0) {
            leftPaddleX = 32f
            rightPaddleX = width - 32f - paddleWidth
            leftPaddleY = height / 2f
            rightPaddleY = height / 2f
            ballX = width / 2f
            ballY = height / 2f
        }

        // Move ball
        ballX += ballVX * dt
        ballY += ballVY * dt

        val topBound = 16f
        val bottomBound = height - 16f

        // Collide with top/bottom
        if (ballY - ballRadius < topBound) {
            ballY = topBound + ballRadius
            ballVY = abs(ballVY)
        } else if (ballY + ballRadius > bottomBound) {
            ballY = bottomBound - ballRadius
            ballVY = -abs(ballVY)
        }

        // AI paddle follows ball with lag
        val targetY = ballY
        val dy = targetY - rightPaddleY
        rightPaddleY += dy * aiLag
        clampPaddles()

        // Collide with left paddle
        val leftRectTop = leftPaddleY - paddleHeight / 2f
        val leftRectBottom = leftPaddleY + paddleHeight / 2f
        if (ballX - ballRadius < leftPaddleX + paddleWidth &&
            ballY in leftRectTop..leftRectBottom &&
            ballVX < 0
        ) {
            ballX = leftPaddleX + paddleWidth + ballRadius
            ballVX = abs(ballVX) * 1.03f // slight acceleration
            // Add spin based on contact point
            val offset = (ballY - leftPaddleY) / (paddleHeight / 2f)
            ballVY += offset * 120f
        }

        // Collide with right paddle
        val rightRectTop = rightPaddleY - paddleHeight / 2f
        val rightRectBottom = rightPaddleY + paddleHeight / 2f
        if (ballX + ballRadius > rightPaddleX &&
            ballY in rightRectTop..rightRectBottom &&
            ballVX > 0
        ) {
            ballX = rightPaddleX - ballRadius
            ballVX = -abs(ballVX) * 1.03f
            val offset = (ballY - rightPaddleY) / (paddleHeight / 2f)
            ballVY += offset * 120f
        }

        // Score check
        if (ballX + ballRadius < 0f) {
            resetRound(scoredRight = true)
        } else if (ballX - ballRadius > width) {
            resetRound(scoredLeft = true)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw net (already in bg, but ensure visible)
        canvas.drawLine(width / 2f, 0f, width / 2f, height.toFloat(), netPaint)

        // Paddles with subtle shadow
        canvas.drawRect(
            leftPaddleX + 2f, leftPaddleY - paddleHeight / 2f + 2f,
            leftPaddleX + paddleWidth + 2f, leftPaddleY + paddleHeight / 2f + 2f,
            shadowPaint
        )
        canvas.drawRect(
            rightPaddleX + 2f, rightPaddleY - paddleHeight / 2f + 2f,
            rightPaddleX + paddleWidth + 2f, rightPaddleY + paddleHeight / 2f + 2f,
            shadowPaint
        )

        canvas.drawRoundRect(
            leftPaddleX, leftPaddleY - paddleHeight / 2f,
            leftPaddleX + paddleWidth, leftPaddleY + paddleHeight / 2f,
            10f, 10f, paddlePaint
        )
        canvas.drawRoundRect(
            rightPaddleX, rightPaddleY - paddleHeight / 2f,
            rightPaddleX + paddleWidth, rightPaddleY + paddleHeight / 2f,
            10f, 10f, paddlePaint
        )

        // Ball with color flip when moving right as subtle feedback
        ballPaint.color = if (ballVX > 0) secondaryColor else primaryColor
        canvas.drawCircle(ballX, ballY, ballRadius, ballPaint)
    }
}
