package com.barreto.android.data.content.remote.response

import com.barreto.android.data.base.BaseListResponse
import com.barreto.android.domain.content.model.ContentItem
import com.squareup.moshi.Json

class ContentListResponse(

    @Json(name = "Message")
    var message: String? = "",

    @Json(name = "Code")
    var codeResponse: Int? = 0,

    var data: List<ContentItemApi> = emptyList()

) : BaseListResponse<ContentItem>(msg = message, code = codeResponse ?: 0) {

    override fun parseItems(): List<ContentItem> {

        return data.map { it.toContent() }
    }
}