package com.muralex.worldnews.presentation.fragments.home

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.muralex.worldnews.app.utils.Constants
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.utils.TestApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ArticlesListAdapterTest {

    private lateinit var context: Context
    private val SUT = ArticlesListAdapter()
    private lateinit var frameLayout: FrameLayout
    private var testItemClickListener: ((Constants.Action, Article) -> Unit)? = mockk()

    @Before
    fun setup() {
        val application: Application = ApplicationProvider.getApplicationContext()
        context = application
        frameLayout = FrameLayout(context)
        every { testItemClickListener?.invoke(Constants.Action.Click , testArticle) } returns Unit
    }

    @Test
    fun onCreateViewHolder_returnViewHolder() {
        val viewHolder = SUT.onCreateViewHolder(frameLayout,1)
        assertThat(viewHolder).isInstanceOf(ArticlesListAdapter.ViewHolder::class.java)
    }

    @Test
    fun bindViewHolder_defaultList_InitialState() {
        val holder =  ArticlesListAdapter.ViewHolder.from(frameLayout)
        holder.bind( testArticle , testItemClickListener)
        assertThat(holder.binding.tvTitle.visibility).isEqualTo(View.VISIBLE)
        assertThat(holder.binding.tvPublished.visibility).isEqualTo(View.VISIBLE)
        assertThat(holder.binding.tvPublishedInfo.visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun bindViewHolder_bindData_dataInsertedCorrectly() {
        val holder =  ArticlesListAdapter.ViewHolder.from(frameLayout)
        holder.bind( testArticle , testItemClickListener)
        assertThat(holder.binding.tvTitle.text).isEqualTo(testArticle.title)
        assertThat(holder.binding.tvPublished.text).isEqualTo(testArticle.publishedAt)
        assertThat(holder.binding.tvPublishedInfo.text).isEqualTo(testArticle.source)
    }

    @Test
    fun onclick_invoke_testItemClickListener() {
        val holder =  ArticlesListAdapter.ViewHolder.from(frameLayout)
        holder.bind( testArticle , testItemClickListener)
        holder.binding.cardWrap.performClick()
        verify { testItemClickListener?.invoke(Constants.Action.Click, any()) }
    }

    companion object {
        private val testArticle: Article = Article(title = "title", description = "",
            text = "", url = "", image = "", source = "", publishedAt = "published", publishedTime = 0)
    }



}