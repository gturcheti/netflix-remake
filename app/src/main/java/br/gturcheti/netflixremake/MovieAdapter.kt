package br.gturcheti.netflixremake

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import br.gturcheti.netflixremake.model.Movie

class MovieAdapter(
    private val movies: List<Movie>,
    @LayoutRes private val layout: Int
    ): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind (movie: Movie) {
//            val iv: ImageView = itemView.findViewById(R.id.iv_movie)
//            iv.setImageResource(movie.coverUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

    }

    override fun getItemCount(): Int = movies.size

}