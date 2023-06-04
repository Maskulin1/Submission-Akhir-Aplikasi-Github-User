package com.reihan.githubuserapp.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.reihan.githubuserapp.R
import com.reihan.githubuserapp.data.database.UserEntity
import com.reihan.githubuserapp.databinding.ActivityFavoriteBinding
import com.reihan.githubuserapp.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.github_favorite_user)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavoriteData().observe(this) {
            setFavoriteData(it)
        }

        favoriteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setFavoriteData(dataUser: List<UserEntity>) {
        val items = arrayListOf<UserEntity>()
        dataUser.map {
            val item = UserEntity(
                username = it.username,
                avatar = it.avatar
            )
            items.add(item)
        }
        val adapter = FavoriteAdapter(items)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                startActivity(
                    Intent(this@FavoriteActivity, DetailActivity::class.java)
                        .putExtra(DetailActivity.EXTRA_USER, data.username)
                        .putExtra(DetailActivity.EXTRA_AVATAR, data.avatar)
                )
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModel.ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}