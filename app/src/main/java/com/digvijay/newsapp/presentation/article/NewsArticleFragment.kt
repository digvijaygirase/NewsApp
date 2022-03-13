package com.digvijay.newsapp.presentation.article

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.digvijay.newsapp.R
import com.digvijay.newsapp.databinding.FragmentNewsArticleBinding
import com.digvijay.newsapp.presentation.headlines.HeadlinesViewModel
import com.digvijay.newsapp.util.setDate
import com.digvijay.newsapp.util.setHtmlText

class NewsArticleFragment : Fragment() {

    private lateinit var binding: FragmentNewsArticleBinding
    private val viewModel: HeadlinesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: NewsArticleFragmentArgs by navArgs()
        viewModel.headlinesResource.value?.run {
            data?.get(args.position)?.run {
                if (!author.isNullOrEmpty()) {
                    binding.authorNameTV.setHtmlText(author)
                } else {
                    binding.authorNameTV.text = getString(R.string.unknown)
                }
                binding.titleTV.text = title
                binding.publishedDateTV.setDate(publishedAt?:"")
                binding.descriptionTV.text = description
                binding.contentTV.text = content
                Glide.with(binding.root)
                    .load(urlToImage)
                    .placeholder(R.drawable.no_image)
                    .into(binding.newsImageIV)
            }
        }
    }
}