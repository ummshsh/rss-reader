package com.ummshsh.rssreader.ui.articleview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.ummshsh.rssreader.R

class ArticleFragment : Fragment() {

    private lateinit var viewModel: ArticleViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        viewModel =
            ViewModelProvider(this, ArticleViewModel.Factory(args.articleId, activity.application))
                .get(ArticleViewModel::class.java)

        var text = TextView(context)
        text.text = HtmlCompat.fromHtml(viewModel.content, HtmlCompat.FROM_HTML_MODE_LEGACY);

        var view = inflater.inflate(R.layout.article_fragment, container, false)
        view.findViewById<ConstraintLayout>(R.id.articleConstraintLayout).addView(text)

        return view
    }
}