package com.example.projekt

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class StarRatingView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val starDrawable = ContextCompat.getDrawable(context, R.drawable.star_outline) // Replace with your star drawable resource
    private val filledStarDrawable = ContextCompat.getDrawable(context, R.drawable.star_filled) // Replace with your filled star drawable resource

    var rating: Int = 0
        set(value) {
            field = value
            updateStars()
        }

    init {
        orientation = HORIZONTAL
         val starLayoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        starLayoutParams.setMargins(0, 0, 8.dpToPx(), 0) // Adjust margin as needed

        for (i in 0 until 10) { // Add 10 stars
            val starImageView = ImageView(context)
            starImageView.layoutParams = starLayoutParams
            starImageView.setImageDrawable(if (i < rating) filledStarDrawable else starDrawable)
            addView(starImageView)
        }
    }

    private fun updateStars() {
        for (i in 0 until childCount) {
            val starImageView = getChildAt(i) as ImageView
            starImageView.setImageDrawable(if (i < rating) filledStarDrawable else starDrawable)
        }
    }

    // Extension function to convert dp to px
    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
}
