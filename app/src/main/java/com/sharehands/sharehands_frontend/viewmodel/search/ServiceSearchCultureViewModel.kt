package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.SearchResult
import com.sharehands.sharehands_frontend.network.search.ServiceList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceSearchCultureViewModel: ViewModel() {
    var _isInitialized = MutableLiveData<Boolean>(true)

    var _sort = MutableLiveData<Int>(1)

    private var _serviceSum = MutableLiveData<Int>()
    val serviceSum: LiveData<Int>
        get() = _serviceSum

    private var _searchResult = MutableLiveData<SearchResult?>()
    val searchResult: LiveData<SearchResult?>
        get() = _searchResult

    // 괄호 안에 초기화를 해야 리스트에 추가 시 오류가 안나옴
    var _servicesList = MutableLiveData<ArrayList<ServiceList>>(ArrayList())
    val servicesList: LiveData<ArrayList<ServiceList>>
        get() = _servicesList

    private var _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private var _isScrollSuccessful = MutableLiveData<Boolean>()
    val isScrollSuccessful: LiveData<Boolean>
        get() = _isScrollSuccessful

    private var _isApplySuccessful = MutableLiveData<Boolean>()
    val isApplySuccessful: LiveData<Boolean>
        get() = _isApplySuccessful

    private var _isCancelSuccessful = MutableLiveData<Boolean>()
    val isCancelSuccessful: LiveData<Boolean>
        get() = _isCancelSuccessful

    private var _isClickSuccessful = MutableLiveData<Boolean>()
    val isClickSuccessful: LiveData<Boolean>
        get() = _isClickSuccessful

    private var _searchKeyword = MutableLiveData<String>()
    val searchKeyword: LiveData<String>
        get() = _searchKeyword

    private var _count = MutableLiveData<Int>(0)
    val count: LiveData<Int>
        get() = _count


    // 검색 키워드
    fun onSearchKeywordChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.d("changed text", "${s}")
        Log.d("keyword text count", "${count}")
        if (s.length == 0) {
            _searchKeyword.value = ""
        } else {
            _searchKeyword.value = s.toString()
        }
    }

    fun searchKeyword(): Boolean {
        try {
            return searchKeyword.value?.length!! >= 2
        } catch (e: NullPointerException) {
            return false
        }

    }

    // 네트워크 통신
    fun loadServices(token: String, category: Int, sort: Int, page: Int) {

        if (token != "null") {
            if (page == 1) {
                RetrofitClient.createRetorfitClient().getServicesInitial(token, category, sort)
                    .enqueue(object : Callback<SearchResult> {
                        override fun onResponse(
                            call: Call<SearchResult>,
                            response: Response<SearchResult>
                        ) {
                            Log.d("봉사활동 목록 GET response code", "${response.code()}")
                            if (response.code() == 200) {
                                val result = response.body()
                                if (result != null) {
                                    _searchResult.value = result
                                    _serviceSum.value = result.workCounter.toInt()
                                    if (searchResult?.value?.serviceList!!.isNotEmpty()) {
                                        Log.d("service cnt once", "${_searchResult.value!!.serviceList.size}")
                                        Log.d("service scroll once", "${_searchResult.value!!.serviceList.last().workId}")

                                        for (elem in result.serviceList) {
                                            _servicesList.value!!.add(elem)
                                        }
                                        Log.d("service count ${page}", "${result.serviceList.size}")

                                        Log.d("봉사활동 목록 GET 성공", "${result}")
                                        Log.d("봉사활동 전체 목록", "${servicesList.value}")
                                    }

                                    _isSuccessful.value = true
                                } else {
                                    Log.d("봉사활동 목록 GET 실패", "데이터 비어있음")
                                    _isSuccessful.value = false
                                }
                            } else {
                                Log.d("봉사활동 목록 GET 실패", "${response.code()}")
                                _isSuccessful.value = false
                                // TODO 더미데이터 코드 제거할것
//                                _servicesList.value!!.apply {
//                                    add(ServiceList(1, "https://tago.kr/images/sub/TG300-D02_img01.png", "https://image.ajunews.com//content/image/2021/09/13/20210913143050976154.jpg",
//                                        "케이엠", true, "봉사갑시다", "동대문구", "10명", "2022. 01. 01 ~ 2022. 03. 03", "화, 목"))
//                                    add(ServiceList(2, "https://tago.kr/images/sub/TG300-D02_img01.png", "https://image.ajunews.com//content/image/2021/09/13/20210913143050976154.jpg",
//                                        "케이엔", true, "봉사2", "동대문구", "10명", "2022. 01. 01 ~ 2022. 03. 03", "화, 목"))
//                                }
                            }
                        }

                        override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                            Log.d("봉사활동 목록 GET 실패", "${t.message}")
                            _isSuccessful.value = false
                        }

                    })


            } else {
                Log.d("last id", "${servicesList.value?.get(servicesList.value!!.size - 1)!!.workId.toInt()}")
                RetrofitClient.createRetorfitClient().getServicesAdditional(
                    token,
                    category,
                    sort,
                    servicesList.value?.get(servicesList.value!!.size - 1)!!.workId.toInt()
                ).enqueue(object : Callback<SearchResult> {
                    override fun onResponse(
                        call: Call<SearchResult>,
                        response: Response<SearchResult>
                    ) {
                        Log.d("봉사활동 목록 추가 GET response code", "${response.code()}")
                        if (response.code() == 200) {
                            val result = response.body()
                            if (result != null) {
                                Log.d("service cnt scrolled", "${_searchResult.value!!.serviceList.size}")
                                Log.d("service scroll last", "${_searchResult.value!!.serviceList.last().workId}")
                                _serviceSum.value = result.workCounter.toInt()
                                for (elem in result.serviceList) {
                                    _servicesList.value!!.add(elem)
                                }
                                Log.d("service count ${page}", "${result.serviceList.size}")
                                Log.d("봉사활동 목록 GET 성공", "${result}")
                                Log.d("봉사활동 전체 목록", "${servicesList.value}")
                                _isScrollSuccessful.value = true
                            } else {
                                Log.d("봉사뢀동 목록 GET 실패", "데이터 비어있음")
                                _isScrollSuccessful.value = false
                            }
                        } else {
                            Log.d("봉사활동 목록 GET 실패", "${response.code()}")
                            _isScrollSuccessful.value = false
                        }
                    }

                    override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                        Log.d("봉사활동 추가 목록 GET 실패", "${t.message}")
                        _isScrollSuccessful.value = false
                    }

                })
            }
        } else {
            Log.d("봉사활동 목록 GET 실패", "no token")
            _isSuccessful.value = false

        }
    }

    fun applyService(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().applyService(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("지원 성공", "${response.code()}")
                        _isApplySuccessful.value = true
                    } else {
                        Log.d("지원 실패", "${response.code()}")
                        _isApplySuccessful.value = false
                    }

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("지원 실패", "${t.message}")
                    _isApplySuccessful.value = false
                }

            })

    }

    fun cancelService(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().cancelApplyService(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("지원 취소 성공", "${response.code()}")
                        _isCancelSuccessful.value = true
                    } else {
                        Log.d("지원 취소 실패", "${response.code()}")
                        _isCancelSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("지원 취소 실패", "${t.message}")
                    _isCancelSuccessful.value = false
                }

            })
    }
}