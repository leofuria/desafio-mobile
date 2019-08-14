package com.barreto.fulllab.presentation.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.barreto.android.data.remote.RemoteError
import com.barreto.android.domain.base.Event
import com.barreto.android.domain.category.model.CategoryItem
import com.barreto.fulllab.R
import com.barreto.fulllab.presentation.MainActivity
import com.barreto.fulllab.presentation.base.view.PagedListLayout
import com.barreto.fulllab.presentation.category.adapter.CategoryAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoryActivity : AppCompatActivity() {

    private val viewModel by viewModel<CategoryViewModel>()

    private val adapter = CategoryAdapter()
    protected val disposable = CompositeDisposable()

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    private val contentListView by lazy { findViewById<PagedListLayout>(R.id.content_list_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        buildToolbar()

        contentListView?.setOnRefreshListener {
            viewModel.getCategoryList()
        }
        contentListView?.setColorSchemeResources(R.color.colorPrimary)

        contentListView.adapter = this.adapter

        adapter.getNotifyItemClick().subscribe {

            startActivity(
                SubCategoryActivity.buildIntent(
                    this@CategoryActivity,
                    it.second.subCategories ?: emptyList(),
                    it.second.name
                )
            )
        }.addTo(disposable)

        initialize()
    }

    private fun initialize() {
        viewModel.hasNextPage.observe(this, Observer { adapter.loadEnable = (it == true) })
        viewModel.error.observe(this, Observer { showError(it) })
        viewModel.categoryList.observe(this, Observer { onContentsEvent(it) })
    }

    private fun buildToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra(MainActivity.TOOLBAR_TITLE) ?: getString(R.string.app_name)
    }

    override fun onDestroy() {

        disposable.clear()
        super.onDestroy()
    }

    private fun onContentsEvent(event: Event<List<CategoryItem>>?) {

        when (event) {
            is Event.Idle -> viewModel.getCategoryList()
            is Event.Data -> {

                contentListView.isRefreshing = false
                if (event.data.isNullOrEmpty()) {
                    contentListView.showFeedbackStatus(R.drawable.ic_search_black_24dp, R.string.contents_not_found)
                } else {
                    adapter.submitList(event.data)
                }
            }
            is Event.Loading -> contentListView.takeUnless { it.isRefreshing }?.isRefreshing = true
            is Event.Error -> showError(event)
        }
    }

    private fun showError(error: Event.Error?) {

        if (error == null) return

        contentListView.isRefreshing = false
        when (error.error.type) {
            RemoteError.UNKNOW_HOST -> if (adapter.itemCount <= 0) {
                contentListView.showFeedbackStatus(
                    feedbackTitle = R.string.error_internet_title,
                    feedbackMessage = R.string.error_internet_msg,
                    action = {
                        viewModel.getCategoryList()
                    }
                )
            } else {
                adapter.showLoadMoreError()
            }
            else -> contentListView.showFeedbackStatus(feedbackTitle = R.string.generic_error)
        }
        Timber.d(error.error)
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

        fun buildIntent(
            context: Context,
            title: String
        ): Intent {

            return Intent(context, CategoryActivity::class.java)
                .apply {
                    putExtra(MainActivity.TOOLBAR_TITLE, title)
                }
        }
    }
}