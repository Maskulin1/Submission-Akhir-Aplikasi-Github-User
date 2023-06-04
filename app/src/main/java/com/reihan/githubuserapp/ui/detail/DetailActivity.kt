package com.reihan.githubuserapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.reihan.githubuserapp.R
import com.reihan.githubuserapp.data.database.UserEntity
import com.reihan.githubuserapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        DetailViewModel.ViewModelFactory.getInstance(application)
    }

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.github_user_detail)

        val username = intent.getStringExtra(EXTRA_USER) as String
        val avatar = intent.getStringExtra(EXTRA_AVATAR) as String

        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        viewModel.getDetailUser(username)
        showLoading(true)
        viewModel.userDetail.observe(this) {
            showLoading(false)
            if (it != null) {
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = resources.getString(R.string.data_followers, it.followers)
                    tvFollowing.text = resources.getString(R.string.data_following, it.following)
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(ciProfile)
                }
            }
        }

        viewModel.getDataByUsername(username).observe(this) {
            isFavorite = it.isNotEmpty()
            val favoriteEntity = UserEntity(username, avatar)
            if (it.isEmpty()) {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.ic_favorite_border
                    )
                )
                binding.fabFavorite.contentDescription = getString(R.string.favorite_added)
            } else {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.ic_favorite_full
                    )
                )
                binding.fabFavorite.contentDescription = getString(R.string.favorite_removed)
            }

            binding.fabFavorite.setOnClickListener {
                if (isFavorite) {
                    viewModel.deleteData(favoriteEntity)
                    Toast.makeText(this, R.string.favorite_removed, Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insertData(favoriteEntity)
                    Toast.makeText(this, R.string.favorite_added, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.errorMessage.observe(this) {
            binding.apply {
                tvErrorMessage.text = it
                tvErrorMessage.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = arrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}