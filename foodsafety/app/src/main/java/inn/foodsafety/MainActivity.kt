package inn.foodsafety

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.TextView

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val key = "https://openapi.foodsafetykorea.go.kr/api//COOKRCP01/json/1/1000"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)

        val site = "https://openapi.foodsafetykorea.go.kr/api"
        val keyId = "/727dc391b1cd417785b9"
        val serviceId = "/COOKRCP01"
        val dataType = "/json"
        val startIdx = "/1"
        val endIdx = "/10"

        var url = site + keyId + serviceId + dataType + startIdx + endIdx

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                var text = ""

                val body = response.body?.string()

                val parser = JsonParser()
                val rootObj = parser.parse(body.toString())
                    .getAsJsonObject().get("COOKRCP01")
                    .getAsJsonObject().get("row")

                val rootArray = rootObj.getAsJsonArray().get(0)
                val name = rootArray.getAsJsonObject().get("RCP_NM").toString().replace("\"", "")

                text = text + name

                println(name)

                for (i in 1..20){
                    val num = i.toString().padStart(2, '0')

                    val manual = rootArray.getAsJsonObject().get("MANUAL" + num).toString().replace("\"", "")
                    if(!manual.equals("")){
                        text = text + manual
                    }

                    println(manual.toString())
                }

                textView.text = text
            }
            override fun onFailure(call: Call, e: IOException) {
                println("리퀘스트 실패")
            }
        })


    }
}