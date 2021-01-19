package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var lastNumeric : Boolean = false
    var lastDot : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //the view is button itself
    fun onDigit(view: View){
        //Toast.makeText(this, "Button works", Toast.LENGTH_SHORT).show()
        tvInput.append((view as Button).text)
        lastNumeric = true
    }
    fun clear(view: View){
        tvInput.text=""
        //after clear when need to set everything back to init
        lastNumeric = false
        lastDot = false
    }
    fun onDecimalPoint(view: View){
        if(lastNumeric && !lastDot){
            tvInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }
    fun onEqual(view: View){
        //first we need to check if end with number
        if(lastNumeric){
            var tvValue = tvInput.text.toString()
            var prefix = ""
            try {
                if(tvValue.startsWith("-")){
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                if(tvValue.contains("-")){
                    val splitValue = tvValue.split("-")
                    var one = splitValue[0]
                    var two = splitValue[1]
                    //when 4-8 = -4, -4-6 this is gonna crush , because in here empty will be the one and 4 gonna be the two , 6 is three
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    tvInput.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                } else if(tvValue.contains("*")){
                    val splitValue = tvValue.split("*")
                    var one = splitValue[0]
                    var two = splitValue[1]
                    //when 4-8 = -4, -4-6 this is gonna crush , because in here empty will be the one and 4 gonna be the two , 6 is three
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    tvInput.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                } else if(tvValue.contains("+")){
                    val splitValue = tvValue.split("+")
                    var one = splitValue[0]
                    var two = splitValue[1]
                    //when 4-8 = -4, -4-6 this is gonna crush , because in here empty will be the one and 4 gonna be the two , 6 is three
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    tvInput.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                } else if(tvValue.contains("/")){
                    val splitValue = tvValue.split("/")
                    var one = splitValue[0]
                    var two = splitValue[1]
                    //when 4-8 = -4, -4-6 this is gonna crush , because in here empty will be the one and 4 gonna be the two , 6 is three
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    tvInput.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                }


            }catch (e: ArithmeticException){
                e.printStackTrace()
            }
        }

    }

    private fun removeZeroAfterDot(result: String): String{
        var value = result
        if(result.contains(".0")){
            value = result.substring(0, result.length-2)

        }
        return value
    }


    fun onOperator(view: View){
        //when this if statement is true
        if(lastNumeric && !isOperatorAdded(tvInput.text.toString())){
            tvInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }

    }

    //value.contains return true or false
    private fun isOperatorAdded(value: String): Boolean{
        return if(value.startsWith("-")){
            false
        }else{
            value.contains("/")  || value.contains("+") || value.contains("-")
                    || value.contains("*")
        }

    }
}