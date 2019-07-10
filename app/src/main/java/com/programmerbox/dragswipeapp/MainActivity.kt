package com.programmerbox.dragswipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simple.setOnClickListener {
            startActivity(Intent(this@MainActivity, SimpleActivity::class.java))
        }

        custom_view.setOnClickListener {
            startActivity(Intent(this@MainActivity, CustomViewTouchActivity::class.java))
        }

        custom_helper.setOnClickListener {
            startActivity(Intent(this@MainActivity, CustomHelperActivity::class.java))
        }

        movement_flag.setOnClickListener {
            startActivity(Intent(this@MainActivity, MovementFlagActivity::class.java))
        }

    }

}
