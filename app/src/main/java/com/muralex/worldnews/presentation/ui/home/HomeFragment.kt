package com.muralex.worldnews.presentation.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muralex.worldnews.R
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.FragmentHomeBinding
import com.muralex.worldnews.presentation.ui.home.HomeViewModel.ViewState
import com.muralex.worldnews.presentation.utils.Constants.ARTICLE_URL
import com.muralex.worldnews.presentation.utils.SettingsHelper
import com.muralex.worldnews.presentation.utils.dataBindings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by dataBindings(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var listAdapter: ArticlesListAdapter

    @Inject
    lateinit var settingsHelper: SettingsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root

        listAdapter = ArticlesListAdapter()
        listAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        listAdapter.setOnItemClickListener { action, item ->
            setClickEvents(item)
        }

        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = listAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
           val country = settingsHelper.getSelectedCountry()
            viewModel.setNewsCountry(country)
            viewModel.updateNews()
        }

        val country = settingsHelper.getSelectedCountry()
        viewModel.getNews(country )
        setUpObservation()
    }

    private fun setClickEvents(item: Article) {
        val bundle = bundleOf(  ARTICLE_URL to item.url,
            "selected_article" to item
        )
        findNavController().navigate(R.id.action_nav_home_to_detailFragment, bundle)
    }

    private fun setUpObservation() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Loading -> showProgressBar()
                is ViewState.ListLoadFailure -> {
                    hideProgressBar()
                    Toast.makeText(requireActivity(),"An error occurred : ${state.errorMessage}", Toast.LENGTH_LONG).show()
                }
                is ViewState.ListLoaded -> {
                    hideProgressBar()
                    listAdapter.submitList( state.data.data  )

                }
                ViewState.Initial -> TODO()
            }
        }
    }


    private fun showProgressBar(){
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun hideProgressBar(){
        binding.swipeRefreshLayout.isRefreshing = false
    }
}