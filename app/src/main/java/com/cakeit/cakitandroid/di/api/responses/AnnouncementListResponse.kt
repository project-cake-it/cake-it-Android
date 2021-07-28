package com.cakeit.cakitandroid.di.api.responses

data class AnnouncementListResponseData(
    var id : Long,
    var title : String,
    var body : String,
    var createdAt : String
)

data class AnnouncementListResponse(
    var status : Int,
    var message : String,
    var data : ArrayList<AnnouncementListResponseData>
)

