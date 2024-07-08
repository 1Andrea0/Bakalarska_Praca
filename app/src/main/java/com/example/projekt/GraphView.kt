package com.example.projekt

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class GraphView (context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var numVertices: Int = 3

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    private val redArrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val blueArrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val squareSize = 120f
    private val arrowHeadLength = 30f
    private val arrowAngle = Math.PI / 6

    var redArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,0))
    var blueArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,0))

    private var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.blchaa)
    private var resizedBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
    var bitmapX: Float = 0f
    var bitmapY: Float = 0f
    private var animateBitmap: Boolean = false

    private var vertexPoints = listOf<PointF>()

    fun setNumVertices(num: Int) {
        numVertices = num
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 4f // Distance from center to each square center
        val verticalOffset = ((0.4 * height) - (numVertices * squareSize)) / (numVertices - 1)  // Vertical offset for top and bottom vertices

        vertexPoints = when (numVertices) {
            3 -> drawTriangle(canvas, centerX, centerY, radius, squareSize, verticalOffset)
            4 -> drawSquare(canvas, centerX, centerY, radius, squareSize)
            5 -> drawPentagon(canvas, centerX, centerY, radius, squareSize)
            else -> listOf()
        }

        if (animateBitmap) {
            canvas.drawBitmap(resizedBitmap, bitmapX, bitmapY, null)
        }
    }

    private fun drawTriangle(canvas: Canvas, centerX: Float, centerY: Float, radius: Float, squareSize: Float, verticalOffset: Double): List<PointF> {
        val points = listOf(
            PointF(centerX, (centerY - radius - verticalOffset).toFloat()),  // Top
            PointF(centerX - radius * sin(Math.PI / 3).toFloat(), (centerY + radius / 2 + verticalOffset).toFloat()),  // Bottom left
            PointF(centerX + radius * sin(Math.PI / 3).toFloat(), (centerY + radius / 2 + verticalOffset).toFloat())   // Bottom right
        )
        drawPolygon(canvas, points, squareSize)
        return points
    }

    private fun drawSquare(canvas: Canvas, centerX: Float, centerY: Float, radius: Float, squareSize: Float): List<PointF> {
        val points = listOf(
            PointF(centerX - radius, centerY - radius),
            PointF(centerX + radius, centerY - radius),
            PointF(centerX - radius, centerY + radius),
            PointF(centerX + radius, centerY + radius)
        )
        drawPolygon(canvas, points, squareSize)
        return points
    }

    private fun drawPentagon(canvas: Canvas, centerX: Float, centerY: Float, radius: Float, squareSize: Float): List<PointF> {
        val angle = 2 * Math.PI / 5
        val points = List(5) { i ->
            PointF(
                (centerX + radius * cos(i * angle)).toFloat(),
                (centerY + radius * sin(i * angle)).toFloat()
            )
        }
        drawPolygon(canvas, points, squareSize)
        return points
    }

    private fun drawPolygon(canvas: Canvas, points: List<PointF>, squareSize: Float) {
        points.forEachIndexed { index, point ->
            canvas.drawRect(
                point.x - squareSize / 2, point.y - squareSize / 2,
                point.x + squareSize / 2, point.y + squareSize / 2, rectPaint
            )
            val label = when (index) {
                0 -> "A"
                1 -> "B"
                2 -> "C"
                3 -> "D"
                4 -> "E"
                else -> ""
            }
            canvas.drawText(label, point.x, point.y + paint.textSize / 4, paint)
        }

        for (i in points.indices) {
            drawArrow(
                canvas, points[redArrowPoints[i].first], points[redArrowPoints[i].second], redArrowPaint, redArrowPaint, 15f * (-1f).pow(i), 15f * (-1f).pow(i)
            )
            drawArrow(
                canvas, points[blueArrowPoints[i].first], points[blueArrowPoints[i].second], blueArrowPaint, blueArrowPaint, -15f * (-1f).pow(i), 15f * (-1f).pow(i)
            )
        }
    }

    private fun drawArrow(
        canvas: Canvas,
        start: PointF,
        end: PointF,
        arrowPaint: Paint,
        arrowHeadPaint: Paint,
        offsetY: Float,
        offsetX: Float
    ) {
        if (start == end) {
            // Draw a loop (arc) for the same start and end points
            val left = start.x - 50f
            val top = start.y - 50f
            val right = start.x + 50f
            val bottom = start.y + 50f
            val oval = RectF(left, top, right, bottom)

            // Draw the arc (loop)
            canvas.drawArc(oval, 0f, 270f, false, arrowPaint)
        } else {
            val angle = atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble())
            val offset = PointF(
                (squareSize / 2) * cos(angle).toFloat(),
                (squareSize / 2) * sin(angle).toFloat()
            )

            val adjustedStart = PointF(start.x + offset.x, start.y + offset.y)
            val adjustedEnd = PointF(end.x - offset.x, end.y - offset.y)

            // Apply perpendicular offset for non-overlapping
            val perpendicularOffset = PointF(
                -offsetY * sin(angle).toFloat() + offsetX * cos(angle).toFloat(),
                offsetY * cos(angle).toFloat() + offsetX * sin(angle).toFloat()
            )
            val finalStart = PointF(
                adjustedStart.x + perpendicularOffset.x,
                adjustedStart.y + perpendicularOffset.y
            )
            val finalEnd =
                PointF(adjustedEnd.x + perpendicularOffset.x, adjustedEnd.y + perpendicularOffset.y)

            canvas.drawLine(finalStart.x, finalStart.y, finalEnd.x, finalEnd.y, arrowPaint)

            val arrowEndAngle = atan2(
                (finalEnd.y - finalStart.y).toDouble(),
                (finalEnd.x - finalStart.x).toDouble()
            )
            val x1 = finalEnd.x - arrowHeadLength * cos(arrowEndAngle + arrowAngle).toFloat()
            val y1 = finalEnd.y - arrowHeadLength * sin(arrowEndAngle + arrowAngle).toFloat()
            val x2 = finalEnd.x - arrowHeadLength * cos(arrowEndAngle - arrowAngle).toFloat()
            val y2 = finalEnd.y - arrowHeadLength * sin(arrowEndAngle - arrowAngle).toFloat()

            canvas.drawLine(finalEnd.x, finalEnd.y, x1, y1, arrowHeadPaint)
            canvas.drawLine(finalEnd.x, finalEnd.y, x2, y2, arrowHeadPaint)
        }
    }

    fun startArrowAnimation(path: String, duration: Long) {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 4f

        val verticalOffset = ((0.4 * height) - (numVertices * squareSize)) / (numVertices - 1)

        val points = when (numVertices) {
            3 -> listOf(
                PointF(centerX, (centerY - radius - verticalOffset).toFloat()),  // A
                PointF(centerX - radius * sin(Math.PI / 3).toFloat(), (centerY + radius / 2 + verticalOffset).toFloat()),  // B
                PointF(centerX + radius * sin(Math.PI / 3).toFloat(), (centerY + radius / 2 + verticalOffset).toFloat())   // C
            )
            4 -> listOf(
                PointF(centerX - radius, centerY - radius),  // A
                PointF(centerX + radius, centerY - radius),  // B
                PointF(centerX - radius, centerY + radius),  // C
                PointF(centerX + radius, centerY + radius)   // D
            )
            5 -> List(5) { i ->
                PointF(
                    (centerX + radius * cos(i * 2 * Math.PI / 5)).toFloat(),
                    (centerY + radius * sin(i * 2 * Math.PI / 5)).toFloat()
                )
            }
            else -> return
        }

        val animations = mutableListOf<ObjectAnimator>()

        for (i in path.indices) {
            val currentChar = path[i]
            val startVertexIndex = when (i) {
                0 -> 1  // Start from vertex A
                else -> getVertexIndexForArrow(path[i - 1], path[i])
            }
            val endVertexIndex = getVertexIndexForArrow(currentChar)

            val start = points[startVertexIndex]
            val end = points[endVertexIndex]

            val animatorX = ObjectAnimator.ofFloat(this, "bitmapX", start.x - resizedBitmap.width / 2, end.x - resizedBitmap.width / 2)
            val animatorY = ObjectAnimator.ofFloat(this, "bitmapY", start.y - resizedBitmap.height / 2, end.y - resizedBitmap.height / 2)

            animatorX.duration = duration
            animatorY.duration = duration

            animatorX.addUpdateListener {
                invalidate()
            }
            animatorY.addUpdateListener {
                invalidate()
            }

            animations.add(animatorX)
            animations.add(animatorY)
        }

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animations as List<Animator>?)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                animateBitmap = true
            }

            override fun onAnimationEnd(animation: Animator) {
                animateBitmap = false
                invalidate()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animatorSet.start()
    }

    private fun getVertexIndexForArrow(currentArrow: Char, nextArrow: Char? = null): Int {
        return when (currentArrow) {
            'C' -> 2  // For red arrow end point
            'M' -> 1  // For blue arrow end point
            else -> 0
        }
    }
}

