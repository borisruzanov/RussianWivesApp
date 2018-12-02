package com.borisruzanov.russianwives.mvp.ui.filter

import android.content.Context
import android.os.Bundle
import android.support.annotation.ArrayRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Spinner

import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.R

import java.util.Arrays

import javax.inject.Inject

import butterknife.BindViews
import butterknife.ButterKnife
import butterknife.OnClick
import com.borisruzanov.russianwives.di.component

class FilterDialogFragment : MvpAppCompatDialogFragment(), FilterView {


    @Inject
    @InjectPresenter
    lateinit var presenter: FilterDialogPresenter

    @ProvidePresenter fun provideFilterDialogPresenter() = presenter


    //not working without @JvmSuppressWildcards:  can't use kotlin List<> for BindViews
    @BindViews(R.id.spinner_gender_ni, R.id.spinner_age_ni, R.id.spinner_country, R.id.spinner_relationship_statuses, R.id.spinner_body_types, R.id.spinner_ethnicities, R.id.spinner_faith_types, R.id.spinner_smoke_statuses, R.id.spinner_drink_statuses, R.id.spinner_have_kids_statuses, R.id.spinner_want_kids_statuses)
    lateinit var spinners: List<@JvmSuppressWildcards Spinner>


    private lateinit var filterListener: FilterListener

    interface FilterListener {
        fun onUpdate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().component.inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is FilterListener) {
            filterListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        val view = inflater.inflate(R.layout.dialog_filters, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.getSavedValues()
    }

    override fun onResume() {
        super.onResume()
        dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    @OnClick(R.id.button_search)
    fun onSearchClicked() {
        presenter.saveValues(spinners)
        filterListener.onUpdate()

        dismiss()
    }

    @OnClick(R.id.button_cancel)
    fun onCancelClicked() {
        dismiss()
    }

    override fun getSavedValues(stringList: List<String>, resIdList: List<Int>) {
        for (i in stringList.indices) {
            if (presenter.isNotDefault(stringList[i])) {
                spinners[i].setSelection(getIndexOfElement(resIdList[i], stringList[i]))
            }
        }
    }

    private fun getIndexOfElement(@ArrayRes resId: Int, value: String): Int {
        val list = Arrays.asList(*resources.getStringArray(resId))
        return list.indexOf(value)
    }

    companion object {

        var TAG = "Dialog Fragment"
    }

}
