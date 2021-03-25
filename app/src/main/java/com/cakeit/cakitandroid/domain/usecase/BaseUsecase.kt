package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import io.reactivex.Single
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseUsecase<T> {
    val TAG = this.javaClass.simpleName

    //job 밖의, on~~에 들어가는 로직들은 모두 viewModel에서 해결할 로직들입니다!
    //Usecase 와 관련된 로직들은 되도록 job 안에서 처리해주세요
    //추후에 usecase단의 callback을 넣을지는 생각해봅시다
    fun run(
            onSuccess : ((t: T) -> Unit),
            onFailure : ((t: Exception) -> Unit),
            onFinished : (() -> Unit),
            vararg args : Any?
    ) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val result = job(*args)
            withContext(Dispatchers.Main){
                onSuccess(result)
            }
        } catch (e : Exception) {
            withContext(Dispatchers.Main){
                onFailure(e)
            }
        } finally {
            withContext(Dispatchers.Main){
                onFinished()
            }
        }

    }

    @Throws
    internal abstract suspend fun job(vararg args : Any?) : T
}