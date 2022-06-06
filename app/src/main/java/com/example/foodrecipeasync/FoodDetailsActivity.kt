package com.example.foodrecipeasync

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext


class FoodDetailsActivity(override val coroutineContext: CoroutineContext) : AppCompatActivity(), CoroutineScope {

    val api: String = "7dfe89627d1246c6b38d06d5c6de4036"
    lateinit var url: String
    lateinit var step1: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)
        val bundle: Bundle? = intent.extras
        val msg = bundle!!.getString("recipeid")
        Log.d("TAG", "ResponseId $msg")
        url="https://api.spoonacular.com/recipes/complexSearch?apiKey=7dfe89627d1246c6b38d06d5c6de4036&query=Cauliflower, Brown Rice, and Vegetable Fried Rice&addRecipeInformation=true"
        doOperation()
        recipesearchTask().execute()
    }
    private fun doOperation() {
        async {
        }
    }
    inner class recipesearchTask(): AsyncTask<Unit, Unit, String>(){
        override fun doInBackground(vararg p0: Unit?): String {

            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(url)
                urlConnection = url.openConnection() as HttpURLConnection?
                urlConnection?.connectTimeout = 7000
                urlConnection?.readTimeout = 7000
                urlConnection?.requestMethod = "GET"
                var inString = streamToString(urlConnection?.inputStream)
                return inString
            }catch (ex: Exception) {
                Log.d("TAG", "Response ${ex.printStackTrace()}")
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
            }
            return ""
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            Log.d("Tag", "onPostExecutive Response4 $result")

            var data:String=""
            var ingerdients:String=""
            try{
                val jsonObj = JSONObject(result)
                val results = jsonObj.getJSONArray("results").getJSONObject(0)
                val title=  results.getString("title")
                val  servings= getString(R.string.serving) + " " +results.getString("servings")+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+" "+getString(R.string.timereq) + " " +results.getString("readyInMinutes")
                val timerequired= getString(R.string.timereq) + " " +results.getString("readyInMinutes")
                val summary=results.getString("summary")
                val images=results.getString("image")
                val instruction= results.getJSONArray("analyzedInstructions").getJSONObject(0)
                val steps= instruction.getJSONArray("steps")
                for(i  in 0.. steps.length()-1){
                    var json_objectdetail = steps.getJSONObject(i)
                    data+=" " +json_objectdetail.getString("step")
                    var items= JSONArray(json_objectdetail.getString("ingredients"))
                    for(j in 0.. items.length()-1){
                        var item= items.getJSONObject(j)
                        ingerdients+=" " +item.getString("name")
                    }
                }
                // val name=getString(R.string.ingerdients) + " " + ingerdients.getString("name")
                findViewById<TextView>(R.id.textViewtitle).text = title
                val recipeimages: ImageView =findViewById(R.id.imageView2)
                Glide.with(this@FoodDetailsActivity).load(images).centerCrop().into(recipeimages)
                findViewById<TextView>(R.id.textViewservings).text =servings
                findViewById<TextView>(R.id.textViewtime).text =getString(R.string.ingerdients) + ingerdients
                findViewById<TextView>(R.id.textViewsummary).text =getString(R.string.steps) +data
            }catch (ex: Exception) {
                Log.d("TAG", "Exceptions ${ex.printStackTrace()}")
            }
        }
    }
    fun streamToString(inputStream: InputStream?): String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        var result = ""
        try {
            do {
                line = bufferReader.readLine()
                if (line != null) {
                    result += line
                }
            } while (true)
            inputStream?.close()
        } catch (ex: Exception) {

        }

        return result
    }
}