package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.SocialLoginRepo
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.domain.model.SocialLoginResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object SocialLoginUseCase : SingleUseCase<SocialLoginResponseModel>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<SocialLoginResponseModel>? {
        var req = baseRequest as Request

        return SocialLoginRepo.sendAuthCode(req.authCode, req.socialType)
            ?.map {
                Log.d("songjem", "accessToken Login = " + it.data.accessToken)

                SocialLoginResponseModel(it.message, it.data.accessToken)
            }
    }

    data class Request(
        val authCode : String,
        val socialType : String
    ) : BaseRequest()

    override fun buildUseCase(): Single<SocialLoginResponseModel>? {
        return null
    }
}