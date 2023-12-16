package com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.shppprojects.R
import com.example.shppprojects.databinding.FragmentDialogCalendarBinding
import com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile.interfaces.DialogCalendarListener

class DialogCalendar: AppCompatDialogFragment() {

    private lateinit var binding: FragmentDialogCalendarBinding
    private var listener: DialogCalendarListener? = null

    fun setListener(listener : DialogCalendarListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_dialog_calendar, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        binding = FragmentDialogCalendarBinding.bind(dialogView)
        setListeners()
        return builder.create()
    }

    private fun setListeners() {
        val calendar = Calendar.getInstance()
        var date = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}" +
                "/${calendar.get(Calendar.YEAR)}"
        with(binding) {
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                date = "$dayOfMonth/${month+1}/$year"
            }
            textViewSave.setOnClickListener {
                listener?.onDateSelected(date)
                dismiss()
            }
            textViewCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}