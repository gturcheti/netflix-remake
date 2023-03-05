package br.gturcheti.netflixremake

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.gturcheti.netflixremake.model.Category
import br.gturcheti.netflixremake.utils.CategoriesTask

class MainActivity : AppCompatActivity(), CategoriesTask.Callback {

    val KEY: String = "722fa115-a8da-49ea-b138-ce219bd5ee9b"
    val URL: String = " https://api.tiagoaguiar.co/netflixapp/home?apiKey=${KEY}"

    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CategoryAdapter
    private val categories = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_main)

        CategoriesTask(this).execute(URL)

        adapter = CategoryAdapter(categories) { id ->
            val intent = Intent(this@MainActivity, MovieActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }


    override fun onPreExecute() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onResult(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        this.categories.addAll(categories)
        adapter.notifyDataSetChanged()
        progressBar.visibility = View.GONE
    }

    override fun onFailure(mensagem: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }

}
