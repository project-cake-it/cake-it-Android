package com.cakeit.cakitandroid.presentation.mypage.announcement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityAnnouncementListBinding
import com.cakeit.cakitandroid.presentation.mypage.textboard.TextboardActivity
import kotlinx.android.synthetic.main.activity_announcement_list.*
import kotlinx.android.synthetic.main.activity_design_list.*

class AnnouncementListActivity : BaseActivity<ActivityAnnouncementListBinding, AnnouncementListViewModel>(), View.OnClickListener{
    lateinit var announcementListViewModel: AnnouncementListViewModel
    lateinit var announcementListBinding: ActivityAnnouncementListBinding

    lateinit var announcementListAdapter: AnnouncementListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        announcementListBinding = getViewDataBinding()
        announcementListBinding.viewModel = getViewModel()

        // Title init
        tv_announcement_list_title.text = getString(R.string.mypage_announcement)
        btn_announcement_list_back.setOnClickListener(this)

        // Init recycler view
        announcementListAdapter = AnnouncementListAdapter(applicationContext)
        announcementListAdapter.setOnItemClickListener(this)

        rv_announcement_list_items.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@AnnouncementListActivity)
            adapter = announcementListAdapter
        }

        // Observe data via viewModel and link it to recyclerViewAdapter
        announcementListViewModel.announcementListItems.observe(this, Observer { datas ->
            if(datas.size == 0) {
                Log.d("sungmin", "announcementListItems Length 0")
            }
            announcementListAdapter.setAnnouncementListItems(datas)
        })

        announcementListViewModel.getAnnouncementListFromServer()

        announcementListActivity = this

    }

    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.btn_announcement_list_back -> {
                super.onBackPressed()
            }

            else -> {
                var position: Int = rv_announcement_list_items.getChildAdapterPosition(v!!)
                val intent = Intent(this, TextboardActivity::class.java)
                val data = announcementListViewModel.announcementListItems.value?.get(position)
                Log.d("sungmin", "position = " + position + ", textTitle = " + data?.title)
                intent.putExtra("boardTitle", getString(R.string.mypage_announcement))
                intent.putExtra("preload", true)
                intent.putExtra("title", data?.title)
                intent.putExtra("body", data?.body)
                intent.putExtra("date", data?.date)
                startActivity(intent)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_announcement_list
    }

    override fun getViewModel(): AnnouncementListViewModel {
        announcementListViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(
            AnnouncementListViewModel::class.java)
        return announcementListViewModel

    }

    companion object {
        lateinit var announcementListActivity: AnnouncementListActivity
    }
}