package com.barreto.android.domain.content.model

import java.io.Serializable

data class ContentItem(

        var id: String = "",

        var title: String = "",

        var thumbnail: String? = "",

        var value: String = "",

        var desc: String = ""

) : Serializable