# retrofitUtils

[![Jitpack](https://jitpack.io/v/wereDevelopers/retrofitUtils.svg)](https://jitpack.io/#wereDevelopers/retrofitUtils)
[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/wereDevelopers/retrofitUtils/blob/main/LICENSE)

Utility function for retrofit

## How to implement:

add in the Gradle

```groovy
dependencies {
    implementation('com.github.wereDevelopers:retrofitUtils:{LastTag}')
}
```


## How to use



### Activity:
```
import com.weredev.binding_ui.viewBinding
import com.weredev.bindingui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	....

	override fun onCreate(savedInstanceState: Bundle?) {
	    ...
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
	...
}
```
