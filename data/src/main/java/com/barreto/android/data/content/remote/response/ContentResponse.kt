package com.barreto.android.data.content.remote.response

import com.barreto.android.data.category.remote.response.CategoryItemApi

data class ContentResponse(

    var data: CategoryItemApi? = null
) {

    fun parseItem(): CategoryItemApi {

        return data ?: CategoryItemApi()
    }
}