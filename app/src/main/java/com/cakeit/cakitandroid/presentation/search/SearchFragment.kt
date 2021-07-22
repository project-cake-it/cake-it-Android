package com.cakeit.cakitandroid.presentation.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentSearchBinding
import com.cakeit.cakitandroid.presentation.search.searchlist.SearchListActivity
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    lateinit var keyword : String
    lateinit var binding : FragmentSearchBinding
    lateinit var searchViewModel : SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        et_search_input.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyword = et_search_input.text.toString()
                Log.d("songjem", "keyword = " + keyword)
                var intent = Intent(context!!, SearchListActivity::class.java)
                intent.putExtra("keyword", keyword)
                startActivity(intent)
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun getViewModel(): SearchViewModel {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        return searchViewModel
    }
}