package com.muralex.worldnews.presentation.fragments.bookmarks

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.Constants
import com.muralex.worldnews.app.utils.Constants.ARTICLE_URL
import com.muralex.worldnews.app.utils.SettingsHelper
import com.muralex.worldnews.app.utils.gone
import com.muralex.worldnews.app.utils.visible
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.FragmentBookmarksBinding
import com.muralex.worldnews.presentation.utils.ContactActions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarksViewModel by viewModels()
    private lateinit var listAdapter: BookmarksListAdapter
    @Inject
    lateinit var contactActions: ContactActions

    @Inject
    lateinit var settingsHelper: SettingsHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMenu()
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

    private fun setMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                menu.clear()
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_detail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        contactActions.shareApp()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner )
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
                    displayErrorNotification()
                    refreshList(state.data.data)
                }
                is BookmarksViewModel.ViewState.ListLoaded -> {
                    refreshList(state.data.data)
                }
                BookmarksViewModel.ViewState.EmptyList -> {
                    hideProgressBar()
                    binding.rvList.gone()
                    binding.emptyBookmarksList.visible()
                }
            }
        }
    }

    private fun displayErrorNotification() {
        val text: String = getString(R.string.error_msg_generic_error)
        val notification = Snackbar.make( binding.root,text,Snackbar.LENGTH_LONG)

        notification.setAction(getString(R.string.snackbar_close)) {
            notification.dismiss()
        }.show()
    }

    private fun refreshList(data: List<Article>?) {
        binding.emptyBookmarksList.gone()
        hideProgressBar()
        binding.rvList.visible()
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