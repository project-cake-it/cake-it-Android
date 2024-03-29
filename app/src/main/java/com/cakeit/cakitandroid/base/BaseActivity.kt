package com.cakeit.cakitandroid.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity() {
    val TAG = this.javaClass.simpleName
    private lateinit var viewDataBinding: T

    @LayoutRes
    abstract fun getLayoutId(): Int
    abstract fun getViewModel(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
    }

    protected fun dataBinding() {
        viewDataBinding = DataBindingUtil.setContentView<T>(this, getLayoutId())
        viewDataBinding!!.lifecycleOwner = this
    }

    fun getViewDataBinding(): T {
        return viewDataBinding
    }

    protected fun showToast(msg: String, long : Boolean = false) {
        Toast.makeText(this, msg, if(long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }
}
