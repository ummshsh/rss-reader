package com.ummshsh.rssreader.ui.articleview

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.ummshsh.rssreader.R
import com.ummshsh.rssreader.database.DatabaseContract
import com.ummshsh.rssreader.databinding.ArticleFragmentBinding
import com.ummshsh.rssreader.databinding.FeedManagementFragmentBinding
import com.ummshsh.rssreader.ui.feedmanagement.FeedManagementViewModel

class ArticleFragment : Fragment() {

    companion object {
        fun newInstance() = ArticleFragment()
    }

    private lateinit var viewModel: ArticleViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding: ArticleFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.article_fragment, container, false)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        viewModel = ViewModelProviders
            .of(this, ArticleViewModel.Factory(args.articleId, activity.application))
            .get(ArticleViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
//        // TODO: Use the ViewModel
//    }
}