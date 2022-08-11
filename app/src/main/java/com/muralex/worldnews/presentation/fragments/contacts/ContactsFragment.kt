package com.muralex.worldnews.presentation.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.muralex.worldnews.R
import com.muralex.worldnews.databinding.FragmentContactsBinding
import com.muralex.worldnews.presentation.dialogs.CountryDialogFragment
import com.muralex.worldnews.presentation.dialogs.RateDialogFragment
import com.muralex.worldnews.presentation.utils.ContactActions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var actions: ContactActions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSendFeedback.setOnClickListener{actions.sendFeedback()}
            btnSendReport.setOnClickListener{actions.sendReport()}
            btnShare.setOnClickListener{actions.shareApp()}
            btnRate.setOnClickListener{
                openRateDialog()
            }
        }
    }

    private fun openRateDialog() {
        val dialog = RateDialogFragment()
        dialog.show(parentFragmentManager, RateDialogFragment.TAG)
        parentFragmentManager.setFragmentResultListener(RateDialogFragment.REQUEST_KEY,
            viewLifecycleOwner) { key, bundle ->
            if (key == RateDialogFragment.REQUEST_KEY) {
                sendRateFromDialog(bundle)
            }
        }
    }

    private fun sendRateFromDialog(bundle: Bundle) {
        when (bundle[RateDialogFragment.RESULT_TYPE]) {
            RateDialogFragment.RESULT_EXCELLENT -> {
                actions.rateApp()
            }

            RateDialogFragment.RESULT_FEEDBACK -> {
                val subject = bundle[RateDialogFragment.RESULT_SUBJECT].toString()
                val body = bundle[RateDialogFragment.RESULT_BODY].toString()
                actions.sendEmailWithData(subject, body)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}