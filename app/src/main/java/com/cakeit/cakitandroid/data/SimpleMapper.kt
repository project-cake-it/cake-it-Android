package com.cakeit.cakitandroid.data

import android.util.Log
import com.cakeit.cakitandroid.domain.model.BaseModel

class SimpleMapper<P : BaseModel, Q : BaseModel > {
    val TAG = this.javaClass.simpleName
    lateinit var keys : List<String>
    init {

    }

    var rules : List<Triple<List<String>, List<String>?, Unit?>>? = null
    //        get (){
//            return field
//        }
//        set (value) {
//
//        }

    fun map(from : List<P>, factory : (() -> Q)) : List<Q>{
        var to = arrayListOf<Q>()
        from.forEach{
            rules?.forEach{
                val new = factory()
                val members = it::class.members
                Log.d(TAG, "members : $members")
            }
        }
        return emptyList()
    }
}