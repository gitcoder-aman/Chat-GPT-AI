package com.tech.chatgptboatai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tech.chatgptboatai.adapter.ChatAdapter
import com.tech.chatgptboatai.databinding.ActivityImageBinding
import com.tech.chatgptboatai.model.MessageModel
import com.tech.chatgptboatai.utils.Utils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ImageActivity : AppCompatActivity() {

    private lateinit var binding:ActivityImageBinding
    var list = ArrayList<MessageModel>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var adapter: ChatAdapter

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

    var client: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mLayoutManager = LinearLayoutManager(this)
        binding.chatRecycler.layoutManager = mLayoutManager
        mLayoutManager.stackFromEnd = true
        adapter = ChatAdapter(list, this)
        binding.chatRecycler.adapter = adapter

        binding.sendBtn.setOnClickListener {
            if (binding.userText.text!!.isEmpty()) {
                Toast.makeText(this, "Please ask your question?", Toast.LENGTH_SHORT).show()
            } else {
                addToChat(binding.userText.text.toString(), isUser = true, isImage = false)
                callApi(binding.userText.text.toString())
                binding.userText.text.clear()
            }
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun addToChat(message:String,isUser:Boolean,isImage:Boolean){
        runOnUiThread(Runnable {

            list.add(
                MessageModel(
                    isUser = isUser,
                    isImage = isImage,
                    message = message
                )
            )
            adapter.notifyItemInserted(list.size - 1)
            adapter.notifyDataSetChanged()
            binding.chatRecycler.recycledViewPool.clear()
            binding.chatRecycler.smoothScrollToPosition(adapter.itemCount) // last message show in recycler view
        })
    }

    fun messageResponse(result: String, isUser: Boolean, isImage: Boolean) {

        list.removeAt(list.size-1)  //Typing message remove
        addToChat(result, isUser = isUser, isImage = isImage)
    }

    private fun callApi(question: String) {
        addToChat("Generate Your Image...",isUser=false, isImage = false)

        val jsonBody: JSONObject = JSONObject()

        try {
            jsonBody.put("prompt",question)
            jsonBody.put("n",2)
            jsonBody.put("size","1024x1024")
        }catch (e: JSONException){
            e.printStackTrace()
            Log.d("@@@@",e.message.toString())

        }
        val requestBody: RequestBody = RequestBody.create(JSON,jsonBody.toString())

        val request: Request = Request.Builder()
            .url("https://api.openai.com/v1/images/generations")
            .header("Authorization","Bearer "+ Utils.getApi())
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("@@@@","OnFailure")
                messageResponse("I apologize, but $question appears to be an incomplete or unclear input. Can you please provide more information or clarify your request so I can better assist you? Thank you!",
                    isUser = false,
                    isImage = false
                )
            }

            override fun onResponse(call: Call, response: Response) {

                if(response.isSuccessful){

                    var jsonObject : JSONObject?= null
                    try{
                        jsonObject = JSONObject(response.body?.string()!!)
                        val jsonArray: JSONArray = jsonObject.getJSONArray("data")
                        val urlResult:String = jsonArray.getJSONObject(0).getString("url")
                        Log.d("@@@@", "result $urlResult")

                        messageResponse(urlResult.trim(), isUser = false, isImage = true)

                    }catch (e: JSONException){
                        e.printStackTrace()
                        Log.d("@@@@","JSON Exception "+e.message)
                    }


                }else{
                    Log.d("@@@@","Not Successful")
                    messageResponse("Response Not successful ",
                        isUser = false,
                        isImage = false
                    )
                }
            }

        })

    }


}