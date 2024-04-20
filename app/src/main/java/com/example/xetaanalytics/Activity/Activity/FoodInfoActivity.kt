package com.example.xetaanalytics.Activity.Activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.xetaanalytics.Activity.Adapter.FactsRecyclerAdapter
import com.example.xetaanalytics.Activity.utils.ConnectionManager
import com.example.xetaanalytics.Activity.Adapter.SimilarRecyclerAdapter
import com.example.xetaanalytics.R
import com.example.xetaanalytics.Activity.model.FoodData
import com.example.xetaanalytics.Activity.model.NutritionInfoScaled
import com.example.xetaanalytics.Activity.model.SimilarItem

import org.json.JSONArray

class FoodInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_food_info)

        val description:TextView = findViewById(R.id.description)
        val textViewRating :TextView = findViewById(R.id.textViewRating)
        val recyclerVfacts : RecyclerView = findViewById(R.id.recyclerVfacts)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        val foodInfoList = arrayListOf<String>()
        val recyclerAdapter: FactsRecyclerAdapter = FactsRecyclerAdapter(this,foodInfoList)

        val recyclerSitems : RecyclerView = findViewById(R.id.recyclerSitems)
        val layoutManager1 : RecyclerView.LayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        val SimilarList = arrayListOf<SimilarItem>() // Initialize empty list
        val recyclerAdapter1: SimilarRecyclerAdapter = SimilarRecyclerAdapter(this, SimilarList)
        val tableLayoutNutrition :TableLayout = findViewById(R.id.tableLayoutNutrition)

        // Find views
        val backButton: ImageButton = findViewById(R.id.backButton)
        val backText: TextView = findViewById(R.id.backText)

        // Set click listeners
        backButton.setOnClickListener {
            // Create an intent to navigate back to the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity
        }

        backText.setOnClickListener {
            // Create an intent to navigate back to the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity
        }



        if(ConnectionManager().checkConnectivity(this)){

            val queue = Volley.newRequestQueue(this)
            val url = "http://52.25.229.242:8000/food_info/"

            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET , url , null,
                Response.Listener{
                                 println("Response is $it")

                    val success = it.getBoolean("success")

                    if(success){

                        // Remove any existing rows from the table
                        val data = it.getJSONObject("data")
                        val foodData = FoodData(
                            description = data.getString("description"),
                            genericFacts = jsonArrayToStringList(data.getJSONArray("generic_facts")),
                            healthRating = data.getDouble("health_rating"),
                            nutritionInfoScaled = parseNutritionInfoScaled(data.getJSONArray("nutrition_info_scaled")),
                            similarItems = parseSimilarItems(data.getJSONArray("similar_items"))

                        )
                        // Update SimilarList with parsed similar items
                        SimilarList.clear()
                        SimilarList.addAll(foodData.similarItems)
                        foodInfoList.addAll(foodData.genericFacts)
//                        nutrientInfoList.addAll(foodData.nutritionInfoScaled)
                        // Add a new row for each nutrient in the nutritionInfoScaled list
                        for (nutrition in foodData.nutritionInfoScaled) {
                            val row = TableRow(this@FoodInfoActivity)
                            row.setPadding(0, 0, 0, 0) // Reset padding for the row


                            val nameTextView = TextView(this@FoodInfoActivity)
                            nameTextView.text = nutrition.name
                            nameTextView.setPadding(15, 30, 30, 0)

                            val valueTextView = TextView(this@FoodInfoActivity)
                            valueTextView.text = nutrition.value.toString()

                            val unitsTextView = TextView(this@FoodInfoActivity)
                            unitsTextView.text = nutrition.units

                            // Add TextViews to the TableRow
                            row.addView(nameTextView)
                            row.addView(valueTextView)
                            row.addView(unitsTextView)

                            // Add the TableRow to the TableLayout
                            tableLayoutNutrition.addView(row)
                        }



                        val foodInfoResponse = FoodData(
                            description = foodData.description,
                            genericFacts = foodData.genericFacts,
                            healthRating = foodData.healthRating,
                            similarItems = foodData.similarItems,
                            nutritionInfoScaled = foodData.nutritionInfoScaled

                        )
                        data.let { description.text = foodData.description
                        textViewRating.text = foodData.healthRating.toString()}
                        // Update the adapter with the new data
                        recyclerAdapter.notifyDataSetChanged()
                        recyclerAdapter1.notifyDataSetChanged()

                    }
                }, Response.ErrorListener {
                    println("Error is $it")

                })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "26c5144c5b9c13"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
            // Set adapter and layout manager for RecyclerView
            recyclerVfacts.layoutManager = layoutManager
            recyclerVfacts.adapter = recyclerAdapter

            recyclerSitems.layoutManager = layoutManager1
            recyclerSitems.adapter = recyclerAdapter1



        }
        else{
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found ")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this?.finish()
            }
            dialog.setNegativeButton("Cancel"){text, listener -> // do nothing
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }


    }


    private fun jsonArrayToStringList(jsonArray: JSONArray): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }

    fun parseNutritionInfoScaled(jsonArray: JSONArray): List<NutritionInfoScaled> {
        val nutritionInfoScaled = mutableListOf<NutritionInfoScaled>()
        for (i in 0 until jsonArray.length()) {
            val nutritionScaledObject = jsonArray.getJSONObject(i)
            val name = nutritionScaledObject.getString("name")
            val value = nutritionScaledObject.getDouble("value").toInt()
            val units = nutritionScaledObject.getString("units")
            if (value != 0) { // Check if value is not equal to 0
                nutritionInfoScaled.add(NutritionInfoScaled(name, value, units))
            }
        }
        return nutritionInfoScaled
    }
//
    fun parseSimilarItems(jsonArray: JSONArray): List<SimilarItem> {
        val similarItems = mutableListOf<SimilarItem>()
        for (i in 0 until jsonArray.length()) {
            val similarItemObject = jsonArray.getJSONObject(i)
            val itemId = similarItemObject.getString("_id")
            val itemName = similarItemObject.getString("name")
            val itemImage = similarItemObject.getString("image")
            similarItems.add(SimilarItem(itemId, itemName, itemImage))
        }
        return similarItems
    }
}