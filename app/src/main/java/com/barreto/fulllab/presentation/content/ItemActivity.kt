package com.barreto.fulllab.presentation.content

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.barreto.android.domain.base.Event
import com.barreto.android.domain.content.model.ContentItem
import com.barreto.fulllab.R
import com.barreto.fulllab.presentation.MainActivity
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class ItemActivity : AppCompatActivity() {

    private var codeId = ""

    private val viewModel by viewModel<ContentViewModel>()

    protected val disposable = CompositeDisposable()

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val titleText by lazy { findViewById<TextView>(R.id.titleText) }
    private val valueText by lazy { findViewById<TextView>(R.id.valueText) }
    private val descText by lazy { findViewById<TextView>(R.id.descText) }
    private val thumbnail by lazy { findViewById<ImageView>(R.id.thumbnail) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        buildToolbar()

        initialize()
    }

    private fun initialize() {
        viewModel.content.observe(this, Observer { onContentEvent(it) })
    }

    private fun buildToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra(MainActivity.TOOLBAR_TITLE) ?: getString(R.string.app_name)
        codeId = intent.getStringExtra(CODE_ID) ?: ""
    }

    override fun onDestroy() {

        disposable.clear()
        super.onDestroy()
    }

    private fun onContentEvent(event: Event<ContentItem>?) {

        when (event) {
//            is Event.Idle -> viewModel.getContent(codeId)
            is Event.Data -> {
                titleText.text = event.data.title
                valueText.text = event.data.value
                descText.text = event.data.desc

                Picasso.with(this)
                    .load(event.data.thumbnail)
                    .noFade()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .fit()
                    .tag("placholder")
                    .into(thumbnail)
            }
            is Event.Error -> showError(event)
        }
    }

    private fun showError(error: Event.Error?) {

        if (error == null) return

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
        const val CODE_ID = "codeId"

        fun buildIntent(
            context: Context,
            codeId: String,
            title: String
        ): Intent {

            return Intent(context, ItemActivity::class.java)
                .apply {
                    putExtra(CODE_ID, codeId)
                    putExtra(MainActivity.TOOLBAR_TITLE, title)
                }
        }
    }
}