package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.SocialLoginRepo
import com.cakeit.cakitandroid.domain.model.SocialLoginResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.BaseUseCase
import io.reactivex.Single

object SocialLoginUsecase : BaseUseCase<SocialLoginResponseModel>() {

    override fun buildUseCase(vararg args: Any?): Single<SocialLoginResponseModel> {
        return Single.just(SocialLoginRepo.sendAuthCode(args[0] as String, args[1] as String))
    }
}