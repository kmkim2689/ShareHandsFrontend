package com.sharehands.sharehands_frontend.view

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.sharehands.sharehands_frontend.R

class ProgressDialog(context: Context, private val string: String): Dialog(context) {
    init {
        // 다이얼 로그 제목을 안보이게...
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.fragment_progress)
        val text = findViewById<TextView>(R.id.loading_text)
        text.text = string
    }
}