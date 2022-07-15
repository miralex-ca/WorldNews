package com.muralex.worldnews.presentation.ui.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muralex.worldnews.R
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.FragmentHomeBinding
import com.muralex.worldnews.presentation.utils.Constants
import com.muralex.worldnews.presentation.utils.Constants.ARTICLE_URL
import com.muralex.worldnews.presentation.utils.SettingsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookmarksFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarksViewModel by viewModels()
    private lateinit var listAdapter: BookmarksListAdapter

    @Inject
    lateinit var settingsHelper: SettingsHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNewsList()
        setupSwipeRefresh()
        getNewsOnStart()
        setUpObservation()
    }

    private fun getNewsOnStart() {
        viewModel.getNews()
    }

    private fun setupNewsList() {
        listAdapter = BookmarksListAdapter()
        listAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        listAdapter.setOnItemClickListener { _, item ->
            setClickEvents(item)
        }

        binding.rvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        val swipeCallBack =  BookMarkListSwipe.setup(listAdapter) {
            viewModel.removeFavorite(it)
        }

        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.rvList)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.apply {
            isEnabled = false
            setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.secondaryDarkColor)
            )

            setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(requireContext(), R.color.colorSwipeBackground)
            )
        }
    }

    private fun setClickEvents(item: Article) {
        val bundle = bundleOf(ARTICLE_URL to item.url,
            Constants.ARTICLE_EXTRA to item
        )
        findNavController().navigate(R.id.action_bookmarksFragment_to_detailFragment, bundle)
    }

    private fun setUpObservation() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BookmarksViewModel.ViewState.Loading -> showProgressBar()
                is BookmarksViewModel.ViewState.ListLoadFailure -> {
                    hideProgressBar()
                    refreshList(state.data.data)
                }
                is BookmarksViewModel.ViewState.ListLoaded -> {
                    hideProgressBar()
                    refreshList(state.data.data)
                }
            }
        }
    }

    private fun refreshList(data: List<Article>?) {
        listAdapter.submitList(data)
    }

    private fun showProgressBar() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun hideProgressBar() {
        lifecycleScope.launch {
            _binding?.swipeRefreshLayout?.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }






}