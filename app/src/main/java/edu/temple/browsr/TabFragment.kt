package edu.temple.browsr

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider

class TabFragment : Fragment(), ControlFragment.ControlInterface {

    private lateinit var browserActivity: ControlInterface
    private lateinit var pageFragment: PageFragment
    
    private val pageDataViewModel: PageDataViewModel by lazy {
        ViewModelProvider(this)[PageDataViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        browserActivity = context as ControlInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false).apply {
            pageFragment = childFragmentManager.findFragmentById(R.id.page) as PageFragment
        }
    }

    fun getPageTitle(): LiveData<String> {
        return pageDataViewModel.getCurrentTitle()
    }

    override fun loadUrl(url: String) {
        pageFragment.loadUrl(url)
    }

    override fun nextPage() {
        pageFragment.goNext()
    }

    override fun backPage() {
        pageFragment.goBack()
    }

    override fun newPage() {
        browserActivity.newPage()
    }

    interface ControlInterface {
        fun newPage()
    }
}