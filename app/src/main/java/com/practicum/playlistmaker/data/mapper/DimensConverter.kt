package com.practicum.playlistmaker.data.mapper

import android.util.TypedValue
import android.widget.ImageView

object DimensConverter {
    fun dpToPx(dp: Float, context: ImageView): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}