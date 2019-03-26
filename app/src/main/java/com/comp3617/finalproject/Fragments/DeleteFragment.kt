package com.comp3617.finalproject.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment


class DeleteFragment : DialogFragment(){
    private lateinit var listener : DeleteFragmentListener

    // Interface for activity to implement
    interface DeleteFragmentListener{
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    // Creates dialog and defines button actions
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ctx = activity as Context
        val builder = AlertDialog.Builder(ctx)
        builder.setMessage("Delete Goal?")
            .setPositiveButton("Yes") { _, id ->
                listener.onDialogPositiveClick(this)
            }
            .setNegativeButton("Cancel") { _, id ->
                // On cancel do nothing
            }
        return builder.create()
    }

    // sets listener on attach
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as DeleteFragmentListener
    }
}