package edu.utap.fling

import android.graphics.Point
import android.util.Log
import android.view.View
import android.widget.ImageView

class Jump(private val puck: ImageView,
           private val border: Border) {
    // XXX remember some X and Y values and any other state
    private val xMin = border.minX()
    private val xMax = border.maxX() - puck.width
    private val yMin = border.minY()
    private val yMax = border.maxY() - puck.height
    var state = 0

    private fun placePuck() {
        when(state % 4){
            0 -> {
                puck.x = xMin.toFloat()
                puck.y = yMin.toFloat()
            }
            1 -> {
                puck.x = xMax.toFloat()
                puck.y = yMin.toFloat()
            }
            2 -> {
                puck.x = xMax.toFloat()
                puck.y = yMax.toFloat()
            }
            3 -> {
                puck.x = xMin.toFloat()
                puck.y = yMax.toFloat()
            }
        }
        state += 1
    }

    fun start() {
        puck.visibility = View.VISIBLE
        puck.isClickable = true
        // XXX Write me
        border.resetBorderColors()
        placePuck()
        puck.setOnClickListener {
                placePuck()
        }
    }

    fun finish() {
        // XXX Write me
        puck.setOnClickListener(null)
        state -= 1
    }
}
