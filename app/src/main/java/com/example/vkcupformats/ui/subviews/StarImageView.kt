package com.example.vkcupformats.ui.subviews

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import com.example.vkcupformats.R

class StarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle) {

    private val imgFilled: ImageView by lazy { ImageView(context) }
    private val imgEmpty: ImageView by lazy { ImageView(context) }
    private val MAX_LEVEL = 10000

    init {
        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
        ).apply {
            addRule(CENTER_IN_PARENT)
        }

        addView(imgEmpty, params)
        addView(imgFilled, params)
    }

    fun setProgress(progress: Float) {
        val fixedProgress = if (progress > 1f) {
            1f
        } else if (progress < 0f) {
            0f
        } else {
            progress
        }
        val levelFilled = (MAX_LEVEL * fixedProgress).toInt()
        val levelEmpty = MAX_LEVEL - levelFilled
        imgFilled.setImageLevel(levelFilled)
        imgEmpty.setImageLevel(levelEmpty)
    }

    fun setEmptyStarColor(color: Int) {
        val drawableOriginal =
            requireNotNull(
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_star_faw_filled
                )
                    ?.constantState
                    ?.newDrawable()
            )
        val clipDrawable = ClipDrawable(
            drawableOriginal,
            Gravity.END,
            ClipDrawable.HORIZONTAL
        )
        imgEmpty.setImageDrawable(clipDrawable)
        imgEmpty.setColorFilter(color)
    }

    fun setFilledColors(colorFrom: Int, colorTo: Int) {
        val gradientStarDrawable = generateDrawable(colorFrom, colorTo)
        imgFilled.setImageDrawable(gradientStarDrawable)
    }

    private fun generateDrawable(colorStart: Int, colorEnd: Int): Drawable {
        val original = BitmapFactory.decodeResource(this.resources, R.drawable.ic_star_faw_filled)
        val coloredBitmap = addGradient(original, colorStart, colorEnd)
        val coloredDrawable = BitmapDrawable(resources, coloredBitmap)
        val coloredClippedDrawable =
            ClipDrawable(
                coloredDrawable.constantState!!.newDrawable(),
                Gravity.START,
                ClipDrawable.HORIZONTAL
            )
        return coloredClippedDrawable
    }

    fun addGradient(originalBitmap: Bitmap, colorFrom: Int, colorTo: Int): Bitmap? {
        val width = originalBitmap.width
        val height = originalBitmap.height
        val updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(updatedBitmap)
        canvas.drawBitmap(originalBitmap, 0.toFloat(), 0.toFloat(), null)
        val paint = Paint()
        val shader = LinearGradient(
            0f,
            0f,
            width.toFloat(),
            0f,
            colorFrom,
            colorTo,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawRect(0.toFloat(), 0.toFloat(), width.toFloat(), height.toFloat(), paint)
        return updatedBitmap
    }
}