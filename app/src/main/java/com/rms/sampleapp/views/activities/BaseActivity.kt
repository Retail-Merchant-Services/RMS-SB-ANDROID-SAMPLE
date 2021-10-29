package com.rms.sampleapp.views.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.rms.sampleapp.R
import com.rms.sampleapp.listeners.BackPressListener
import com.rms.sampleapp.views.dialogs.TransparentProgressDialog

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val ANIMATION_NONE = 0
    }

    private var dialog: TransparentProgressDialog? = null
    private val parentContainer by lazy { findViewById<View>(R.id.container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = TransparentProgressDialog(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog = null
    }

    fun showProgressDialog() {
        dialog?.show()
    }

    fun hideProgressDialog() {
        dialog?.dismiss()
    }

    fun doFragmentTransaction(
        fragManager: FragmentManager = supportFragmentManager,
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String = "",
        @AnimatorRes enterAnimation: Int = ANIMATION_NONE,
        @AnimatorRes exitAnimation: Int = ANIMATION_NONE,
        @AnimatorRes popEnterAnimation: Int = ANIMATION_NONE,
        @AnimatorRes popExitAnimation: Int = ANIMATION_NONE,
        isAddFragment: Boolean = true,
        isAddToBackStack: Boolean = true,
        sharedElements: List<View>? = null,
        allowStateLoss: Boolean = false
    ) {

        val fragmentTransaction = fragManager.beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)

        if (isAddFragment) {
            fragmentTransaction.add(containerViewId, fragment, tag)
        } else {
            fragmentTransaction.replace(containerViewId, fragment, tag)
        }

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }

        sharedElements?.forEach { view ->
            fragmentTransaction.addSharedElement(view, view.transitionName)
        }

        if (allowStateLoss) {
            fragmentTransaction.commitAllowingStateLoss()
        } else {
            fragmentTransaction.commit()
        }
    }

    fun showSnackbarMessage(
        message: String,
        isLong: Boolean = false,
        isIndefinite: Boolean = false,
        containerView: View? = parentContainer
    ) {
        containerView?.let {
            Snackbar.make(
                it, message, when {
                    isIndefinite -> Snackbar.LENGTH_INDEFINITE
                    isLong -> Snackbar.LENGTH_LONG
                    else -> Snackbar.LENGTH_SHORT
                }
            ).show()
        }
    }

    fun showToastMessage(message: String, isLong: Boolean = false) {
        Toast.makeText(
            this, message, when (isLong) {
                true -> Toast.LENGTH_LONG
                false -> Toast.LENGTH_SHORT
            }
        ).show()
    }

    fun showDialogMessage(title: String, message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.okay)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create().show()
//        BaseActionDialogFragment.newInstance(
//            BaseActionDialogFragment.DIALOG_TYPE_NO_ACTION,
//            title = title,
//            description = message,
//            positiveButtonText = getString(
//                R.string.okay
//            )
//        ).show(supportFragmentManager, "")
    }

    override fun onBackPressed() {
        var handled = false

        val fragmentList = supportFragmentManager.fragments

        for (f in fragmentList) {
            if (f is BackPressListener) {
                handled = f.onBackPressed()
                if (handled) {
                    break
                }
            }
        }

        if (!handled) {
            super.onBackPressed()
        }
    }

}