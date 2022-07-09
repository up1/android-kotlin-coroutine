package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.ui.longrunning.LongRunningActivity
import com.example.myapplication.ui.single.SingleActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startSingleCall(view: View) {
        startActivity(Intent(this, SingleActivity::class.java))
    }

    fun startLongRunning(view: View) {
        startActivity(Intent(this, LongRunningActivity::class.java))
    }
}