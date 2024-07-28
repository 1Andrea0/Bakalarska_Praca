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

    var redArrowPoints = listOf(Pair(0, 1), Pair(1, 2), Pair(2, 0))
    var blueArrowPoints = listOf(Pair(0, 1), Pair(1, 2), Pair(2, 0))

    private var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.blchaa)
    private var resizedBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
    private var bitmap2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.blchab)
    private var resizedBitmap2: Bitmap = Bitmap.createScaledBitmap(bitmap2, 300, 300, true)
    private var bitmap3: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.blchac)
    private var resizedBitmap3: Bitmap = Bitmap.createScaledBitmap(bitmap3, 300, 300, true)

    private var animateBitmap: Boolean = false

    private var vertexPoints = listOf<PointF>()

    // Properties for each bitmap's position
    private val bitmapPositions = mutableListOf<PointF>()
    private val bitmapList = listOf(resizedBitmap, resizedBitmap2, resizedBitmap3)

    init {
        // Initialize positions for the bitmaps
        for (i in bitmapList.indices) {
            bitmapPositions.add(PointF(0f, 0f))
        }
    }

    var bitmap1X: Float
        get() = bitmapPositions[0].x
        set(value) {
            bitmapPositions[0].x = value
            invalidate()
        }

    var bitmap1Y: Float
        get() = bitmapPositions[0].y
        set(value) {
            bitmapPositions[0].y = value
            invalidate()
        }

    var bitmap2X: Float
        get() = bitmapPositions[1].x
        set(value) {
            bitmapPositions[1].x = value
            invalidate()
        }

    var bitmap2Y: Float
        get() = bitmapPositions[1].y
        set(value) {
            bitmapPositions[1].y = value
            invalidate()
        }

    var bitmap3X: Float
        get() = bitmapPositions[2].x
        set(value) {
            bitmapPositions[2].x = value
            invalidate()
        }

    var bitmap3Y: Float
        get() = bitmapPositions[2].y
        set(value) {
            bitmapPositions[2].y = value
            invalidate()
        }

    fun setNumVertices(num: Int) {
        numVertices = num
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 3f // Distance from center to each square center
        val verticalOffset =
            ((0.4 * height) - (numVertices * squareSize)) / (numVertices - 1)  // Vertical offset for top and bottom vertices

        vertexPoints = when (numVertices) {
            3 -> drawTriangle(canvas, centerX, centerY, radius, squareSize, verticalOffset)
            4 -> drawSquare(canvas, centerX, centerY, radius, squareSize)
            5 -> drawPentagon(canvas, centerX, centerY, radius, squareSize)
            else -> listOf()
        }

        // Draw arrows between vertices
        val offset = squareSize / 2
        for (pair in redArrowPoints) {
            val start = vertexPoints[pair.first]
            val end = vertexPoints[pair.second]
            drawArrow(canvas, start, end, redArrowPaint, offset)
        }

        for (pair in blueArrowPoints) {
            val start = vertexPoints[pair.first]
            val end = vertexPoints[pair.second]
            drawArrow(canvas, start, end, blueArrowPaint, offset)
        }

        // Draw each bitmap at its corresponding position
        for (i in bitmapList.indices) {
            if (animateBitmap) {
                val bitmap = bitmapList[i]
                val position = bitmapPositions[i]
                canvas.drawBitmap(bitmap, position.x, position.y, null)
            }
        }
    }

    private fun drawTriangle(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        squareSize: Float,
        verticalOffset: Double
    ): List<PointF> {
        val points = listOf(
            PointF(centerX, (centerY - radius - verticalOffset).toFloat()),  // Top
            PointF(
                centerX - radius * sin(Math.PI / 3).toFloat(),
                (centerY + radius / 2 + verticalOffset).toFloat()
            ),  // Bottom left
            PointF(
                centerX + radius * sin(Math.PI / 3).toFloat(),
                (centerY + radius / 2 + verticalOffset).toFloat()
            )   // Bottom right
        )

        drawPolygon(canvas, points, squareSize)
        return points
    }

    private fun drawSquare(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        squareSize: Float
    ): List<PointF> {
        val points = listOf(
            PointF(centerX - radius, centerY - radius),
            PointF(centerX + radius, centerY - radius),
            PointF(centerX - radius, centerY + radius),
            PointF(centerX + radius, centerY + radius)
        )
        drawPolygon(canvas, points, squareSize)
        return points
    }

    private fun drawPentagon(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        squareSize: Float
    ): List<PointF> {
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

    }

    private fun drawArrow(canvas: Canvas, start: PointF, end: PointF, paint: Paint, offset: Float) {
        val angle = atan2((end.y - start.y), (end.x - start.x))
        val startX = start.x + offset * cos(angle)
        val startY = start.y + offset * sin(angle)
        val endX = end.x - offset * cos(angle)
        val endY = end.y - offset * sin(angle)

        canvas.drawLine(startX, startY, endX, endY, paint)

        // Draw the arrow head
        val arrowAngle1 = angle + arrowAngle
        val arrowAngle2 = angle - arrowAngle
        canvas.drawLine(endX, endY,
            (endX - arrowHeadLength * cos(arrowAngle1)).toFloat(),
            (endY - arrowHeadLength * sin(arrowAngle1)).toFloat(), paint)
        canvas.drawLine(endX, endY,
            (endX - arrowHeadLength * cos(arrowAngle2)).toFloat(),
            (endY - arrowHeadLength * sin(arrowAngle2)).toFloat(), paint)
    }

    fun startArrowAnimation(path: String, duration: Long) {
        animateBitmap = true
        val animations = mutableListOf<Animator>()

        for (vertex in 0 until numVertices) {
            var startVertexIndex = vertex
            val imageIndex = vertex % bitmapList.size

            for (i in path.indices) {
                val currentChar = path[i]

                val endVertexIndex = getVertexIndexForArrow(currentChar, startVertexIndex)

                val startVertex = vertexPoints[startVertexIndex]
                val endVertex = vertexPoints[endVertexIndex]

                val animatorX = ObjectAnimator.ofFloat(
                    this, "bitmap${imageIndex + 1}X",
                    startVertex.x - bitmapList[imageIndex].width / 2, endVertex.x - bitmapList[imageIndex].width / 2
                )
                val animatorY = ObjectAnimator.ofFloat(
                    this, "bitmap${imageIndex + 1}Y",
                    startVertex.y - bitmapList[imageIndex].height / 2, endVertex.y - bitmapList[imageIndex].height / 2
                )

                animatorX.duration = duration
                animatorY.duration = duration

                animatorX.addUpdateListener {
                    invalidate()
                }
                animatorY.addUpdateListener {
                    invalidate()
                }

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(animatorX, animatorY)
                animations.add(animatorSet)

                startVertexIndex = endVertexIndex
            }
        }
        val finalAnimatorSet = AnimatorSet()
        finalAnimatorSet.playSequentially(animations)
        finalAnimatorSet.start()
    }



    private fun getVertexIndexForArrow(char: Char, int: Int): Int {
        if (char == 'Č') {
            for (pair in redArrowPoints) {
                if (int == pair.first) {
                    return pair.second
                }
            }
        } else {
            for (pair in blueArrowPoints) {
                if (int == pair.first) {
                    return pair.second
                }
            }
        }
        return -1
    }
}