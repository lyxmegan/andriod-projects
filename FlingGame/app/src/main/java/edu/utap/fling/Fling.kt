package edu.utap.fling

import android.annotation.SuppressLint
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.ImageView
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import kotlin.math.abs
import kotlin.math.roundToInt


class Fling(private val puck: ImageView,
            private val border: Border,
            private val testing: Boolean
)  {
    private val puckMinX = border.minX().toFloat()
    private val puckMaxX = (border.maxX() - puck.width).toFloat()
    private val puckMinY= border.minY().toFloat()
    private val puckMaxY = (border.maxY() - puck.height).toFloat()
    private val friction = 3.0f
    private var goalBorder = Border.Type.T
    private lateinit var flingAnimationX: FlingAnimation
    private lateinit var flingAnimationY: FlingAnimation

    private fun placePuck() {
        if (testing) {
            puck.x = ((border.maxX() - border.minX()) / 2).toFloat()
            puck.y = ((border.maxY() - border.minY()) / 2).toFloat()
        } else {
            // XXX Write me
            puck.x = border.randomX(puck.width)
            puck.y = border.randomY(puck.height)
        }
        // If puck had been made invisible, make it visible now
        puck.visibility = View.VISIBLE
    }

    private fun success(goalAchieved: () -> Unit) {
        // XXX Write me
        flingAnimationX.cancel()
        flingAnimationY.cancel()
        puck.visibility = View.INVISIBLE
        goalAchieved()
    }

    fun makeXFlingAnimation(initVelocity: Float,
                            goalAchieved: () -> Unit): FlingAnimation {
        //Log.d("XXX", "Fling X vel $initVelocity")
        return FlingAnimation(puck, DynamicAnimation.X)
                .setFriction(friction)
                .apply {
                    setStartVelocity(initVelocity)
                    setMaxValue(puckMaxX)
                    setMinValue(puckMinX)
                    setStartValue(puck.x)
                    minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS
            }
    }

    fun makeYFlingAnimation(initVelocity: Float,
                            goalAchieved: () -> Unit): FlingAnimation {
       // Log.d("XXX", "Fling Y vel $initVelocity")
        return FlingAnimation(puck, DynamicAnimation.Y)
                .setFriction(friction)
                .apply {
                        setStartVelocity(initVelocity)
                        setMinValue(puckMinY)
                        setMaxValue(puckMaxY)
                        setStartValue(puck.y)
                        minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS
                }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun listenPuck(goalAchieved: ()->Unit) {
        // A SimpleOnGestureListener notifies us when the user puts their
        // finger down, and when they edu.utap.edu.utap.fling.
        // Note that here we construct the listener object "on the fly"
        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(
                e1: MotionEvent?, e2: MotionEvent?,
                velocityX: Float, velocityY: Float
            ): Boolean {
                // XXX Write me
                flingAnimationX = makeXFlingAnimation(velocityX, goalAchieved)
                flingAnimationY = makeYFlingAnimation(velocityY, goalAchieved)
                flingAnimationX.addEndListener { animation, canceled, value, velocity ->
                    when(goalBorder){
                        Border.Type.S -> { if(value == puckMinX) {success(goalAchieved)}}
                        Border.Type.E -> { if(value == puckMaxX) {success(goalAchieved)}}
                    }
                    makeXFlingAnimation(-velocity, goalAchieved).start()
                }.start()
                flingAnimationY. addEndListener { animation, canceled, value, velocity ->
                    when(goalBorder){
                        Border.Type.T -> { if(value == puckMinY) {success(goalAchieved)} }
                        Border.Type.B -> { if(value == puckMaxY) {success(goalAchieved)} }
                    }
                    makeYFlingAnimation(-velocity, goalAchieved).start()
                }.start()
                return true
                }
            }

        val gestureDetector = GestureDetector(puck.context, gestureListener)
        // When Android senses that the puck is being touched, it will call this code
        // with a motionEvent object that describes the motion.  Our detector
        // will take sequences of motion events and send them to the gesture listener to
        // let us know what the user is doing.
        puck.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun deactivatePuck() {
        // XXX Write me
        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return false
            }
        }
        val gestureDetector = GestureDetector(puck.context, gestureListener)
        puck.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
        }
    }

    fun playRound(goalAchieved: () -> Unit) {
        // XXX Write me
        border.resetBorderColors()
        placePuck()
        listenPuck(goalAchieved)
        border.nextGoal()
    }
}