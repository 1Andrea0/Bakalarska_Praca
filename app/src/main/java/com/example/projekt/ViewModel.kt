package com.example.projekt

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt
import kotlin.random.Random

class ViewModel : ViewModel() {

    private val commands = createCommands()
    private val graphsRed: MutableList<List<Pair<Int, Int>>> = mutableListOf()
    private val graphsBlue: MutableList<List<Pair<Int, Int>>> = mutableListOf()
    private var currentLevel = 0
    private var loop = false
    private var numberLoopsRed = 0
    private var numberLoopsBlue = 0

    private var numberOfVertices = 3

    var redArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,0))
    var blueArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,0))

    fun setNumberOfVerticesForGraph(int: Int){numberOfVertices = int}

    fun getNumberOfVerticesForGraph():Int{return numberOfVertices}

    private fun graphWithoutLoops(startingVertex: MutableList<Int>):MutableList<Pair<Int,Int>>{
        if (numberOfVertices <= 0) return mutableListOf()

        val vertices = (0 until numberOfVertices).toMutableList()
        val result = mutableListOf<Pair<Int, Int>>()

        for (i in startingVertex) {
            if (i > -1) {
                vertices.remove(i)
            }
        }

        vertices.shuffle()

            for (i in 0 until vertices.size - 1) {
                result.add(Pair(vertices[i], vertices[i + 1]))
            }
            result.add(Pair(vertices.last(), vertices.first()))

        result.sortBy { it.first }

        return result
    }

    fun graphWithLoop(numberLoop: Int): MutableList<Pair<Int, Int>> {
        if (numberOfVertices <= 0) return mutableListOf()

        val result = mutableListOf<Pair<Int, Int>>()
        var repeat = -1
        val repeating = mutableListOf<Int>()

        for (i in (0 until numberLoop).filter { it != repeat }) {
            val loopVertex = (0 until numberOfVertices).random()
            repeat = loopVertex
            result.add(Pair(loopVertex, loopVertex))
            repeating.add(loopVertex)
        }

        val remainingEdges = graphWithoutLoops(repeating)

        result.addAll(remainingEdges)

        result.sortBy { it.first }

        return result
    }

    fun numberLoops(){
        if (numberOfVertices == 3 && loop) {
            val coin = Math.random().roundToInt()
            if ((coin == 1)) {
                numberLoopsRed = 1
                numberLoopsBlue = 0
            } else {
                numberLoopsRed = 0
                numberLoopsBlue = 1
            }
        }

        if (numberOfVertices == 4 && loop) {
            numberLoopsRed = (0..1).random()
            numberLoopsBlue = (0..1).random()
            if (numberLoopsRed == 0 && numberLoopsBlue == 0) numberLoopsRed = 1
        }

        if (numberOfVertices == 5 && loop) {
            numberLoopsRed = (0..2).random()
            numberLoopsBlue = (0..2).random()
            if (numberLoopsRed == 0 && numberLoopsBlue == 0) numberLoopsRed = 1
        }
    }

    fun createGraph(){
        if (loop) {
            numberLoops()
            redArrowPoints = graphWithLoop(numberLoopsRed).toList()
            blueArrowPoints = graphWithLoop(numberLoopsBlue).toList()
        } else {
            redArrowPoints = graphWithoutLoops(mutableListOf(-1)).toList()
            blueArrowPoints = graphWithoutLoops(mutableListOf(-1)).toList()
        }
        graphsRed.add(redArrowPoints)
        graphsBlue.add(blueArrowPoints)
    }

    fun resetLevel() {
        currentLevel = 0
    }

    fun currentLevel(): Int {
        return currentLevel
    }

    fun nextLevel() : Int {
        currentLevel += 1
        if (currentLevel > 3) loop = true
        return currentLevel
    }

    fun getCommand(): String {
        return commands[currentLevel]
    }

    fun createCommands() : List<String> {
        var length: Int
        val choices = listOf("M","Č")
        val commands = mutableListOf("","","","","","","","","","")

        for (i in commands.indices) {
            var command = ""
            length = if (i > 3) {
                (2..4).random()
            } else {
                (1..2).random()
            }
            for (j in 1..length) {
                command+=choices.random()
            }
            commands[i] = command
        }
        return commands.toList()
    }

    private var resultVerify = mutableListOf(Pair(0, 0), Pair(1, 1), Pair(2, 2), Pair(3, 3), Pair(4,4)).take(numberOfVertices).toMutableList()

    fun verify(answers: List<Int>): Boolean {
        val result =
            mutableListOf(Pair(0, 0), Pair(1, 1), Pair(2, 2), Pair(3, 3), Pair(4,4)).take(numberOfVertices).toMutableList()
        val command = getCommand().toList()

        for (c in command) {
            if (c == 'M') {
                for (i in result.indices) {
                    if (result[i].second == blueArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, blueArrowPoints[result[i].second].second)
                        result[i] = updatedPair
                    }
                }
            }
            if (c == 'Č') {
                for (i in result.indices) {
                    if (result[i].second == redArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, redArrowPoints[result[i].second].second)
                        result[i] = updatedPair
                    }
                }
            }
        }

        resultVerify = result

        val answersCorrect = answers.take(numberOfVertices)
        for (i in answersCorrect.indices) {
            if (answersCorrect[i]-1 != result[i].second) return false
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun verify() : MutableList<Pair<Int,Int>> {

        val result =
            mutableListOf(Pair(0, 0), Pair(1, 1), Pair(2, 2), Pair(3, 3), Pair(4,4)).take(numberOfVertices).toMutableList()
        val command = getCommand().toList()

        for (c in command) {
            if (c == 'M') {
                for (i in result.indices) {
                    if (result[i].second == blueArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, blueArrowPoints[result[i].second].second)
                        result[i] = updatedPair
                    }
                }
            }
            if (c == 'Č') {
                for (i in result.indices) {
                    if (result[i].second == redArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, redArrowPoints[result[i].second].second)
                        result[i] = updatedPair
                    }
                }
            }
        }

        return result
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun verify(answers: List<Int>, command:List<Char>): Boolean {
        val result =
            mutableListOf(Pair(0, 0), Pair(1, 1), Pair(2, 2), Pair(3, 3), Pair(4,4)).take(numberOfVertices).toMutableList()

        for (c in command) {
            if (c == 'M') {
                for (i in result.indices) {
                    if (result[i].second == blueArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, blueArrowPoints[result[i].second].second)
                        result[i] = updatedPair
                    }
                }
            }
            if (c == 'Č') {
                for (i in result.indices) {
                    if (result[i].second == redArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, redArrowPoints[result[i].second].second)
                        result[i] = updatedPair
                    }
                }
            }
        }

        val answersCorrect = answers.take(numberOfVertices)
        for (i in answersCorrect.indices) {
            if (answersCorrect[i] != result[i].second) return false
        }

        return true
    }

}