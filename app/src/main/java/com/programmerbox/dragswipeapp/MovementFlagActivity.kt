package com.programmerbox.dragswipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.programmerbox.dragswipe.Direction
import com.programmerbox.dragswipe.DragSwipeActions
import com.programmerbox.dragswipe.DragSwipeUtils
import com.programmerbox.dragswipe.*
import kotlinx.android.synthetic.main.activity_simple.*

class MovementFlagActivity : AppCompatActivity() {

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

        val adapter = DSAdapter(list, this@MovementFlagActivity)

        simple_rv.adapter = adapter

        val helper = DragSwipeUtils.setDragSwipeUp(
            adapter,
            simple_rv,
            Direction.UP + Direction.DOWN,
            Direction.START + Direction.END,
            object : DragSwipeActions<String, ViewHolder> {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int? {
                    return if (viewHolder.adapterPosition%10==0)
                        makeMovementFlags(Direction.NOTHING.value, Direction.START + Direction.END + Direction.UP + Direction.DOWN)
                    else
                        super.getMovementFlags(recyclerView, viewHolder)
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Direction,
                    dragSwipeAdapter: DragSwipeAdapter<String, ViewHolder>
                ) {
                    super.onSwiped(viewHolder, direction, dragSwipeAdapter)
                    dragSwipeAdapter.notifyItemRangeChanged(viewHolder.adapterPosition, dragSwipeAdapter.itemCount-viewHolder.adapterPosition)
                }
            }
        )

        enable_disable.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                DragSwipeUtils.enableDragSwipe(helper, simple_rv)
            } else {
                DragSwipeUtils.disableDragSwipe(helper)
            }
        }

    }

}