package com.muralex.worldnews.presentation.fragments.bookmarks

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.Constants
import com.muralex.worldnews.app.utils.gone
import com.muralex.worldnews.app.utils.visible
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.FragmentBookmarksBinding
import com.muralex.worldnews.presentation.fragments.bookmarks.BookmarksContract.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarksViewModel by viewModels()
    private lateinit var listAdapter: BookmarksListAdapter

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
        setUpObservation()
        processUiEvent(UserAction.LaunchScreen)
    }

    private fun processUiEvent(userAction: UserAction) {
        when (userAction) {
            is UserAction.LaunchScreen -> getNewsOnStart()
            is UserAction.ListItemClicked -> navigateToDetail(userAction.article)
            is UserAction.ListItemSwiped -> setIntent(ViewIntent.RemoveFavorite(userAction.article))
        }
    }

    private fun setIntent(intent: ViewIntent) {
        viewModel.setIntent(intent)
    }

    private fun getNewsOnStart() {
        setIntent(ViewIntent.GetFavoriteNews)
    }

    private fun setUpObservation() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            renderViewState(state)
        }
    }

    private fun setupNewsList() {
        listAdapter = BookmarksListAdapter()
        listAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        listAdapter.setOnItemClickListener { _, item ->
            processUiEvent(UserAction.ListItemClicked(item))
        }

        binding.rvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        val swipeCallBack = BookMarkListSwipe.setup(listAdapter) {
            processUiEvent(UserAction.ListItemSwiped(it))
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
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun navigateToDetail(item: Article) {
        val bundle = bundleOf(Constants.ARTICLE_EXTRA to item)
        findNavController().navigate(R.id.action_bookmarksFragment_to_detailFragment, bundle)
    }

    private fun renderViewState(state: ViewState) {
        when (state) {
            is ViewState.ListLoadFailure -> refreshList(state.data.data)
            is ViewState.ListLoaded -> refreshList(state.data.data)
            ViewState.EmptyList -> {
                binding.rvList.gone()
                binding.emptyBookmarksList.visible()
            }
        }
    }

    private fun refreshList(data: List<Article>?) {
        binding.emptyBookmarksList.gone()
        binding.rvList.visible()
        listAdapter.submitList(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}