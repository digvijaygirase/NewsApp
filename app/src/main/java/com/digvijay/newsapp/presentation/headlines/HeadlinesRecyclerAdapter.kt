package com.digvijay.newsapp.presentation.headlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.digvijay.newsapp.R
import com.digvijay.newsapp.databinding.ListItemHeadlineBinding
import com.digvijay.newsapp.domain.NewsArticle

class HeadlinesRecyclerAdapter(
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<HeadlinesRecyclerAdapter.ViewHolder>() {

    private var headlines: List<NewsArticle> = listOf()
    private var context = (itemClickListener as HeadlinesFragment).requireContext()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.titleTV.text = headlines[position].title
            Glide.with(binding.root)
                .load(
                    headlines[position].urlToImage ?: AppCompatResources.getDrawable(
                        context, R.drawable.no_image
                    )
                )
                .apply(RequestOptions.overrideOf(400))
                .into(binding.thumbnailIV)
            binding.root.setOnClickListener { itemClickListener.onClick(position) }
        }
    }

    override fun getItemCount(): Int {
        return headlines.size
    }

    fun setListData(headlines: List<NewsArticle>) {
        this.headlines = headlines
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListItemHeadlineBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ItemClickListener {
        fun onClick(position: Int)
    }
}