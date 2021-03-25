package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.SocialLoginRepo
import com.cakeit.cakitandroid.domain.model.SocialLoginResponseModel

object SocialLoginUsecase : BaseUsecase<SocialLoginResponseModel>() {

    override suspend fun job(vararg args: Any?): SocialLoginResponseModel {
        Log.d(TAG, "2 :: ")
        return SocialLoginRepo.sendAuthCode(args[0] as String, args[1] as String)
    }
}