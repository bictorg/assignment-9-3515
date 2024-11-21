package edu.temple.browsr

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), TabFragment.ControlInterface {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private var tabLayout: TabLayout? = null
    private var recyclerView: RecyclerView? = null
    private var tabTitleAdapter: TabTitleAdapter? = null
    private val titles = mutableListOf<String>()
    private val tabTitles = mutableListOf<String>()

    private val browserViewModel: BrowserViewModel by lazy {
        ViewModelProvider(this)[BrowserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager()
        setupOrientation()
    }

    private fun setupViewPager() {
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = browserViewModel.getNumberOfTabs()
            override fun createFragment(position: Int) = TabFragment()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout?.selectTab(tabLayout?.getTabAt(position))
                
                val fragment = supportFragmentManager.findFragmentByTag("f$position")
                if (fragment is TabFragment) {
                    fragment.getPageTitle().observe(this@MainActivity) { title ->
                        tabTitles[position] = title
                        updateTitles()
                    }
                }
            }
        })
    }

    private fun setupOrientation() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setupTabLayout()
        } else {
            setupRecyclerView()
        }
    }

    private fun setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout?.let { tabs ->
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = titles.getOrNull(position) ?: "New Tab"
            }.attach()
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.let { rv ->
            rv.layoutManager = LinearLayoutManager(this)
            tabTitleAdapter = TabTitleAdapter(titles) { position ->
                viewPager.currentItem = position
            }
            rv.adapter = tabTitleAdapter
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupOrientation()
    }

    override fun newPage() {
        browserViewModel.addTab()
        titles.add("New Tab")
        tabTitles.add("New Tab")
        viewPager.adapter?.notifyItemChanged(browserViewModel.getNumberOfTabs() - 1)
        viewPager.setCurrentItem(browserViewModel.getNumberOfTabs() - 1, true)
        tabTitleAdapter?.notifyDataSetChanged()
    }

    private fun updateTitles() {
        tabLayout?.let { tabs ->
            for (i in 0 until tabs.tabCount) {
                tabs.getTabAt(i)?.text = tabTitles.getOrNull(i) ?: "New Tab"
            }
        }
        tabTitleAdapter?.updateTitles(tabTitles)
    }
}