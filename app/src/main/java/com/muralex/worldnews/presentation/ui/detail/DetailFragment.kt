package com.muralex.worldnews.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.muralex.worldnews.R
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.FragmentDetailBinding
import com.muralex.worldnews.presentation.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args : DetailFragmentArgs by navArgs()

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
        binding.article = article
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        binding.button.setOnClickListener {
            openWebPage(article)
        }
    }

    private fun openWebPage(article: Article) {
        val bundle = bundleOf(  Constants.ARTICLE_URL to article.url )
        findNavController().popBackStack()
        findNavController().navigate(R.id.webDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}