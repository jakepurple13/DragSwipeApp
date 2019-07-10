package com.programmerbox.dragswipeapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.programmerbox.dragswipe.*
import kotlinx.android.synthetic.main.activity_simple.*

class CustomViewTouchActivity : AppCompatActivity() {

    private lateinit var helper: DragSwipeHelper

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

        val adapter = DSAdapter(list, this@CustomViewTouchActivity)

        simple_rv.adapter = adapter

        helper = DragSwipeUtils.setDragSwipeUp(
            adapter,
            simple_rv,
            Direction.UP or Direction.DOWN,
            Direction.START + Direction.END,
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

                override fun isItemViewSwipeEnabled(): Boolean {
                    return false
                }

                override fun isLongPressDragEnabled(): Boolean = false
            }
        )

        adapter.helper = helper

        enable_disable.setOnCheckedChangeListener { _, b ->
            adapter.helper = if (b) {
                DragSwipeUtils.enableDragSwipe(helper, simple_rv)
                helper
            } else {
                DragSwipeUtils.disableDragSwipe(helper)
                null
            }
            adapter.notifyDataSetChanged()
        }
    }
}