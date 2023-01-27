package br.gturcheti.netflixremake.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import br.gturcheti.netflixremake.model.Category
import br.gturcheti.netflixremake.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoriesTask (private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())

    interface Callback {
        fun onPreExecute()
        fun onResult(categories: List<Category>)
        fun onFailure(mensagem : String)
    }

    fun execute(url: String) {
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            callback.onPreExecute()

            var urlConnection : HttpsURLConnection? = null
            var buffer : BufferedInputStream? = null
            var stream : InputStream? = null

            try {
                val requestURL = URL(url)
                urlConnection = requestURL.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2_000
                urlConnection.connectTimeout = 2_000

                val statusCode: Int = urlConnection.responseCode
                if (statusCode > 400) throw IOException("Erro na comunicação com o servidor!")

                stream = urlConnection.inputStream
                buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)
                val categories = toCategories(jsonAsString)

                handler.post { callback.onResult(categories) }

            } catch (e: IOException) {
                val message = e.message ?: "Erro desconhecido"
                Log.e("RequestFailure", message, e)
                callback.onFailure(message)

            } finally {
                urlConnection?.disconnect()
                buffer?.close()
                stream?.close()
            }
        }
    }

    private fun toCategories(json: String) : List<Category>{
        val categories = mutableListOf<Category>()
        val jsonRoot = JSONObject(json)
        val jsonCategories = jsonRoot.getJSONArray("category")
        for (i in 0 until jsonCategories.length()) {
            val jsonCategory = jsonCategories.getJSONObject(i)
            val title = jsonCategory.getString("title")
            val jsonMovies = jsonCategory.getJSONArray("movie")

            val movies = mutableListOf<Movie>()
            for (j in 0 until jsonMovies.length()) {
                val jsonMovie = jsonMovies.getJSONObject(j)
                val id = jsonMovie.getString("id")
                val coverUrl = jsonMovie.getString(("cover_url"))
                movies.add(Movie(id, coverUrl))
            }
            categories.add(Category(title,movies))
        }
        return categories.toList()
    }

    private fun toString(stream: InputStream): String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int
        while(true) {
            read = stream.read(bytes)
            if(read <= 0) {
                break
            }
            baos.write(bytes, 0, read)
        }
        return String(baos.toByteArray())
    }

}