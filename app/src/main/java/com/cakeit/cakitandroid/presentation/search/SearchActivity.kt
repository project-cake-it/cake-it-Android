package com.cakeit.cakitandroid.presentation.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.presentation.search.searchlist.SearchListActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search_list.*

class SearchActivity : AppCompatActivity() {
    lateinit var keyword : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        btn_search_search.setOnClickListener {
            keyword = et_search_input.text.toString()
            Log.d("songjem", "keyword = " + keyword)
            var intent = Intent(this, SearchListActivity::class.java)
            intent.putExtra("keyword", keyword)
            startActivity(intent)
        }
    }
}