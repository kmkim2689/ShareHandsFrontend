package com.sharehands.sharehands_frontend.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // TODO : Set Base URL after Decided
    private const val BASE_URL = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 여기서 상속을 받는 ApiService 인터페이스는, get, post 등 서버와 통신하기 위한 메소드들을 정의해놓은 인터페이스를 의미한다.
    fun createRetorfitClient(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // TODO : 클라이언트를 사용하는 클래스에서, 다음과 같이 사용
    // private val retrofitClient = RetrofitClient.createRetrofitClient()
}