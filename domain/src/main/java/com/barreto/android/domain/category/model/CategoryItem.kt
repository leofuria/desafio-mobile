package com.barreto.android.domain.category.model

import java.io.Serializable

data class CategoryItem(

        var id: String = "",

        var name: String = "",

        var image: String? = "",

        var subCategories: List<CategoryItem>? = emptyList()

) : Serializable