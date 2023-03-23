package com.sharehands.sharehands_frontend.view.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityServiceDetailBinding

class ReviewDetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityServiceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)
        binding = DataBindingUtil.setContentView<ActivityServiceDetailBinding>(this, R.layout.activity_review_detail)
    }
}