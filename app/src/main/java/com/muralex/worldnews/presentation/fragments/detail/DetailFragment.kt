package com.muralex.worldnews.presentation.fragments.detail

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.*
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.FragmentDetailBinding
import com.muralex.worldnews.presentation.fragments.detail.DetailContract.UserAction
import com.muralex.worldnews.presentation.fragments.home.HomeContract
import com.muralex.worldnews.presentation.utils.ContactActions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    private var title = ""
    @Inject
    lateinit var contactActions: ContactActions
    @Inject
    lateinit var networkHelper: NetworkHelper
    @Inject
    lateinit var settingsHelper: SettingsHelper
    private var starMenuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.selectedArticle
        title = article.source
        setToolbarTitle()
        setMenu(article)
        setupUI(article)
    }

    private fun setupObserver() {
        viewModel.favorite.observe(viewLifecycleOwner) { isFavorite ->
            starMenuItem?.let {
                if (isFavorite) {
                    it.setIcon(R.drawable.ic_toolbar_bookmark)
                } else {
                    it.setIcon(R.drawable.ic_toolbar_bookmark_border)
                }
            }
        }
    }

    private fun setupUI(article: Article) {
        val time = article.publishedTime.formatToDate()
        val articleUI = article.copy( publishedAt = time)

        binding.article = articleUI
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        with(binding) {
            button.displayIf(networkHelper.isNetworkConnected())
            button.setOnClickListener {
                processUiEvent(UserAction.OpenSourceButtonClicked(article))
            }
        }
    }

    private fun processUiEvent(userAction: UserAction) {
        when (userAction) {
            is UserAction.LaunchScreen -> viewModel.checkFavorite(userAction.article)
            is UserAction.BookmarkClicked -> changeFavorite(userAction.article)
            is UserAction.ShareMenuMenuItemSelected -> {
                contactActions.shareURL(userAction.article.url)
            }
            is UserAction.OpenSourceButtonClicked -> openWebPage(userAction.article)
            is UserAction.OpenSourceMenuItemSelected -> openWebPage(userAction.article)
        }
    }

    private fun setToolbarTitle() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle()
    }

    private fun openWebPage(article: Article) {
        val bundle = bundleOf(Constants.ARTICLE_EXTRA to article)
        findNavController().popBackStack()
        findNavController().navigate(R.id.webDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setMenu(article: Article) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu)
                starMenuItem = menu.findItem(R.id.action_favorite)
                setupObserver()
                processUiEvent(UserAction.LaunchScreen(article))
                val sourceItem = menu.findItem(R.id.action_source)
                sourceItem.isVisible = networkHelper.isNetworkConnected()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        processUiEvent(UserAction.ShareMenuMenuItemSelected(article))
                        true
                    }
                    R.id.action_favorite -> {
                        processUiEvent(UserAction.BookmarkClicked(article))
                        true
                    }
                    R.id.action_source -> {
                        processUiEvent(UserAction.OpenSourceMenuItemSelected(article))
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner )
    }

    private fun changeFavorite(article: Article) {
        viewModel.favorite.value?.let {  isFavorite ->
            if (isFavorite) viewModel.removeFromFavorite(article)
            else viewModel.addToFavorite(article)
        }
    }
}