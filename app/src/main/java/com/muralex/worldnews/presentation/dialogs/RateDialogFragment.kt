package com.muralex.worldnews.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.gone
import com.muralex.worldnews.app.utils.visible
import com.muralex.worldnews.databinding.DialogRateBinding


class RateDialogFragment : DialogFragment() {
    private val stars = arrayOfNulls<View>(5)
    private var rating = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater =  requireActivity().layoutInflater
        val content = inflater.inflate(R.layout.dialog_rate, null)
        val binding = DialogRateBinding.bind(content)
        initStarViews(binding)
        setOnClicks(binding)

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .create()
    }

    private fun setOnClicks(binding: DialogRateBinding) {
        with(binding) {
            btnRateSubmit.setOnClickListener { processRating(binding) }
            btnRateLate.setOnClickListener { dismiss() }
            btnRateSend.setOnClickListener {
                collectResult(binding)
                dismiss()
            }
        }
    }

    private fun processRating(binding: DialogRateBinding) {
        if (rating == 5) {
            prepareRateResult()
            dismiss()
        } else {
            askComment(binding)
        }
    }

    private fun prepareRateResult() {
        val bundle = bundleOf(RESULT_TYPE to RESULT_EXCELLENT)
        parentFragmentManager.setFragmentResult(REQUEST_KEY, bundle)
    }

    private fun collectResult(binding: DialogRateBinding)  {
        val subject = binding.rateTitleSend.text.toString()
        val mailBody = binding.etRateFeedback.text.toString()
        val bundle = bundleOf(RESULT_TYPE to RESULT_FEEDBACK,
            RESULT_SUBJECT to subject, RESULT_BODY to mailBody)
        parentFragmentManager.setFragmentResult(REQUEST_KEY, bundle)
    }

    private fun askComment(binding: DialogRateBinding) {
        binding.rateValueWrap.gone()
        binding.rateFeedbackWrap.visible()
        val title = getString(R.string.send_rate_title)
        val stars = StringBuilder()
        repeat(rating) {
            stars.append("★")
        }
        binding.rateTitleSend.text = String.format("%s %s", title, stars)
    }

    private fun initStarViews(binding: DialogRateBinding) {
        stars[0] = binding.rateStar1
        stars[1] = binding.rateStar2
        stars[2] = binding.rateStar3
        stars[3] = binding.rateStar4
        stars[4] = binding.rateStar5

        for (star in stars) {
            star?.setOnClickListener { view: View? ->
                checkRating(view)
            }
        }
    }

    private fun checkRating(view: View?) {
        val rate = view?.tag.toString().toInt()
        rating = rate
        for (i in stars.indices) {
            val starWrap: View? = stars[i]
            starWrap?.let {
                val star = starWrap.findViewById<ImageView>(R.id.rate_star)
                star.setImageResource(R.drawable.ic_star_borded_inactive)
                if (rate > i) {
                    star.setImageResource(R.drawable.ic_star_borded)
                }
            }
        }
    }

    companion object {
        const val REQUEST_KEY = "rate_dialog"
        const val TAG = "rate"
        const val RESULT_SUBJECT = "subject"
        const val RESULT_BODY = "body"
        const val RESULT_TYPE = "result_type"
        const val RESULT_FEEDBACK = "feedback"
        const val RESULT_EXCELLENT = "excellent"
    }

}