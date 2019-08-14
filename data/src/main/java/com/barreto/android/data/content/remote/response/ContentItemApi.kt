package com.barreto.android.data.content.remote.response

import android.net.Uri
import com.barreto.android.domain.content.model.ContentItem
import com.squareup.moshi.Json

class ContentItemApi(

    var id: String = "",

    var title: String = "",

    @Json(name = "image")
    var thumbnail: String? = "",

    var value: String = "",

    var desc: String = ""

) {

    fun toContent(): ContentItem {

        return ContentItem(
            id,
            title,
            Uri.encode(thumbnail, "@#&=*+-_.,:!?()/~'%"),
            value,
            desc
        )
    }
}