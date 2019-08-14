package com.barreto.fulllab.presentation.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.barreto.android.domain.category.model.CategoryItem
import com.barreto.fulllab.R
import com.barreto.fulllab.presentation.MainActivity
import com.barreto.fulllab.presentation.base.view.PagedListLayout
import com.barreto.fulllab.presentation.category.adapter.CategoryAdapter
import java.io.Serializable

class SubCategoryActivity : AppCompatActivity() {

    private val adapter = CategoryAdapter()
    private var subCategoryList: List<CategoryItem> = emptyList()

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    private val contentListView by lazy { findViewById<PagedListLayout>(R.id.content_list_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        buildToolbar()

        contentListView?.setColorSchemeResources(R.color.colorPrimary)
        adapter.loadEnable = false
        contentListView.adapter = this.adapter

        if (subCategoryList.isNullOrEmpty()) {
            contentListView.showFeedbackStatus(R.drawable.ic_search_black_24dp, R.string.contents_not_found)
        } else {
            adapter.submitList(subCategoryList)
        }
    }

    private fun buildToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra(MainActivity.TOOLBAR_TITLE) ?: getString(R.string.app_name)
        subCategoryList = intent.getSerializableExtra(SUB_CATEGORY) as List<CategoryItem>
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val SUB_CATEGORY = "subCategory"

        fun buildIntent(
            context: Context,
            subcategory: List<CategoryItem>,
            title: String
        ): Intent {

            return Intent(context, SubCategoryActivity::class.java)
                .apply {
                    putExtra(SUB_CATEGORY, subcategory as Serializable)
                    putExtra(MainActivity.TOOLBAR_TITLE, title)
                }
        }
    }
}