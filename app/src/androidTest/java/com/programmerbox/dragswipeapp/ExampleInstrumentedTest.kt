package com.programmerbox.dragswipeapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.programmerbox.dragswipe.DragSwipeBuilder
import com.programmerbox.dragswipe.buildDragSwipe
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.programmerbox.dragswipeapp", appContext.packageName)

        val f = DragSwipeBuilder<Int, ViewHolder> {
            dragSwipeActions {
                onMove { recyclerView, viewHolder, viewHolder2, dragSwipeAdapter -> }
                onSwipe { viewHolder, direction, dragSwipeAdapter -> }
                isItemViewSwipeEnabled = true
                isLongPressDragEnabled = true
                getMovementFlags { recyclerView, viewHolder -> null }
            }
            dragDirections()
            swipeDirections()
        }
        RecyclerView(appContext).buildDragSwipe<Int, ViewHolder> { }
        DSAdapter(arrayListOf("asdf"), appContext).buildDragSwipe { }
    }
}
