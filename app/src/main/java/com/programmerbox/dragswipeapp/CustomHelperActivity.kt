package com.programmerbox.dragswipeapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.programmerbox.dragswipe.Direction
import com.programmerbox.dragswipe.DragSwipeActions
import com.programmerbox.dragswipe.DragSwipeAdapter
import com.programmerbox.dragswipe.DragSwipeUtils
import kotlinx.android.synthetic.main.activity_simple.*

class CustomHelperActivity : AppCompatActivity() {

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

        adapter = DSAdapter(list, this@CustomHelperActivity)

        simple_rv.adapter = adapter

        val customSwipeToDelete = CustomSwipeToDelete(
            adapter,
            Direction.UP + Direction.DOWN,
            Direction.START + Direction.END,
            this@CustomHelperActivity
        )

        val helper = DragSwipeUtils.setDragSwipeUp(
            simple_rv,
            customSwipeToDelete,
            object : DragSwipeActions<String, ViewHolder> {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                    dragSwipeAdapter: DragSwipeAdapter<String, ViewHolder>
                ) {
                    super.onMove(recyclerView, viewHolder, target, dragSwipeAdapter)
                    Log.d(
                        "DragSwipe",
                        "Moved from ${viewHolder.adapterPosition} to ${target.adapterPosition}"
                    )
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Direction,
                    dragSwipeAdapter: DragSwipeAdapter<String, ViewHolder>
                ) {
                    super.onSwiped(viewHolder, direction, dragSwipeAdapter)
                    Log.d("DragSwipe", "Was swiped $direction")
                }
            }
        )

        enable_disable.setOnCheckedChangeListener { _, b ->
            if (b) {
                DragSwipeUtils.enableDragSwipe(helper, simple_rv)
            } else {
                DragSwipeUtils.disableDragSwipe(helper)
            }
        }

        add_button.setOnClickListener {
            adapter.addItem("Hello!!!")
        }

        delete_button.setOnClickListener {
            adapter.removeItem(0)
        }
    }
}