package com.ummshsh.rssreader.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ummshsh.rssreader.R
import com.ummshsh.rssreader.databinding.MainFragmentBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var binding: MainFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProviders
            .of(this, MainViewModel.Factory(activity.application))
            .get(MainViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = RssListAdapter()
        binding.rssEntriesList.adapter = adapter
        viewModel.articles.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                adapter.listArticles = it
            }
        })

        //TODO: to delete
        binding.buttonAddFeed.setOnClickListener {
            GlobalScope.launch {
                viewModel.addFeedTesting()
            }
        }

        return binding.root
    }
}