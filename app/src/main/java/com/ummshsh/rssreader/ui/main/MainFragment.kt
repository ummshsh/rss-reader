package com.ummshsh.rssreader.ui.main

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.ummshsh.rssreader.R
import com.ummshsh.rssreader.databinding.MainFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var binding: MainFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(activity, MainViewModel.Factory(activity.application))
            .get(MainViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = RssListAdapter(object : RssListAdapter.OnArticleClickListener {
            override fun clickOnArticle(id: Int) {
                var action = MainFragmentDirections.actionMainFragmentToArticleFragment(id)
                binding.root.findNavController()
                    .navigate(action)
            }
        })
        binding.rssEntriesList.adapter = adapter
        viewModel.articles.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                adapter.listArticles = it
            }
        })

        root = binding.root
        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_mark_all_read -> {
                viewModel.markArticlesAsRead(true)
                Snackbar.make(root, "All marked as read", Snackbar.LENGTH_LONG)
                    .setAction("Undo") { _ -> viewModel.markArticlesAsRead(false) }
                    .show()
            }
            R.id.action_toggle_read -> viewModel.toggleOnlyUnreadArticles()
            R.id.action_toggle_sorting -> viewModel.toggleSorting()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }
}