package com.reihan.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.reihan.githubuserapp.api.ApiConfig
import com.reihan.githubuserapp.data.response.GithubResponse
import com.reihan.githubuserapp.data.response.ItemsItem
import com.reihan.githubuserapp.ui.setting.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val prefs: SettingPreferences) : ViewModel() {

    private val _username = MutableLiveData<List<ItemsItem>>()
    val username: LiveData<List<ItemsItem>> = _username

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findGithubUser(USERNAME)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return prefs.getThemeSetting().asLiveData()
    }

    fun findGithubUser(query: String) {
        _isLoading.value = true
         val client = ApiConfig.getApiService().getFindUsers(query)
         client.enqueue(object : Callback<GithubResponse> {
             override fun onResponse(
                 call: Call<GithubResponse>,
                 response: Response<GithubResponse>
             ) {
                 _isLoading.value = false
                 if (response.isSuccessful) {
                        _username.value = response.body()?.items
                 } else {
                     Log.d(TAG, "onFailure: ${response.message()}")
                 }
             }

             override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                 _isLoading.value = false
                 Log.d(TAG, "onFailure: ${t.message.toString()}")
             }
         })
    }

    class ViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val USERNAME = "Reihan"
    }
}