package br.gturcheti.netflixremake

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

    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById<ProgressBar>(R.id.progress_main)
        CategoriesTask(this).execute(URL)
    }


    override fun onPreExecute() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onResult(categories: List<Category>) {
        progressBar.visibility = View.GONE
        val adapter = CategoryAdapter(categories)
        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    override fun onFailure(mensagem: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }


}

val KEY: String = "722fa115-a8da-49ea-b138-ce219bd5ee9b"
val URL: String = " https://api.tiagoaguiar.co/netflixapp/home?apiKey=${KEY}"
