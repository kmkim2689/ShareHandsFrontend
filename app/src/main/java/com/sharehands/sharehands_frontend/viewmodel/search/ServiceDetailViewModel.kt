package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ServiceContent
import com.sharehands.sharehands_frontend.network.search.UserProfile
import com.sharehands.sharehands_frontend.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ServiceDetailViewModel:ViewModel() {
    private var _contents = MutableLiveData<ServiceContent>()
//        .apply { ServiceContent(
//            null, null, null, null, null, null,
//            null, null, null, null, null,null, null, null,
//            null, null, null,null,null, ArrayList<String>(),null,null,
//            null,null,null,null,null,null,null,null,null) }
    val contents: LiveData<ServiceContent>
        get() = _contents

    private var _isAuthor = MutableLiveData<Boolean>()
    val isAuthor: LiveData<Boolean>
        get() = _isAuthor

    private var _isExpired = MutableLiveData<Boolean>()
    val isExpired: LiveData<Boolean>
        get() = _isExpired

    private var _isFull = MutableLiveData<Boolean>()
    val isFull: LiveData<Boolean>
        get() = _isFull

    private var _isApplied = MutableLiveData<Boolean>()
    val isApplied: LiveData<Boolean>
        get() = _isApplied

    private var _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean>
        get() = _isLiked

    private var _isScraped = MutableLiveData<Boolean>()
    val isScraped: LiveData<Boolean>
        get() = _isScraped

    private var _photoList = MutableLiveData<List<String>?>()
    val photoList: LiveData<List<String>?>
        get() = _photoList

    private var _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private var _profile = MutableLiveData<UserProfile>()
    val profile: LiveData<UserProfile>
        get() = _profile

    private var _rating = MutableLiveData<String>()
    val rating: LiveData<String>
        get() = _rating

    private var _recruited = MutableLiveData<String>()
    val recruited: LiveData<String>
        get() = _recruited

    private var _applied = MutableLiveData<String>()
    val applied: LiveData<String>
        get() = _applied

    private var _completed = MutableLiveData<String>()
    val completed: LiveData<String>
        get() = _completed

    private var _profileUrl = MutableLiveData<String>()
    val profileUrl: LiveData<String>
        get() = _profileUrl

    fun showContents(token: String, serviceId: Int) {
        Log.d("봉사활동 상세 불러오기 아이디", "${serviceId}")
        RetrofitClient.createRetorfitClient().getService(token, serviceId)
            .enqueue(object : Callback<ServiceContent> {
                override fun onResponse(
                    call: Call<ServiceContent>,
                    response: Response<ServiceContent>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            val result = response.body()
                            Log.d("봉사활동 상세 불러오기 데이터 result 변수", "${result}")
                            _contents.value = result!!
                            _isApplied.value = result.didApply!!
                            _isAuthor.value = result.author!!
                            val photoList = result.photoList
                            _isLiked.value = result.didLike!!
                            _isScraped.value = result.didScrap!!
                            _photoList.value = result.photoList!!
                            _isExpired.value = result.isExpired
                            _isFull.value = result.isFull
                            _isSuccessful.value = true
                        } else {
                            Log.d("봉사활동 상세 데이터 불러오기 실패", response.code().toString())
                            _isSuccessful.value = false
                        }
                    }
                }

                override fun onFailure(call: Call<ServiceContent>, t: Throwable) {
                    Log.d("봉사활동 상세 데이터 불러오기 실패", t.message.toString())
                    _isSuccessful.value = false
                }
            })
    }

    fun apply(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().applyService(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("지원 성공", "${response.code()}")
                        _isSuccessful.value = true
                    } else {
                        Log.d("지원 실패", "${response.code()}")
                        _isSuccessful.value = false
                    }

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("지원 실패", "${t.message}")
                    _isSuccessful.value = false
                }

            })
    }

    fun cancelApply(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().cancelApplyService(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("지원 취소 성공", "${response.code()}")
                        _isSuccessful.value = true
                    } else {
                        Log.d("지원 취소 실패", "${response.code()}")
                        _isSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("지원 취소 실패", "${t.message}")
                    _isSuccessful.value = false
                }

            })
    }

    fun scrap(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().postScrap(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("스크랩 성공", "${response.code()}")
                        _isSuccessful.value = true
                    } else {
                        Log.d("스크랩랩 실패", "${response.code()}")
                        _isSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("스크랩 실패", "${t.message}")
                    _isSuccessful.value = false
                }

            })
    }

    fun scrapCancel(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().cancelScrap(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("스크랩 취소 성공", "${response.code()}")
                        _isSuccessful.value = true
                    } else {
                        Log.d("스크랩랩 실패", "${response.code()}")
                            _isSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("스크랩 실패", "${t.message}")
                    _isSuccessful.value = false
                }
            })
    }

    fun getProfile(token: String, userId: Int) {
        RetrofitClient.createRetorfitClient().getUserProfile(token, userId.toLong())
            .enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            _profile.value = result!!
                            Log.d("프로필 결과", "${_profile.value}")
                            _isSuccessful.value = true
                            _rating.value = result.avgRate.toString()
                            _recruited.value = result.appliedWork.toString()
                            _applied.value=  result.appliedWork.toString()
                            _completed.value = result.participatedWork.toString()
                            _profileUrl.value = result.profileUrl.toString()
                        } else {
                            _isSuccessful.value = false
                        }

                    } else {
                        _isSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    Log.d("프로필 불러오기 실패", "${t.message}")
                    _isSuccessful.value = false
                }

            })
    }

    fun postLike(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().postLike(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("좋아요 성공", "${response.code()}")
                        _isSuccessful.value = true
                    } else {
                        Log.d("좋아요 실패", "${response.code()}")
                        _isSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("좋아요 실패", "${t.message}")
                }
            })
    }

    fun cancelLike(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().cancelLike(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("좋아요 취소 성공", "${response.code()}")
                        _isSuccessful.value = true
                    } else {
                        Log.d("좋아요 취소 실패", "${response.code()}")
                        _isSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("좋아요 취소 실패", "${t.message}")
                }
            })
    }

}