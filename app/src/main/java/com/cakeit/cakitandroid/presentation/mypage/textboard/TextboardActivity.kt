package com.cakeit.cakitandroid.presentation.mypage.textboard

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityMypageTextboardBinding
import com.cakeit.cakitandroid.domain.model.TextboardModel
import kotlinx.android.synthetic.main.activity_mypage_textboard.*

class TextboardActivity : BaseActivity<ActivityMypageTextboardBinding, TextboardViewModel>(), View.OnClickListener{

    lateinit var textboardViewModel: TextboardViewModel
    lateinit var textboardBinding: ActivityMypageTextboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        textboardBinding = getViewDataBinding()
        textboardBinding.viewModel = getViewModel()

        // Title init
        tv_mypage_textboard_title.text = intent.getStringExtra("boardTitle")!!
        btn_mypage_textboard_back.setOnClickListener(this)

        // Textboard item observe
        textboardViewModel.textboardItem.observe(this, Observer { data->
            tv_mypage_textboard_body.text = data.body

            // If the title and date is null, it is a plain textboard (i.e. not announcement)
            data.title?.let{ title ->
                cl_mypage_textboard_header.visibility = View.VISIBLE
                tv_mypage_textboard_header_title.text = title
                tv_mypage_textboard_header_date.text = intent.getStringExtra("date") // Note that the nullity of title and date is paired
            }
        })

        // For preloaded textboards like announcement, pass datas immediately to viewModel via intent for sake of performance
        // This might conflict within MVVM architecture. Modification to use external DBs like ROOM DAO might be needed.
        if(intent.getBooleanExtra("preload", false)){
            textboardViewModel.setTextboardItem(TextboardModel(null, intent.getStringExtra("title"),
                intent.getStringExtra("body")!!, intent.getStringExtra("date")))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_mypage_textboard
    }

    override fun getViewModel(): TextboardViewModel {

        textboardViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(
            TextboardViewModel::class.java)
        return textboardViewModel
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_mypage_textboard_back -> {
                super.onBackPressed()
            }
        }
    }

}