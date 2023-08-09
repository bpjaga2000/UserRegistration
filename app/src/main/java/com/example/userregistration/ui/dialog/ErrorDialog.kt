package com.example.userregistration.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.example.userregistration.api.ErrorModel
import com.example.userregistration.databinding.DialogErrorBinding

class ErrorDialog(
    private val errorModel: ErrorModel
) : DialogFragment() {

    private lateinit var binding: DialogErrorBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogErrorBinding.inflate(inflater, container, false)
        binding.model = errorModel
        binding.errorBtn.setOnClickListener {
            dismiss()
        }
        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?) = Dialog(requireContext()).apply {
        window?.setLayout(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } else {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        setContentView(RelativeLayout(requireContext()).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setBackgroundColor(resources.getColor(android.R.color.transparent, null))
            } else {
                setBackgroundColor(resources.getColor(android.R.color.transparent))
            }
        })

    }

}