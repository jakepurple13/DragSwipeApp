package com.programmerbox.dragswipe

import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

@DslMarker
annotation class DragSwipeMarker

@DslMarker
annotation class DragActionMarker

@DragSwipeMarker
class DragSwipeBuilder<T, VH : RecyclerView.ViewHolder> internal constructor() {

    @DragSwipeMarker
    var adapter: DragSwipeAdapter<T, VH> by Delegates.notNull()

    @DragSwipeMarker
    var recyclerView: RecyclerView by Delegates.notNull()

    private var dragDirections: Array<out Direction> = arrayOf()

    @DragSwipeMarker
    fun dragDirections(vararg directions: Direction) {
        dragDirections = directions
    }

    private var swipeDirections: Array<out Direction> = arrayOf()

    @DragSwipeMarker
    fun swipeDirections(vararg directions: Direction) {
        swipeDirections = directions
    }

    private var actions: DragSwipeActions<T, VH>? = null

    @DragSwipeMarker
    fun dragSwipeActions(dragSwipeActions: DragSwipeActionBuilder<T, VH>.() -> Unit) {
        actions = DragSwipeActionBuilder<T, VH>().apply(dragSwipeActions).build()
    }

    private fun Array<out Direction>.getItems(): Int {
        var count = this[0].value
        drop(0).forEach { count += it }
        return count
    }

    internal fun build() = DragSwipeUtils.setDragSwipeUp(
        adapter,
        recyclerView,
        dragDirections.getItems(),
        swipeDirections.getItems(),
        actions
    )

    @DragSwipeMarker
    class DragSwipeActionBuilder<T, VH : RecyclerView.ViewHolder> {

        private var moveOn: DragSwipeActions<T, VH>.(RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder, DragSwipeAdapter<T, VH>) -> Unit =
            { r, v, t, d -> onMove(r, v, t, d) }

        @DragActionMarker
        fun onMove(block: DragSwipeActions<T, VH>.(RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder, DragSwipeAdapter<T, VH>) -> Unit) {
            moveOn = block
        }

        private var swipeOn: DragSwipeActions<T, VH>.(RecyclerView.ViewHolder, Direction, DragSwipeAdapter<T, VH>) -> Unit =
            { r, d, d1 -> onSwiped(r, d, d1) }

        @DragActionMarker
        fun onSwipe(block: DragSwipeActions<T, VH>.(RecyclerView.ViewHolder, Direction, DragSwipeAdapter<T, VH>) -> Unit) {
            swipeOn = block
        }

        private var movementFlags: DragSwipeActions<T, VH>.(RecyclerView, RecyclerView.ViewHolder) -> Int? = { r, v -> getMovementFlags(r, v) }

        @DragActionMarker
        fun getMovementFlags(block: DragSwipeActions<T, VH>.(RecyclerView, RecyclerView.ViewHolder) -> Int?) {
            movementFlags = block
        }

        @DragActionMarker
        var isItemViewSwipeEnabled: Boolean = true

        @DragActionMarker
        var isLongPressDragEnabled: Boolean = true

        fun build(): DragSwipeActions<T, VH> = object : DragSwipeActions<T, VH> {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
                dragSwipeAdapter: DragSwipeAdapter<T, VH>
            ) = moveOn(recyclerView, viewHolder, target, dragSwipeAdapter)

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Direction, dragSwipeAdapter: DragSwipeAdapter<T, VH>) =
                swipeOn(viewHolder, direction, dragSwipeAdapter)

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int? =
                movementFlags(recyclerView, viewHolder)

            override fun isItemViewSwipeEnabled(): Boolean = isItemViewSwipeEnabled
            override fun isLongPressDragEnabled(): Boolean = isLongPressDragEnabled
        }
    }

    companion object {
        @DragSwipeMarker
        operator fun <T, VH : RecyclerView.ViewHolder> invoke(block: DragSwipeBuilder<T, VH>.() -> Unit) =
            DragSwipeBuilder<T, VH>().apply(block).build()
    }

}

operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeBuilder.Companion.invoke(
    adapter: DragSwipeAdapter<T, VH>,
    block: DragSwipeBuilder<T, VH>.() -> Unit
) = DragSwipeBuilder<T, VH>().apply { this.adapter = adapter }.apply(block).build()

operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeBuilder.Companion.invoke(
    recyclerView: RecyclerView,
    block: DragSwipeBuilder<T, VH>.() -> Unit
) = DragSwipeBuilder<T, VH>().apply { this.recyclerView = recyclerView }.apply(block).build()

fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.buildDragSwipe(
    block: DragSwipeBuilder<T, VH>.() -> Unit
) = DragSwipeBuilder(this, block)

fun <T, VH : RecyclerView.ViewHolder> RecyclerView.buildDragSwipe(
    block: DragSwipeBuilder<T, VH>.() -> Unit
) = DragSwipeBuilder(this, block)