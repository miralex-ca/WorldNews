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
import com.muralex.worldnews.app.utils.Constants
import com.muralex.worldnews.app.utils.Constants.ARTICLE_URL
import com.muralex.worldnews.app.utils.ResourceProvider
import com.muralex.worldnews.app.utils.SettingsHelper
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.FragmentHomeBinding
import com.muralex.worldnews.presentation.dialogs.CountryDialogFragment
import com.muralex.worldnews.presentation.fragments.home.HomeViewModel.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
    @Inject
    lateinit var resourceProvider: ResourceProvider

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
        getNewsOnStart()
        setUpObservation()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_home, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.action_refresh -> {
                        updateNewsData()
                        true
                    }
                    R.id.action_select -> {
                        showCountryDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    fun showCountryDialog() {
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
                updateNewsData()
            }
        }
    }

    private fun getNewsOnStart() {
        val refreshOnStart = settingsHelper.isStartRefreshEnabled() && viewModel.startRefresh
        val country = settingsHelper.getSelectedCountry()

        if (refreshOnStart) updateNewsData()
        else viewModel.getNews(country)
    }

    private fun setupNewsList() {
        listAdapter = ArticlesListAdapter()

        listAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        listAdapter.setOnItemClickListener { _, item ->
            setClickEvents(item)
        }

        binding.rvList.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = listAdapter
        }
    }

    private fun updateNewsData() {
        val country = settingsHelper.getSelectedCountry()
        viewModel.setNewsCountry(country)
        viewModel.updateNews()
    }

    private fun setupSwipeRefresh() {

        binding.swipeRefreshLayout.apply {

            isEnabled = settingsHelper.isSwipeDownEnabled()

            setOnRefreshListener {
                updateNewsData()
            }

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
        findNavController().navigate(R.id.action_nav_home_to_detailFragment, bundle)
    }

    private fun setUpObservation() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Loading -> showProgressBar()
                is ViewState.ListLoadFailure -> {
                    hideProgressBar(true)
                    val message = resourceProvider.getErrorMessage(state.data.getErrorType())
                    displayErrorNotification(message)
                    refreshListAndTitle(state.data.data)
                }
                is ViewState.ListLoaded -> {
                    hideProgressBar(false)
                    refreshListAndTitle(state.data.data)
                }
                is ViewState.ListRefreshed -> {
                    hideProgressBar(true)
                    saveUpdatedCountry()
                    refreshListAndTitle(state.data.data)
                }
            }
        }
    }

    private fun refreshListAndTitle(data: List<Article>?) {
        listAdapter.submitList(data)

        setToolbarTitle()
    }

    private fun saveUpdatedCountry() {
        val countryCode = settingsHelper.getSelectedCountry()
        settingsHelper.setLastUpdateCountry(countryCode)
    }

    private fun checkCountry(): String {
        val lastUpdated = settingsHelper.getLastUpdateCountry()
        val current = settingsHelper.getSelectedCountry()

        return if (current == lastUpdated) {
            settingsHelper.getCountryName()
        } else ""
    }

    private fun displayErrorNotification(message: String) {
        val country = settingsHelper.getCountryName()



        val text: String = getString(R.string.load_error_message, country, message)
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

    override fun onResume() {
        super.onResume()
        setToolbarTitle()
    }

    private fun showProgressBar() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun hideProgressBar(delayed: Boolean) {
        lifecycleScope.launch {
            if (delayed) delay(800) /// better visibility for the user
            _binding?.swipeRefreshLayout?.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}