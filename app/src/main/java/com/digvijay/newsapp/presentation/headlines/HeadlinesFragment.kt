package com.digvijay.newsapp.presentation.headlines

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.digvijay.newsapp.R
import com.digvijay.newsapp.databinding.FragmentHeadlinesBinding
import com.digvijay.newsapp.domain.NewsCountry
import com.digvijay.newsapp.framework.ViewModelFactory
import com.digvijay.newsapp.framework.api.Status

class HeadlinesFragment : Fragment(), HeadlinesRecyclerAdapter.ItemClickListener {

    private lateinit var binding: FragmentHeadlinesBinding
    private val viewModel: HeadlinesViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application)
    }
    private lateinit var adapter: HeadlinesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = HeadlinesRecyclerAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeadlinesBinding.inflate(layoutInflater, container, false)
        setupViews()

        viewModel.headlinesResource.observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { headlines ->
                            adapter.setListData(headlines)
                            binding.headlinesRV.adapter = adapter
                        }
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (adapter.itemCount == 0)
            viewModel.fetchNewsHeadlines()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_headlines, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (viewModel.getCurrentSelectedCountry() == NewsCountry.USA) {
            menu.findItem(R.id.country).title = getString(R.string.usa)
        } else {
            menu.findItem(R.id.country).title = getString(R.string.canada)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.usa -> {
                viewModel.changeNewsCountry(NewsCountry.USA)
            }
            R.id.canada -> {
                viewModel.changeNewsCountry(NewsCountry.CANADA)
            }
        }
        viewModel.fetchNewsHeadlines()
        requireActivity().invalidateOptionsMenu()
        return super.onOptionsItemSelected(item)
    }

    private fun setupViews() {
        val layoutManager = LinearLayoutManager(context)
        binding.headlinesRV.addItemDecoration(
            DividerItemDecoration(
                context,
                layoutManager.orientation
            )
        )
        binding.headlinesRV.layoutManager = layoutManager

        with(binding.swipeRefresh) {
            setOnRefreshListener {
                viewModel.fetchNewsHeadlines()
                isRefreshing = false
            }
        }
    }

    override fun onClick(position: Int) {
        val action =
            HeadlinesFragmentDirections.actionHeadlinesFragmentToNewsArticleFragment(position)
        findNavController().navigate(action)
    }
}