package com.cakeit.cakitandroid.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel<T>(application: Application) : AndroidViewModel(application) {
    val TAG = this.javaClass.simpleName
}