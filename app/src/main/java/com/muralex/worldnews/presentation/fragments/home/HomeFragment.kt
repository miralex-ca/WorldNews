package com.muralex.worldnews.presentation.fragments.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.*
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.FragmentHomeBinding
import com.muralex.worldnews.presentation.dialogs.CountryDialogFragment
import com.muralex.worldnews.presentation.fragments.home.HomeContract.UserAction
import com.muralex.worldnews.presentation.fragments.home.HomeContract.ViewEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var listAdapter: ArticlesListAdapter

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
        setupMenu()
        setupNewsList()
        setupSwipeRefresh()
        setUpObservation()
        processUiEvent(UserAction.LaunchScreen)
    }

    private fun processUiEvent(userAction: UserAction) {
        when (userAction) {
            is UserAction.LaunchScreen -> getNewsOnStart()
            is UserAction.CountrySelectedFromDialog -> updateNewsData()
            is UserAction.ListItemClick -> navigateToDetail(userAction.article)
            is UserAction.OpenCountryDialogFromMenu -> showCountryDialog()
            is UserAction.RefreshFromMenu -> updateNewsData()
            is UserAction.SwipeRefreshPulled -> updateNewsData()
        }
    }

    private fun getNewsOnStart() {
        val refreshOnStart = settingsHelper.isStartRefreshEnabled() && viewModel.startRefresh
        val country = settingsHelper.getSelectedCountry()
        if (refreshOnStart) setIntent(HomeContract.ViewIntent.UpdateNews)
        else setIntent(HomeContract.ViewIntent.GetNews(country))
    }

    private fun setIntent(intent: HomeContract.ViewIntent) {
        viewModel.setIntent(intent)
    }

    private fun setUpObservation() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            renderViewState(state)
        }

        viewModel.viewEffect.onEach {
            renderViewEffect(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun renderViewState(state: HomeContract.ViewState) {
        when (state) {
            is HomeContract.ViewState.Loading -> showProgressBar()
            is HomeContract.ViewState.EmptyList -> {
                hideProgressBar(true)
            }
            is HomeContract.ViewState.ListLoadFailure -> {
                hideProgressBar(true)
                setToolbarTitle()
                refreshList(state.data.data)
            }

            is HomeContract.ViewState.ListLoaded -> {
                hideProgressBar(false)
                notEmptyUI()
                setToolbarTitle()
                refreshList(state.data.data)
            }

            is HomeContract.ViewState.ListRefreshed -> {
                hideProgressBar(true)
                notEmptyUI()
                saveUpdatedCountry()
                setToolbarTitle()
                refreshList(state.data.data)
            }
        }
    }

    private fun renderViewEffect(effect: ViewEffect) {
        when (effect) {
            is ViewEffect.ShowErrorSnackBar -> displayErrorNotification(effect.message)
            is ViewEffect.ShowLoading -> showProgressBar()
        }
    }

    private fun setupNewsList() {
        listAdapter = ArticlesListAdapter()

        listAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        listAdapter.setOnItemClickListener { _, item ->
            processUiEvent(UserAction.ListItemClick(item))
        }

        binding.rvList.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = listAdapter
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_home, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_refresh -> {
                        processUiEvent(UserAction.RefreshFromMenu)
                        true
                    }
                    R.id.action_select -> {
                        processUiEvent(UserAction.OpenCountryDialogFromMenu)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun showCountryDialog() {
        val dialog = CountryDialogFragment()
        val selectedCountryIndex = settingsHelper.getSelectedCountryIndex()
        dialog.arguments = bundleOf(CountryDialogFragment.SELECTED to selectedCountryIndex)
        dialog.show(parentFragmentManager, CountryDialogFragment.TAG)
        parentFragmentManager.setFragmentResultListener(CountryDialogFragment.REQUEST_KEY,
            viewLifecycleOwner) { key, bundle ->
            if (key == CountryDialogFragment.REQUEST_KEY) {
                val selectedIndex = bundle[CountryDialogFragment.CHECKED]
                val selected =
                    resources.getStringArray(R.array.countries_options_values)[selectedIndex as Int]
                settingsHelper.setSelectedCountry(selected)
                processUiEvent(UserAction.CountrySelectedFromDialog)
            }
        }
    }

    private fun updateNewsData() {
        val country = settingsHelper.getSelectedCountry()
        viewModel.setNewsCountry(country)
        setIntent(HomeContract.ViewIntent.UpdateNews)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.apply {

            isEnabled = settingsHelper.isSwipeDownEnabled()

            setOnRefreshListener { processUiEvent(UserAction.SwipeRefreshPulled) }

            setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.secondaryDarkColor)
            )

            setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(requireContext(), R.color.colorSwipeBackground)
            )
        }
    }

    private fun showProgressBar() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun navigateToDetail(item: Article) {
        val bundle = bundleOf(Constants.ARTICLE_EXTRA to item)
        findNavController().navigate(R.id.action_nav_home_to_detailFragment, bundle)
    }

    private fun refreshList(data: List<Article>?) {
        listAdapter.submitList(data)
        if (data.isNullOrEmpty()) emptyUI()
        else notEmptyUI()
    }

    private fun emptyUI() {
        binding.emptyNewsList.visible()
        binding.rvList.gone()
    }

    private fun notEmptyUI() {
        binding.emptyNewsList.gone()
        binding.rvList.visible()
    }

    private fun saveUpdatedCountry() {
        val countryCode = settingsHelper.getSelectedCountry()
        settingsHelper.setLastUpdateCountry(countryCode)
    }

    private fun displayErrorNotification(errorMessage: String) {
        val country = settingsHelper.getCountryName()
        val text: String = getString(R.string.load_error_message, country, errorMessage)

        val notification = Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_LONG)

        notification.setAction(getString(R.string.snackbar_close)) {
            notification.dismiss()
        }.show()
    }

    private fun setToolbarTitle() {
        val country = checkCountry()
        val title = if (country.isNotEmpty()) {
            getString(R.string.title_news_country, country)
        } else {
            getString(R.string.title_news)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }

    private fun checkCountry(): String {
        val lastUpdated = settingsHelper.getLastUpdateCountry()
        val current = settingsHelper.getSelectedCountry()
        return if (current == lastUpdated) {
            settingsHelper.getCountryName()
        } else ""
    }

    private fun hideProgressBar(delayed: Boolean) {
        lifecycleScope.launch {
            if (delayed) delay(800) /// better visibility for the user
            _binding?.swipeRefreshLayout?.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}