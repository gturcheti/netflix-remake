package br.gturcheti.netflixremake

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.gturcheti.netflixremake.model.Movie
import br.gturcheti.netflixremake.model.MovieDetail
import br.gturcheti.netflixremake.utils.DownloadImageTask
import br.gturcheti.netflixremake.utils.MovieDetailTask

class MovieActivity : AppCompatActivity(), MovieDetailTask.Callback {

    private val KEY: String = "722fa115-a8da-49ea-b138-ce219bd5ee9b"

    private lateinit var title: TextView
    private lateinit var cast: TextView
    private lateinit var description: TextView
    private lateinit var coverImg: ImageView
    private lateinit var rv: RecyclerView
    private lateinit var loadingFrame: FrameLayout
    private lateinit var errorFrame: FrameLayout
    private lateinit var errorMessage: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var adapter: MovieAdapter

    private val similars = mutableListOf<Movie>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        bindViews()
        bindToolbar()
        bindRecyclerView()

        val id = intent?.getStringExtra("id")
            ?: throw java.lang.IllegalStateException("ID não foi encontrado!")
        val url = "https://api.tiagoaguiar.co/netflixapp/movie/${id}?apiKey=${KEY}"
        MovieDetailTask(this).execute(url)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPreExecute() {
        loadingFrame.visibility = View.VISIBLE
    }

    override fun onResult(movieDetail: MovieDetail) {
        loadingFrame.visibility = View.GONE
        this.similars.clear()
        this.similars.addAll(movieDetail.similars)
        adapter.notifyDataSetChanged()
        title.text = movieDetail.movie.title
        cast.text = movieDetail.movie.cast
        description.text = movieDetail.movie.description
        downloadImage(movieDetail.movie.coverUrl)
    }

    override fun onFailure(mensagem: String) {
        loadingFrame.visibility = View.GONE
        errorFrame.visibility = View.VISIBLE
    }

    private fun downloadImage(coverUrl: String) {
        DownloadImageTask(object : DownloadImageTask.Callback {
            override fun onResult(bitmap: Bitmap) {
                val layerDrawable: LayerDrawable = ContextCompat.getDrawable(
                    this@MovieActivity,
                    R.drawable.shadows
                ) as LayerDrawable
                val movieCover = BitmapDrawable(resources, bitmap)
                layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)
                coverImg.setImageDrawable(layerDrawable)
            }
        }).execute(coverUrl)
    }

    private fun bindViews() {
        title = findViewById(R.id.movie_tv_title)
        cast = findViewById(R.id.movie_tv_cast)
        description = findViewById(R.id.movie_tv_description)
        coverImg = findViewById(R.id.movie_img)
        loadingFrame = findViewById(R.id.progress_movie)
        errorFrame = findViewById(R.id.movie_error)
        errorMessage = findViewById(R.id.frame_error_message)
    }

    private fun bindRecyclerView() {
        rv = findViewById(R.id.movie_rv_similar)
        adapter = MovieAdapter(similars, R.layout.movie_item_similar)
        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = adapter
    }

    private fun bindToolbar() {
        toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }
}