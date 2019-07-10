package com.programmerbox.dragswipeapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.programmerbox.dragswipe.Direction
import com.programmerbox.dragswipe.DragSwipeUtils
import kotlinx.android.synthetic.main.activity_simple.*

class SimpleActivity : AppCompatActivity() {

    private lateinit var adapter: DSAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        val list = arrayListOf<String>()

        for (i in 0..20) {
            list += "$i Hello World"
        }

        simple_rv.setHasFixedSize(true)
        simple_rv.layoutManager = LinearLayoutManager(this)
        simple_rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        adapter = DSAdapter(list, this@SimpleActivity)

        simple_rv.adapter = adapter

        DragSwipeUtils.setDragSwipeUp(
            adapter,
            simple_rv,
            Direction.UP + Direction.DOWN,
            Direction.START + Direction.END
        )

        enable_disable.visibility = View.INVISIBLE

        add_button.setOnClickListener {
            adapter.addItem("Hello!!!")
        }

        delete_button.setOnClickListener {
            try {
                adapter.removeItem(0)
            } catch(ignored: IndexOutOfBoundsException) {}
        }

    }
}
