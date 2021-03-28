package com.ummshsh.rssreader.ui.feedmanagementfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ummshsh.rssreader.R
import com.ummshsh.rssreader.databinding.FeedManagementFragmentBinding
import kotlinx.coroutines.*

class FeedManagementFragment : Fragment() {

    private lateinit var viewModel: FeedManagementViewModel
    private val args: FeedManagementFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding: FeedManagementFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.feed_management_fragment, container, false)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, FeedManagementViewModel.Factory(activity.application))
            .get(FeedManagementViewModel::class.java)

        binding.viewModelFeed = viewModel
        binding.lifecycleOwner = this

        binding.editTextUrl.setText(args.feedToAdd)

        val adapter = FeedListAdapter(object : FeedListAdapter.OnFeedDeleteClickListener {
            override fun clickDeleteOnItem(id: Int) {
                viewModel.deleteFeed(id)
            }
        })

        binding.feedEntriesList.adapter = adapter
        viewModel.feeds.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                adapter.listFeeds = it
            }
        })

        binding.buttonAddFeed.setOnClickListener {
            GlobalScope.launch {
                viewModel.addFeed(binding.editTextUrl.text.toString())
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }
}