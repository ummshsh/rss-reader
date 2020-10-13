package com.ummshsh.rssreader

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.ummshsh.rssreader.ui.main.MainFragment
import com.ummshsh.rssreader.ui.main.MainFragmentDirections
import com.ummshsh.rssreader.ui.main.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        val toolbar: Toolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this, MainActivityViewModel.Factory(application))
            .get(MainActivityViewModel::class.java)

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

        var viewModelMainFragment = ViewModelProvider(this, MainViewModel.Factory(application))
            .get(MainViewModel::class.java)

        val adapter =
            DrawerFeedListAdapter(object : DrawerFeedListAdapter.OnFeedClickListener {
                override fun clickFeed(id: Int) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    viewModelMainFragment.showOnlyFeed(id)
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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.mainFragment) {
                viewModel.refreshFeeds()
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    var action =
                        MainFragmentDirections.actionMainFragmentToFeedManagementFragment()
                    intent.clipData?.let { action.setFeedToAdd(it.getItemAt(0).text.toString()) }
                    navController.navigate(action)
                }
            }
        }
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
