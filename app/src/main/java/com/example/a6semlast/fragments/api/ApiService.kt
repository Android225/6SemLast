import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ApiService {

    companion object {
        private const val BASE_URL = "https://svatky.adresa.info"
    }

    private val client = OkHttpClient()

    fun searchNameByDate(date: String, lang: String, callback: ApiCallback) {
        val url = "$BASE_URL/json?date=$date&lang=$lang"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback.onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                val responseData = response.body()?.string()
                if (response.isSuccessful && responseData != null) {
                    callback.onSuccess(responseData)
                } else {
                    callback.onError("Failed to get data")
                }
            }
        })
    }

    interface ApiCallback {
        fun onSuccess(response: String)
        fun onError(error: String)
    }
}
