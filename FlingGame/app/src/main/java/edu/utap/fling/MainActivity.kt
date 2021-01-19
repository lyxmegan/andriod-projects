package edu.utap.fling

import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*


class MainActivity :
    AppCompatActivity(),
    CoroutineScope by MainScope() {
    // A round lasts this many milliseconds
    private val durationMillis = 2000L // Increase for testing if you want
    private var score = 0
    private lateinit var timer: Timer
    private lateinit var border: Border
    private lateinit var fling: Fling
    private lateinit var jump: Jump
    private var timerActive = false
    // This determines which borders are chosen as goal boarders.
    // It is to your advantage to change these values to more thoroughly
    // test your code.
    private var goalOrder = intArrayOf(0)
    // NB: We use this variable for our testing
    var testing = false
    enum class GameType {
        JUMP, FLING
    }
    // Start on Jump
    private var gameType = GameType.JUMP

    override fun onDestroy() {
        super.onDestroy()
        cancel() // destroy all coroutines
    }


    private fun layoutThenFinishInit() {
        // The problem is that the layout has 0dp elements whose true dimension is only
        // known after layout has happened.  Unfortunately (in this case), layout
        // hasn't happened in onCreate.
        // So set a callback to initialize ourselves the content has been laid out
        allContent.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //Remove it here unless you want to get this callback for EVERY
                //layout pass, which can get you into infinite loops if you ever
                //modify the layout from within this method.
                allContent.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // frame* from XML layout
                border = Border(listOf(frameT, frameB, frameS, frameE), goalOrder)
                fling = Fling(puck, border, testing)
                jump = Jump(puck, border)
                jump.start()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        // XML button
        flingBut.setOnClickListener {
            if(gameType == GameType.FLING) {
                // If user gets impatient and reclicks button, cancel running coroutines
                coroutineContext.cancelChildren()
                doRound()
            }
        }

        gameBut.setOnClickListener {
            // Don't switch modes during a game of fling
            Log.d("XXX ${timer.millisLeft()}", "")
            if( !timerActive ) {
                // Toggle game type
                if (gameType == GameType.JUMP) {
                    jump.finish()
                    gameType = GameType.FLING
                    gameBut.text = "Fling"
                } else {
                    gameType = GameType.JUMP
                    gameBut.text = "Jump"
                    jump.start()
                }
            }
        }
        // Create timer
        timer = Timer(flingBut) // XML button
        // XML TextView
        scoreTV.text = "0"
        score = 0

        layoutThenFinishInit()
    }

    private fun doScore(millisLeft: Long) {
        // User won Get one point plus tenths of a sec left
        if( millisLeft > 0 ) {
            if (testing) {
                score++
                scoreTV.text = score.toString()
                Log.d(localClassName, "User wins ${score}")
            } else {
                score += (millisLeft / 100).toInt()
                score++
                scoreTV.text = score.toString()
                Log.d(localClassName, "User wins ${score}")
            }
        }
    }

    private fun doRound() {
        launch {
            val timerJob = async {
                timerActive = true
                timer.timerCo(durationMillis)
                fling.deactivatePuck()
                timerActive = false
            }
            fling.playRound() {
                doScore(timer.millisLeft())
                launch{timerJob.cancelAndJoin()}
                fling.deactivatePuck()
                timerActive = false
            }
        }
    }
    // Don't inflate a menu
}
