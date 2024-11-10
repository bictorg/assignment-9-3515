package edu.temple.browsr

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TabFragment : Fragment(), ControlFragment.ControlInterface {

    private lateinit var browserActivity: ControlInterface

    private lateinit var pageFragment: PageFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)

        browserActivity = context as ControlInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false).apply {
            pageFragment = childFragmentManager.findFragmentById(R.id.page) as PageFragment
        }
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

    // route invocation from child fragment to parent activity
    override fun newPage() {
        browserActivity.newPage()
    }

    interface ControlInterface {
        fun newPage()
    }
}