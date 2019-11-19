package com.programmerbox.dragswipeex

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.programmerbox.dragswipe.*
import java.util.*
import kotlin.random.Random

/**
 * Shuffles items in the adapter and notifies
 */
fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.shuffleItems() {
    for (i in list.indices) {
        val num = Random.nextInt(0, list.size - 1)
        Collections.swap(list, i, num)
        notifyItemMoved(i, num)
        notifyItemChanged(i)
        notifyItemChanged(num)
    }
}

/**
 * gets the first item in [DragSwipeAdapter.list]
 */
fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.getFirstItem(): T? = list.firstOrNull()

/**
 * gets the middle item in [DragSwipeAdapter.list]
 */
fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.getMiddleItem(): T? = list.getOrNull(itemCount / 2)

/**
 * gets the last item in [DragSwipeAdapter.list]
 */
fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.getLastItem(): T? = list.lastOrNull()

/**
 * @see ItemTouchHelper.Callback.makeFlag
 */
@Suppress("unused")
fun <T, VH : RecyclerView.ViewHolder> DragSwipeActions<T, VH>.makeFlag(
    state: Int,
    direction: Int
): Int = ItemTouchHelper.Callback.makeFlag(state, direction)

/**
 * gets the [num] item from [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.get(num: Int): T = list[num]

/**
 * Gets the location of [element] in [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.get(element: T): Int = list.indexOf(element)

/**
 * sets the [num] location of [DragSwipeAdapter.list] to [element]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.set(num: Int, element: T) {
    list[num] = element
    notifyItemChanged(num)
}

/**
 * sets the [num] locations of [DragSwipeAdapter.list] to [element]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.set(num: IntRange, element: List<T>) {
    for ((i, j) in num.withIndex()) {
        list[j] = element[i]
    }
    notifyItemRangeChanged(num.first, num.count())
}

/**
 * adds a list of [elements] to [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.plusAssign(elements: List<T>) = addItems(elements)

/**
 * adds an [element] to [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.plusAssign(element: T) = addItem(element)

/**
 * removes a list of [element] from [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.minusAssign(element: List<T>) {
    val intList = arrayListOf<Int>()
    for (i in list.withIndex())
        if (i == element)
            intList += i.index
    for (i in intList)
        removeItem(i)
}

/**
 * removes [element] from [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.minusAssign(element: T) {
    removeItem(list.indexOf(element))
}

/**
 * checks if [element] is in [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.contains(element: T): Boolean = element in list

/**
 * allows iteration of [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.iterator() = list.iterator()

/**
 * @see [DragSwipeUtils.enableDragSwipe]
 */
fun RecyclerView.attachDragSwipeHelper(dragSwipeHelper: DragSwipeHelper) = DragSwipeUtils.enableDragSwipe(dragSwipeHelper, this)

/**
 * @see [DragSwipeUtils.disableDragSwipe]
 */
fun RecyclerView.removeDragSwipeHelper(dragSwipeHelper: DragSwipeHelper) = DragSwipeUtils.disableDragSwipe(dragSwipeHelper)

/**
 * A nice little manager that will take care of enabling and disabling dragswipe actions dynamically
 *
 * @param recyclerView the recyclerView you want to be attached to
 * @param dragSwipeHelper the helper you want to use on the [recyclerView]
 */
class RecyclerViewDragSwipeManager(private val recyclerView: RecyclerView, dragSwipeHelper: DragSwipeHelper? = null) {

    /**
     * The helper! Set this in order to use this this class well!
     */
    var dragSwipeHelper: DragSwipeHelper? = dragSwipeHelper
        set(value) {
            if (field != null) {
                disableDragSwipe()
            }
            field = value
            setDragSwipe()
        }

    /**
     * if true, it will enable DragSwipe
     * if false, it will disable DragSwipe
     */
    var dragSwipedEnabled: Boolean = true
        set(value) {
            field = value
            setDragSwipe()
        }

    private fun setDragSwipe() {
        dragSwipeHelper?.let {
            when (dragSwipedEnabled) {
                true -> enableDragSwipe()
                false -> disableDragSwipe()
            }
        }
    }

    private fun disableDragSwipe() = DragSwipeUtils.disableDragSwipe(dragSwipeHelper!!)
    private fun enableDragSwipe() = DragSwipeUtils.enableDragSwipe(dragSwipeHelper!!, recyclerView)
}

/**
 * @see DragSwipeUtils.setDragSwipeUp
 */
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.setDragSwipeUp(
    dragSwipeAdapter: DragSwipeAdapter<T, VH>,
    dragDirs: Int = Direction.NOTHING.value,
    swipeDirs: Int = Direction.NOTHING.value,
    dragSwipeActions: DragSwipeActions<T, VH>? = null
): DragSwipeHelper {
    check(adapter is DragSwipeAdapter<*, *>) { throw IllegalStateException("Adapter is not a DragSwipeAdapter") }
    return DragSwipeUtils.setDragSwipeUp(dragSwipeAdapter, this, dragDirs, swipeDirs, dragSwipeActions)
}

/**
 * @see DragSwipeUtils.setDragSwipeUp
 */
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.setDragSwipeUp(
    dragDirs: Int = Direction.NOTHING.value,
    swipeDirs: Int = Direction.NOTHING.value,
    dragSwipeActions: DragSwipeActions<T, VH>? = null
): DragSwipeHelper {
    check(adapter is DragSwipeAdapter<*, *> && adapter != null) { throw IllegalStateException("Adapter is not a DragSwipeAdapter") }
    return DragSwipeUtils.setDragSwipeUp(adapter as DragSwipeAdapter<T, VH>, this, dragDirs, swipeDirs, dragSwipeActions)
}

/**
 * @see DragSwipeUtils.setDragSwipeUp
 */
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.setDragSwipeUp(
    callback: DragSwipeManageAdapter<T, VH>,
    dragSwipeActions: DragSwipeActions<T, VH>? = null
): DragSwipeHelper {
    check(adapter is DragSwipeAdapter<*, *>) { throw IllegalStateException("Adapter is not a DragSwipeAdapter") }
    return DragSwipeUtils.setDragSwipeUp(this, callback, dragSwipeActions)
}

/**
 * @see DragSwipeUtils.enableDragSwipe
 */
fun RecyclerView.enableDragSwipe(helper: DragSwipeHelper) = DragSwipeUtils.enableDragSwipe(helper, this)

/**
 * @see DragSwipeUtils.disableDragSwipe
 */
fun RecyclerView.disableDragSwipe(helper: DragSwipeHelper) = DragSwipeUtils.disableDragSwipe(helper)

/**
 * A DSL builder for [DragSwipeUtils.setDragSwipeUp]
 */
class DragSwipeBuilder<T, VH : RecyclerView.ViewHolder> private constructor(
    val recyclerView: RecyclerView,
    val dragSwipeAdapter: DragSwipeAdapter<T, VH> = recyclerView.adapter as DragSwipeAdapter<T, VH>
) {
    /**
     * The directions you want to drag
     */
    var dragDirs: Int = Direction.NOTHING.value
    /**
     * The directions you want to swipe
     */
    var swipeDirs: Int = Direction.NOTHING.value
    /**
     * Default DragSwipe actions
     */
    val defaultActions: DragSwipeActions<T, VH> = object : DragSwipeActions<T, VH> {}
    private var dragSwipeActions: DragSwipeActions<T, VH> = object : DragSwipeActions<T, VH> {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
            dragSwipeAdapter: DragSwipeAdapter<T, VH>
        ) =
            onMoving(recyclerView, viewHolder, target, dragSwipeAdapter)

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Direction, dragSwipeAdapter: DragSwipeAdapter<T, VH>) =
            onSwiping(viewHolder, direction, dragSwipeAdapter)
    }
    private var onMoving: (RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder, DragSwipeAdapter<T, VH>) -> Unit =
        { r, v1, v2, a -> defaultActions.onMove(r, v1, v2, a) }
    private var onSwiping: (RecyclerView.ViewHolder, Direction, DragSwipeAdapter<T, VH>) -> Unit = { v, d, a -> defaultActions.onSwiped(v, d, a) }

    /**
     * What do you want to happen on move
     * Default is the default [DragSwipeActions.onMove]
     */
    fun onMove(block: (RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder, DragSwipeAdapter<T, VH>) -> Unit) {
        onMoving = block
    }

    /**
     * What do you want to happen on move
     * Default is the default [DragSwipeActions.onSwiped]
     */
    fun onSwipe(block: (RecyclerView.ViewHolder, Direction, DragSwipeAdapter<T, VH>) -> Unit) {
        onSwiping = block
    }

    private fun build(): DragSwipeHelper = DragSwipeUtils.setDragSwipeUp(dragSwipeAdapter, recyclerView, dragDirs, swipeDirs, dragSwipeActions)

    companion object {
        fun <T, VH : RecyclerView.ViewHolder> dragSwipeBuilder(
            recyclerView: RecyclerView,
            dragSwipeAdapter: DragSwipeAdapter<T, VH>,
            block: DragSwipeBuilder<T, VH>.() -> Unit
        ) =
            DragSwipeBuilder(recyclerView, dragSwipeAdapter).apply(block).build()
    }
}
