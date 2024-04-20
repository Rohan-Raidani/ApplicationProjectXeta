package com.example.xetaanalytics.Activity.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.xetaanalytics.Activity.utils.ConnectionManager
import com.example.xetaanalytics.R
import com.example.xetaanalytics.Activity.model.Category
import com.example.xetaanalytics.Activity.model.Data
import com.example.xetaanalytics.Activity.model.Plan
import com.example.xetaanalytics.Activity.model.Section1
import com.example.xetaanalytics.Activity.model.Section2
import com.example.xetaanalytics.Activity.model.Section3
import com.example.xetaanalytics.Activity.model.Section4
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Declaring variables and geting ids
        val textViewDate: TextView = findViewById(R.id.textViewDate)
        val textViewDPts : TextView = findViewById<TextView>(R.id.textViewDPts)
        val textViewEPts: TextView  = findViewById<TextView>(R.id.textViewEPts)
        val textView1 : TextView = findViewById<TextView>(R.id.textView1)
        val textView3 : TextView = findViewById<TextView>(R.id.textView3)
        val textView5 : TextView = findViewById<TextView>(R.id.textView5)
        val textViewG1 : TextView = findViewById(R.id.textViewG1)
        val textViewF1 : TextView = findViewById(R.id.textViewF1)
        val textViewF4 : TextView = findViewById(R.id.textViewF4)
        val textViewF3 : TextView = findViewById(R.id.textViewF3)
        val textViewF2 : TextView = findViewById(R.id.textViewF2)
        val progressText2 :  TextView = findViewById(R.id.progressText2)

        val outerRingProgressBar = findViewById<ProgressBar>(R.id.outerRingProgressBar)
        val innerProgressBar = findViewById<ProgressBar>(R.id.innerProgressBar)
        val GoalProgressBar = findViewById<ProgressBar>(R.id.GoalProgressBar)

        val bottomIcon4:ImageView = findViewById(R.id.bottomIcon4)




        // Get the current date and format it
        val currentDate = SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault()).format(Date())

        // Set the text of the TextView
        textViewDate.text = currentDate

        if(ConnectionManager().checkConnectivity(this)){

            val queue = Volley.newRequestQueue(this)
            val url = "http://52.25.229.242:8000/homepage_v2/"

            val jsonObjectRequest = object :JsonObjectRequest(Request.Method.GET , url , null,
                Response.Listener{
                    try {


                        val success = it.getBoolean("success")
                        if (success) {

                            val data = it.getJSONObject("data")
                            val section1Object = data.getJSONObject("section_1")
                            val section1 = Section1(
                                plan_name = section1Object.getString("plan_name"),
                                progress = section1Object.getString("progress")
                            )

                            val section2Object = data.getJSONObject("section_2")
                            val section2 = Section2(
                                accuracy = section2Object.getString("accuracy"),
                                workout_duration = section2Object.getString("workout_duration"),
                                reps = section2Object.getInt("reps"),
                                calories_burned = section2Object.getInt("calories_burned")
                            )

                            val section3Object = data.getJSONObject("section_3")
                            val plan1Object = section3Object.getJSONObject("plan_1")
                            val plan2Object = section3Object.getJSONObject("plan_2")
                            val plan1 = Plan(
                                plan_name = plan1Object.getString("plan_name"),
                                difficulty = plan1Object.getString("difficulty")
                            )
                            val plan2 = Plan(
                                plan_name = plan2Object.getString("plan_name"),
                                difficulty = plan2Object.getString("difficulty")
                            )
                            val section3 = Section3(plan1, plan2)

                            val section4Object = data.getJSONObject("section_4")
                            val category1Object = section4Object.getJSONObject("category_1")
                            val category2Object = section4Object.getJSONObject("category_2")
                            val category1 = Category(
                                category_name = category1Object.getString("category_name"),
                                no_of_exercises = category1Object.getString("no_of_exercises")
                            )
                            val category2 = Category(
                                category_name = category2Object.getString("category_name"),
                                no_of_exercises = category2Object.getString("no_of_exercises")
                            )
                            val section4 = Section4(category1, category2)

                            val parsedData = Data(section1, section2, section3, section4)

                            data.let {
                                // Update TextViews with fetched data
                                textViewG1.text = section1.plan_name
                                progressText2.text = section1.progress

                                textView1.text = section2.calories_burned.toString()
                                textView5.text = section2.accuracy
                                textView3.text = section2.workout_duration.toString()
                                outerRingProgressBar.progress = section2.reps.toInt()

                                textViewF1.text = section3.plan_1.plan_name
                                textViewF2.text = section3.plan_1.difficulty
                                textViewF4.text = section3.plan_2.plan_name
                                textViewF3.text = section3.plan_2.difficulty


                            }

                        }else{
                            Toast.makeText(this,"Some Error Occured!!!",Toast.LENGTH_SHORT).show()
                        }

                    }catch (e:JSONException){
                        Toast.makeText(this,"Some Unexpected Error Occured!!!",Toast.LENGTH_SHORT).show()
                    }
            }, Response.ErrorListener {
                    Toast.makeText(this,"Volley Error Occured!!!",Toast.LENGTH_SHORT).show()

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
        bottomIcon4.setOnClickListener {
            val intent = Intent(this@MainActivity, FoodInfoActivity::class.java)
            startActivity(intent)
        }
    }
}