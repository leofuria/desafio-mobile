package com.barreto.android.data.category.remote.response

import android.net.Uri
import com.barreto.android.domain.category.model.CategoryItem
import com.squareup.moshi.Json

class CategoryItemApi(

    @Json(name = "Id")
    var id: String = "",

    @Json(name = "Name")
    var name: String = "",

    @Json(name = "Image")
    var image: String? = "",

    @Json(name = "SubCategories")
    var subCategories: List<CategoryItemApi>? = emptyList()

) {

    fun toCategory(): CategoryItem {

        return CategoryItem(
            id,
            name,
            Uri.encode(image, "@#&=*+-_.,:!?()/~'%"),
            subCategories?.map { it.toCategory() }
        )
    }
}