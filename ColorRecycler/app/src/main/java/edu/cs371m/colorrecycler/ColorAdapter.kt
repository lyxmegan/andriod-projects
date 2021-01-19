package edu.cs371m.colorrecycler

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row.view.*
import java.util.*
import java.util.Collections.swap
import kotlin.random.Random

/**
 * Created by witchel on 1/29/18.  Subsequently modified.
 */

class ColorAdapter(private val mContext: Context,
                   private val colorList: List<ColorList.ColorName>)
    : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {
    private var random = Random(System.currentTimeMillis())
    // Create a new, writable list that we initialize with colorList
    private var list = mutableListOf<ColorList.ColorName>().apply {
        addAll(colorList.shuffled())
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var textView = v.findViewById<TextView>(R.id.tv)

        init {
            // XXX Write me. setOnClickListener, look at adapterPosition in Android docs
            v.setOnClickListener{
                val position = adapterPosition
                swapItem(position)
                Log.d("position", "$position")
            }
        }
        fun bind(pos: Int) {
            // XXX Write me.
            val colorName = list[pos]
            val luminance = getLuminance(colorName.color)
            val display = String.format("%s %1.2f", colorName.name, luminance)
            textView.text = display
            textView.setBackgroundColor(colorName.color)
            if(luminance < 0.3) {
                textView.setTextColor(Color.parseColor("#FFFFFF"))
            } else{textView.setTextColor(Color.parseColor("#000000"))}
        }
    }

    override fun getItemCount(): Int {
        return ColorList.size()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new View
        val v = LayoutInflater.from(mContext).inflate(R.layout.color_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun swapItem(position: Int) {
        if (list.size <= 1) return
        var index = random.nextInt(list.size)
        while (index == position) {
            index = random.nextInt(list.size)
        }
        // XXX Write me (swap list item at position with the one at index)
        val posName = list[position].name
        val posColor = list[position].color
        val indexName = list[index].name
        val indexColor = list[index].color
        list[position].apply {
            color = indexColor
            name = indexName
        }
        list[index].apply{
            color = posColor
            name = posName
        }
        notifyDataSetChanged()
    }

    // A static function for computing luminance
    companion object {
        fun getLuminance(color: Int): Float {
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)

            val hsl = FloatArray(3)
            ColorUtils.RGBToHSL(red, green, blue, hsl)
            return hsl[2]
        }
    }
}