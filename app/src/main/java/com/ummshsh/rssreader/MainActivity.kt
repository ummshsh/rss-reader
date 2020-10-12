package com.ummshsh.rssreader

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.get
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.ummshsh.rssreader.databinding.MainActivityBinding
import com.ummshsh.rssreader.databinding.MainFragmentBinding
import com.ummshsh.rssreader.ui.feedmanagement.FeedListAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.main_activity)
        var binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)

        val toolbar: Toolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)

        drawerLayout = findViewById(R.id.container)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainFragment
            ), drawerLayout
        )

        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.navigationView).setupWithNavController(navController)

        viewModel = ViewModelProviders
            .of(this, MainActivityViewModel.Factory(application))
            .get(MainActivityViewModel::class.java)

                val adapter =
            DrawerFeedListAdapter(object : DrawerFeedListAdapter.OnFeedDeleteClickListener {
                override fun clickDeleteOnItem(id: Int) {
                    Toast.makeText(applicationContext, "okay", Toast.LENGTH_LONG).show()
                }
            })

        binding.rssEntriesListDrawer.adapter = adapter
        viewModel.feeds.observe(this, Observer {
            it?.let {
                adapter.listFeeds = it
            }
        })

        findViewById<Button>(R.id.manage_feeds).setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_feedManagementFragment)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        addSubMenuItems()
    }

    private fun addSubMenuItems() {
        findViewById<NavigationView>(R.id.navigationView).menu.add("One")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (navController.currentDestination == navController.graph[R.id.mainFragment]) {
            when (item.itemId) {
                android.R.id.home -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
