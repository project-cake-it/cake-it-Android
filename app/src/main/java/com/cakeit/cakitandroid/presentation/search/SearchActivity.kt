package com.cakeit.cakitandroid.presentation.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.presentation.search.searchlist.SearchListActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        btn_search_search.setOnClickListener {
            var intent = Intent(this, SearchListActivity::class.java)
            startActivity(intent)
        }
    }
}