package br.gturcheti.netflixremake.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import br.gturcheti.netflixremake.model.Movie
import br.gturcheti.netflixremake.model.MovieDetail
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class MovieDetailTask(private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())

    interface Callback {
        fun onPreExecute()
        fun onResult(movieDetail: MovieDetail)
        fun onFailure(mensagem: String)
    }

    fun execute(url: String) {
        val executor = Executors.newSingleThreadExecutor()

        callback.onPreExecute()

        executor.execute {

            var urlConnection: HttpsURLConnection? = null
            var buffer: BufferedInputStream? = null
            var stream: InputStream? = null

            try {
                val requestURL = URL(url)
                urlConnection = requestURL.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2_000
                urlConnection.connectTimeout = 2_000

                val statusCode: Int = urlConnection.responseCode

                if (statusCode == 400) {
                    stream = urlConnection.errorStream
                    buffer = BufferedInputStream(stream)
                    val jsonAsString = toString(buffer)

                    val json = JSONObject(jsonAsString)

                    throw IOException(json.getString("message"))

                } else if (statusCode > 400) throw IOException("Erro na comunicação com o servidor!")

                stream = urlConnection.inputStream

                buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)

                val moveDetail = toMovieDetail(jsonAsString)

                handler.post { callback.onResult(moveDetail) }

            } catch (e: IOException) {
                val message = e.message ?: "Erro desconhecido"
                Log.e("RequestFailure", message, e)
                handler.post { callback.onFailure(message) }

            } finally {
                urlConnection?.disconnect()
                buffer?.close()
                stream?.close()
            }
        }
    }

    private fun toMovieDetail(json: String): MovieDetail {
        val jsonRoot = JSONObject(json)

        val id = jsonRoot.getString("id")
        val title = jsonRoot.getString("title")
        val description = jsonRoot.getString("desc")
        val cast = jsonRoot.getString("cast")
        val coverUrl = jsonRoot.getString("cover_url")
        val jsonSimilars = jsonRoot.getJSONArray("movie")

        val similars = mutableListOf<Movie>()

        for (i in 0 until jsonSimilars.length()) {
            val jsonMovie = jsonSimilars.getJSONObject(i)

            val similarId = jsonMovie.getString("id")
            val similarCoverUrl = jsonMovie.getString("cover_url")

            similars.add(Movie(similarId, similarCoverUrl))
        }

        val movie = Movie(id, coverUrl, title, description, cast)

        return MovieDetail(movie, similars)
    }

    private fun toString(stream: InputStream): String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int
        while (true) {
            read = stream.read(bytes)
            if (read <= 0) {
                break
            }
            baos.write(bytes, 0, read)
        }
        return String(baos.toByteArray())
    }

}