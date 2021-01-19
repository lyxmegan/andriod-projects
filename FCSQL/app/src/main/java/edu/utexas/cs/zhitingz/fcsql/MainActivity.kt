package edu.utexas.cs.zhitingz.fcsql

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var restaurantDb: SQLiteDatabase
    private lateinit var dbHelper: DatabaseHelper
    private var restaurantAdapter: RestaurantItemAdapter? = null
    private val cities: Array<String> by lazy {
        resources.getStringArray(R.array.city)
    }
    private val restarantTypes: Array<String> by lazy {
        resources.getStringArray(R.array.restaurant_type)
    }
    private val order: Array<String> by lazy {
        resources.getStringArray(R.array.order)
    }
    private val limit: Array<String> by lazy {
        resources.getStringArray(R.array.limit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restaurantTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.restaurant_type,
                android.R.layout.simple_spinner_item)
        restaurantTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        restaurant_type_spinner.adapter = restaurantTypeAdapter

        val cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.city,
                android.R.layout.simple_spinner_item)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        city_spinner.adapter = cityAdapter

        val limitAdapter = ArrayAdapter.createFromResource(this,
                R.array.limit,
                android.R.layout.simple_spinner_item)
        limitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        limit_spinner.adapter = limitAdapter

        val orderAdapter = ArrayAdapter.createFromResource(this,
                R.array.order,
                android.R.layout.simple_spinner_item)
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        choose_order_spinner.adapter = orderAdapter


        query_button.setOnClickListener(this)

        dbHelper = DatabaseHelper(this)
        try {
            dbHelper.createDatabase()
        } catch (e: IOException) {
            Log.e("DB", "Fail to create database", e)
        }
        restaurantDb = dbHelper.readableDatabase

    }

    override fun onClick(v: View) {
        // where contains the selection clause and args contains the corresponding arguments
        val where = ArrayList<String>()
        val args = ArrayList<String>()

        // XXX WRITE ME: Create table string that is the name of
        // the table we are querying in the database

        //var tableCursor = restaurantDb.rawQuery("SELECT * FROM businesses", null)
        var table = "businesses"

        // XXX WRITE ME: Generate selection clause for query city and restaurant type
        // Please use query instead of rawQuery.
        // There are two helper function you can use handleCity and handleRestaurant.
        // The query for restaurant type is provided as an example.
        handleCity(where, args)
        table = handleRestaurant(table, where, args)

        var selectionStr = ""
        if (where.size != 0) {
            selectionStr += where[0]
            for (i in 1 until where.size) {
                selectionStr += " AND " + where[i]
            }
        }

        // XXX WRITE ME: Handle ORDER BY and LIMIT request
        val limitPos = limit_spinner.selectedItemPosition
        var limitSelected = ""
        if(limitPos != 0){
            limitSelected = limit[limitPos]
        }
        var orderSelected = ""
        if(price_order_checkbox.isChecked) {
            val orderPos = choose_order_spinner.selectedItemPosition
            if(orderPos != 0){
                orderSelected = "price "+ order[orderPos]
            }
        }

        // XXX WRITE ME: query database and show result in the ListView.
        // Look at the documentation for SQLiteDatabase.query
        // https://developer.android.com/reference/kotlin/android/database/sqlite/SQLiteDatabase.html#query
        // You can pass null to columns, groupBy and having
        // If the query result is empty, generate a toast.
        val cursor = restaurantDb.query(
                table,
                null,
                selectionStr,
                args.toTypedArray(),
                null,
                null,
                orderSelected,
                limitSelected
        )
        Log.d("count", "${cursor.count}")
        restaurantAdapter = RestaurantItemAdapter(this, cursor, false)
        if(cursor.count != 0){
            with(cursor) {
                while (moveToNext()) {
                    restaurant_list.adapter = restaurantAdapter
                    restaurantAdapter!!.changeCursor(cursor)
                    restaurantAdapter!!.notifyDataSetChanged()
                }
            }
        } else {
            Toast.makeText(applicationContext, "no match found", Toast.LENGTH_SHORT).show()
            restaurant_list.adapter = restaurantAdapter
            restaurantAdapter!!.changeCursor(null)
            restaurantAdapter!!.notifyDataSetChanged()
        }
    }

    // Helper method for generate selection clause for query city
    private fun handleCity(where: MutableList<String>, args: MutableList<String>) {
        // XXX Write me.
        val cityPos = city_spinner.selectedItemPosition
        var cityName = ""
        if(cityPos != 0) {
            cityName = cities[cityPos]
            where.add("city = ?")
            args.add(cityName)
        }
    }

    // Helper method to generate the selection clause for the restaurant type.
    private fun handleRestaurant(tableStr: String, where: MutableList<String>, args: MutableList<String>): String {
        var table = tableStr
        val restaurantTypePos = restaurant_type_spinner.selectedItemPosition
        if (restaurantTypePos != 0) {
            var categoryFilter = ""
            when {
                restaurantTypePos == 1 -> categoryFilter = "newamerican"
                restaurantTypePos == 2 -> categoryFilter = "breakfast_brunch"
                // These are lower cased in the database, but cities are not
                restaurantTypePos > 0 -> categoryFilter = restarantTypes[restaurantTypePos].toLowerCase(Locale.getDefault())
            }
            table += ", categories"
            where.add("(businesses._id = categories._id) AND (categories.category_name = ?)")
            args.add(categoryFilter)
        }
        return table
    }
}
