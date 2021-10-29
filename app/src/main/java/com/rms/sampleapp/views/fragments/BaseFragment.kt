package com.rms.sampleapp.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.rms.sampleapp.R
import com.rms.sampleapp.viewmodels.BaseViewModel
import com.rms.sampleapp.views.activities.BaseActivity

abstract class BaseFragment<BVM : BaseViewModel> : Fragment() {

    protected var baseActivity: BaseActivity? = null

    val viewModel by lazy { provideBaseViewModel() }

    private val parentContainer by lazy { view?.findViewById<View>(R.id.container) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResource(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        baseObservables()
        registerObservables()
    }

    private fun baseObservables() {
        viewModel.isShowLoader.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> showProgressDialog()
                false -> hideProgressDialog()
            }
        })
        viewModel.snackbarMessage.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                showSnackbarMessage(it, isLong = true)
            }
        })
        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                showToastMessage(it, isLong = true)
            }
        })
        viewModel.snackbarResId.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showSnackbarMessage(getString(it), isLong = true)
            }
        })
        viewModel.toastResId.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToastMessage(getString(it), isLong = true)
            }
        })
        viewModel.backAction.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                baseActivity?.onBackPressed()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as? BaseActivity
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }

    protected fun showProgressDialog() {
        baseActivity?.showProgressDialog()
    }

    protected fun hideProgressDialog() {
        baseActivity?.hideProgressDialog()
    }

    fun doFragmentTransaction(
        fragManager: FragmentManager = childFragmentManager,
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String = "",
        @AnimatorRes enterAnimation: Int = 0,
        @AnimatorRes exitAnimation: Int = 0,
        @AnimatorRes popEnterAnimation: Int = 0,
        @AnimatorRes popExitAnimation: Int = 0,
        isAddFragment: Boolean = true,
        isAddToBackStack: Boolean = true,
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

        if (allowStateLoss) {
            fragmentTransaction.commitAllowingStateLoss()
        } else {
            fragmentTransaction.commit()
        }
    }


    fun showSnackbarMessage(
        message: String, isLong: Boolean = false, isIndefinite: Boolean = false
    ) {
        parentContainer?.let { container ->
            baseActivity?.showSnackbarMessage(message, isLong, isIndefinite, container)
        } ?: baseActivity?.showSnackbarMessage(message, isLong, isIndefinite)
    }

    fun showToastMessage(message: String, isLong: Boolean = false) {
        baseActivity?.showToastMessage(message, isLong)
    }

    fun showDialogMessage(title: String, message: String) {
        baseActivity?.showDialogMessage(title, message)
    }

    fun goBack() {
        baseActivity?.onBackPressed()
    }

    @LayoutRes
    abstract fun getLayoutResource(): Int

    abstract fun init()

    abstract fun provideBaseViewModel(): BVM

    abstract fun registerObservables()
}