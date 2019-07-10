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
fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.getFirstItem(): T {
    return list.first()
}

/**
 * gets the middle item in [DragSwipeAdapter.list]
 */
fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.getMiddleItem(): T {
    return list[itemCount / 2]
}

/**
 * gets the last item in [DragSwipeAdapter.list]
 */
fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.getLastItem(): T {
    return list.last()
}

/**
 * @see ItemTouchHelper.Callback.makeFlag
 */
@Suppress("unused")
fun <T, VH : RecyclerView.ViewHolder> DragSwipeActions<T, VH>.makeFlag(
    state: Int,
    direction: Int
): Int {
    return ItemTouchHelper.Callback.makeFlag(state, direction)
}

/**
 * gets the [num] item from [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.get(num: Int): T = list[num]

/**
 * Gets the location of [element] in [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.get(element: T): Int =
    list.indexOf(element)

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
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.set(
    num: IntRange,
    element: List<T>
) {
    for ((i, j) in num.withIndex()) {
        list[j] = element[i]
    }
    notifyItemRangeChanged(num.first, num.count())
}

/**
 * adds a list of [elements] to [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.plusAssign(elements: List<T>) {
    addItems(elements)
}

/**
 * adds an [element] to [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.plusAssign(element: T) {
    addItem(element)
}

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
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.contains(element: T): Boolean {
    return element in list
}

/**
 * allows iteration of [DragSwipeAdapter.list]
 */
operator fun <T, VH : RecyclerView.ViewHolder> DragSwipeAdapter<T, VH>.iterator() = list.iterator()

/**
 * @see [DragSwipeUtils.enableDragSwipe]
 */
fun RecyclerView.attachDragSwipeHelper(dragSwipeHelper: DragSwipeHelper) {
    DragSwipeUtils.enableDragSwipe(dragSwipeHelper, this)
}

/**
 * @see [DragSwipeUtils.disableDragSwipe]
 */
@Suppress("unused")
fun RecyclerView.removeDragSwipeHelper(dragSwipeHelper: DragSwipeHelper) {
    DragSwipeUtils.disableDragSwipe(dragSwipeHelper)
}

/**
 * A nice little manager that will take care of enabling and disabling dragswipe actions dynamically
 *
 * @param recyclerView the recyclerView you want to be attached to
 * @param dragSwipeHelper the helper you want to use on the [recyclerView]
 */
class RecyclerViewDragSwipeManager(
    private val recyclerView: RecyclerView,
    dragSwipeHelper: DragSwipeHelper? = null
) {

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
        if (dragSwipeHelper != null) {
            when (dragSwipedEnabled) {
                true -> enableDragSwipe()
                false -> disableDragSwipe()
            }
        }
    }

    private fun disableDragSwipe() {
        DragSwipeUtils.disableDragSwipe(dragSwipeHelper!!)
    }

    private fun enableDragSwipe() {
        DragSwipeUtils.enableDragSwipe(dragSwipeHelper!!, recyclerView)
    }
}
