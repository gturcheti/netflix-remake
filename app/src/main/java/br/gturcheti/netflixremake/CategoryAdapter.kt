package br.gturcheti.netflixremake

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.gturcheti.netflixremake.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClickListener: (String) -> Unit,
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            val tv: TextView = itemView.findViewById(R.id.tv_category)
            tv.text = category.label
            val rv: RecyclerView = itemView.findViewById(R.id.rv_category)
            rv.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            rv.adapter = MovieAdapter(category.movies, R.layout.movie_item, onItemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)

    }

    override fun getItemCount(): Int = categories.size

}