package br.gturcheti.netflixremake

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.gturcheti.netflixremake.model.Movie

class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val toolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        val movies = mutableListOf<Movie>()
        val rv: RecyclerView = findViewById(R.id.movie_rv_similar)

        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = MovieAdapter(movies, R.layout.similar_item)

        //busca o layer (shadows)
        val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable
        //busca a imagem a ser inserida no layer
        val movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4)
        //insere no item do layer a imagem buscada
        layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)
        //busca na activity a iv que será inserida o layer (shadow)
        val coverImg: ImageView = findViewById(R.id.movie_img)
        // define o shadow no iv
        coverImg.setImageDrawable(layerDrawable)
    }
}