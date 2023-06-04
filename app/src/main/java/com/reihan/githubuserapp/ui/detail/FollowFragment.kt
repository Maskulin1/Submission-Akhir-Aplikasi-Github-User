package com.reihan.githubuserapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.reihan.githubuserapp.databinding.FragmentFollowBinding
import com.reihan.githubuserapp.ui.main.MainAdapter

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var position = 0
        var username = arguments?.getString(ARG_USERNAME)

        setAdapter()

        detailViewModel =
            ViewModelProvider(
                requireActivity(),
                ViewModelProvider.NewInstanceFactory()
            )[DetailViewModel::class.java]

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1) {
            showLoading(true)
            username?.let { detailViewModel.getFollowers(it) }
        } else {
            showLoading(true)
            username?.let { detailViewModel.getFollowing(it) }
        }


        detailViewModel.followers.observe(viewLifecycleOwner) {
            if (position == 1) {
                adapter.setList(it)
            }
            showLoading(false)
        }

        detailViewModel.following.observe(viewLifecycleOwner) {
            if (position == 2) {
                adapter.setList(it)
            }
            showLoading(false)
        }
    }

    private fun setAdapter() {
        adapter = MainAdapter(ArrayList())
        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = ""
    }
}