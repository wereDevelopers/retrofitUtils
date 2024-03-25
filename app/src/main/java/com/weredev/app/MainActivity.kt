package com.weredev.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.weredev.app.databinding.ActivityMainBinding
import com.weredev.binding_ui.viewBinding
import com.weredev.retrofitUtils.RetrofitUtils
import com.weredev.retrofitUtils.RetrofitUtils.checkAndParseResponse
import com.weredev.retrofitUtils.exception.NetworkOperationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * This example demonstrates the use of retrofitUtils to acquire view binding within an activity.
 */
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnFakeFloating.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val response = RetrofitUtils.execute(createService().getJson("https://jsonplaceholder.typicode.com/todos/1"))

                val responseBody = try {
                    response.checkAndParseResponse().string()
                } catch (e: NetworkOperationException){
                    e.message
                }

                CoroutineScope(Dispatchers.Main).launch {
                    binding.txtExample.text = responseBody
                }
            }
        }
    }

    private fun createService(): GenericService {
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(GenericService::class.java)
    }
}