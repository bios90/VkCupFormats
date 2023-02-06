package com.example.vkcupformats.ui.utils

import android.graphics.PathMeasure
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath

class PathMeasureHelper(pathOriginal: Path) {
    private val pathForMeasure = PathMeasure(pathOriginal.asAndroidPath(), false)
    private val allSegments = PathMeasure(pathOriginal.asAndroidPath(), false).getLengthSegments()
    private val fullLength = PathMeasure(pathOriginal.asAndroidPath(), false).getFullLength()
    private var lastSegmentIndex = 0
    private var lastProgress: Float = 0f
    private val poses = floatArrayOf(0f, 0f)

    fun getProgressPoint(progress: Float): Pair<Float, Float> {
        if (progress < lastProgress) {
            throw IllegalStateException("*** error asked wrong progress ***")
        }
        val fullLengthPos = fullLength * progress
        val segmentIndex = getSegmentPos(progress)
        if (segmentIndex < 0) {
            return Pair(poses[0], poses[1])
        }
        val segment = allSegments.get(segmentIndex)
        val segmentLength = (segment.second - segment.first)
        if (segmentIndex > lastSegmentIndex) {
            pathForMeasure.nextContour()
        }
        val posInSegment = fullLengthPos - segment.first
        val progressInSegment = posInSegment / segmentLength
        lastSegmentIndex = segmentIndex
        pathForMeasure.getPosTan(progressInSegment * segmentLength, poses, null)
        return Pair(poses[0], poses[1])
    }

    fun getSegmentPos(progress: Float): Int {
        val point = fullLength * progress
        for (i in 0 until allSegments.size - 1) {
            val range = allSegments.get(i)
            if (point >= range.first && point < range.second) {
                return i
            }
        }
        if (point == allSegments.last().second) {
            return allSegments.size - 1
        }
        return -1
    }
}

fun PathMeasure.getLengthSegments(): List<Pair<Float, Float>> {
    var lastEnd = 0f
    val lengths: MutableList<Pair<Float, Float>> = arrayListOf()
    do {
        lengths.add(Pair(lastEnd, lastEnd + this.length))
        lastEnd = lastEnd + this.length
    } while (this.nextContour())
    lengths.add(Pair(lastEnd, lastEnd + this.length))
    return lengths
}

fun PathMeasure.getFullLength(): Float {
    val lengths: MutableList<Float> = arrayListOf()
    do {
        lengths.add(this.length)
    } while (this.nextContour())
    lengths.add(this.length)
    return lengths.sum()
}