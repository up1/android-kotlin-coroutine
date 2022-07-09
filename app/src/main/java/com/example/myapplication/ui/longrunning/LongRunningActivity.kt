package com.example.myapplication.ui.longrunning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.R
import com.example.myapplication.data.api.ApiWithRetrofit
import com.example.myapplication.data.api.RetrofitBuilder
import com.example.myapplication.ui.single.SingleViewModel
import com.example.myapplication.util.Status
import kotlinx.android.synthetic.main.activity_long_running.*

class LongRunningActivity : AppCompatActivity() {
    private lateinit var viewModel: LongRunningTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_running)
        setupViewModel()
        setupLongRunningTask()
    }

    private fun setupLongRunningTask() {
        viewModel.getStatus().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    textView.text = it.data
                    textView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.startLongRunningTask()
    }

    private fun setupViewModel() {
        val factory = viewModelFactory {
            initializer {
                LongRunningTaskViewModel() }
        }
        viewModel = ViewModelProvider(this, factory)[LongRunningTaskViewModel::class.java]
    }
}