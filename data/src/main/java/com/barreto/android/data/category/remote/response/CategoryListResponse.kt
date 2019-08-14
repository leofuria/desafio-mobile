package com.barreto.android.data.category.remote.response

import com.barreto.android.data.base.BaseListResponse
import com.barreto.android.domain.category.model.CategoryItem
import com.squareup.moshi.Json

class CategoryListResponse(

    @Json(name = "Categories")
    var data: List<CategoryItemApi> = emptyList()

) : BaseListResponse<CategoryItem>() {

    override fun parseItems(): List<CategoryItem> {

        return data.map { it.toCategory() }
    }
}