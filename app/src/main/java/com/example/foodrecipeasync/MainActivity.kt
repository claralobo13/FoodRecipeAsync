package com.example.foodrecipeasync

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var lv: ListView
    val api: String = "91e9af87e23446fba7be44399ac65782"
    lateinit var url: String
    lateinit var title: String
    lateinit var image: String
    lateinit var adapter: ArrayAdapter<*>
    lateinit var urls: URL
    var id:Int=0
    var recipeimages: ArrayList<Int> = ArrayList()


    private var recipenames = ArrayList<DataRecipe>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: CustomRecipeList? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        url="https://api.spoonacular.com/recipes/complexSearch?apiKey=$api&number=100"
        recyclerView = findViewById<RecyclerView>(R.id.listView)
        recipesearchTask().execute()

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
            Log.d("Tag", "onPostExecutive Response $result")
            try{

                Log.d("Tag", "onPostExecutive Response $result")
                val jsonObj = JSONObject(result)
                val results = jsonObj.getJSONArray("results")
                var size:Int = results.length()
                for(i  in 0.. size-1){
                    var json_objectdetail = results.getJSONObject(i)
                    title= json_objectdetail.getString("title")
                    image=json_objectdetail.getString("image")
                    id= json_objectdetail.getInt("id")
                    val name1= DataRecipe(title,image,id)
                    recipenames.add(name1)

                }
                createListView()

//               val image= results.getString("image")
//               urls = URL(image)
//               val newtask = NewAsyncTask()
//               newtask.execute()


            }catch (ex: Exception) {
                Log.d("TAG", "Exceptions ${ex.printStackTrace()}")
            }
        }
    }

    fun createListView() {
        mAdapter = CustomRecipeList(recipenames, this)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter
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

    fun onItemClick(position: Int) {
        Toast.makeText(this,"Item $position clicked", Toast.LENGTH_SHORT).show()
        Log.d("TAG3", ""+title)
        var datarecipe:DataRecipe= recipenames.get(position)
        Log.d("TAG4", ""+datarecipe.recipename)
        val intent = Intent(this, FoodDetailsActivity::class.java)
        intent.putExtra("recipeid", datarecipe.recipename)
        startActivity(intent)
    }
}