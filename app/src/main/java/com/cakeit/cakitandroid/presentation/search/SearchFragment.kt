package com.cakeit.cakitandroid.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
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
    var myIsVisibleToUser : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        et_search_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(et_search_input.text.length > 0) btn_search_clear.visibility = View.VISIBLE
                else btn_search_clear.visibility = View.INVISIBLE
            }
        })

        et_search_input.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                keyword = et_search_input.text.toString()
                if(keyword.length == 0) Toast.makeText(context, "키워드를 입력해주세요", Toast.LENGTH_LONG).show()
                else {
                    var intent = Intent(context!!, SearchListActivity::class.java)
                    intent.putExtra("keyword", keyword)
                    startActivity(intent)
                }
                return@OnEditorActionListener true
            }
            false
        })

        et_search_input.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
            } else hideKeyboard()
        })

        btn_search_clear.setOnClickListener {
            et_search_input.setText("")
            showKeyboard()
            btn_search_clear.visibility = View.INVISIBLE
        }

        rl_search_content.setOnClickListener {
            hideKeyboard()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        myIsVisibleToUser = isVisibleToUser

        if (isVisibleToUser && getActivity()!= null) {
            Log.d("songjem", "setUserVisibleHint")
            showKeyboard()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun getViewModel(): SearchViewModel {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        return searchViewModel
    }

    fun showKeyboard() {
        et_search_input.isFocusableInTouchMode()
        et_search_input.setFocusable(true)
        et_search_input.requestFocus()

        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboard() {
        var imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0);
    }
}