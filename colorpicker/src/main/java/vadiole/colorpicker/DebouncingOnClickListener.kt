package vadiole.colorpicker

import android.view.View
import java.util.concurrent.atomic.AtomicBoolean

internal class DebouncingOnClickListener(
    private val intervalMillis: Long,
    private val click: ((View) -> Unit),
) : View.OnClickListener {

    override fun onClick(v: View) {
        if (enabled.getAndSet(false)) {
            v.postDelayed(ENABLE_AGAIN, intervalMillis)
            click(v)
        }
    }

    companion object {
        @JvmStatic
        var enabled = AtomicBoolean(true)
        private val ENABLE_AGAIN = { enabled.set(true) }
    }
}
