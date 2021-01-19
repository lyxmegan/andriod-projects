package edu.cs371m.colorrecycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


/**
 * Created by witchel on 2/5/2018, modified since
 */
class SimpleAdapter(context: Context,
                    colorList: List<ColorList.ColorName>)
    : BaseAdapter() {
    private var inflater = LayoutInflater.from(context)

    // Create a new, writable list and copy contents of input colorList
    private var list = mutableListOf<ColorList.ColorName>().apply {
        addAll(colorList)
    }

    /////////////////////////////////////////////////////////////
    // These must be provided for any BaseAdapter implementation
    override fun getCount(): Int {
        return list.size //returns total of items in the list
    }

    override fun getItem(position: Int): ColorList.ColorName {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun bindView(view: View, colorAndName: ColorList.ColorName) {
        // XXX Write me (and call me)
        val textView = view.findViewById<TextView>(R.id.text)
        textView.text = colorAndName.name
        textView.setBackgroundColor(colorAndName.color)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // XXX Write me.
        if(convertView == null){
            Log.d("view", "$position NULL")
        } else{
            Log.d("view", "$position not Null")
        }
        val colorAndName = getItem(position)
        var returnView: View = convertView ?: inflater.inflate(R.layout.row, parent, false)
        bindView(returnView,colorAndName )
        return returnView
    }

    fun moveToTop(position: Int) {
        val colorName = getItem(position)
        list.remove(colorName)
        list.add(0, colorName)
        notifyDataSetChanged()
    }
}
