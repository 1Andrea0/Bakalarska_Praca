package com.example.projekt

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt
import kotlin.random.Random

class ViewModel : ViewModel() {

    private val commands = listOf("Č", "M", "ČM", "MČ", "MM", "ČM", "MČ", "ČČ", "ČMM", "MČM")
    private val commands2 = listOf("M", "Č", "MČ", "ČM", "ČČ", "MM", "ČM", "MČ", "MČM", "MČČ")
    private var currentLevel = 0
    private var loop = false
    private var coin = 0
    var stages = 1

    var graphs = listOf(listOf(Pair(0,1), Pair(1,2), Pair(2,0)),
        listOf(Pair(0,2), Pair(1,0), Pair(2,1)))

    private val graphsLoop = listOf(listOf(Pair(0,0), Pair(1,2), Pair(2,1)),
        listOf(Pair(0,2), Pair(1,1), Pair(2,0)),
        listOf(Pair(0,1), Pair(1,0), Pair(2,2)))

    var redArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,0))
    var blueArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,0))

    fun arrows() {
        if (loop) {
            coin = Math.random().roundToInt()
//            Log.d("DEBUG", "LOOPUJES?")
//            Log.d("DEBUG", "$coin")
            if ((coin == 1)) {
                redArrowPoints = graphsLoop.random()
                blueArrowPoints = graphs.random()
            } else {
                redArrowPoints = graphs.random()
                blueArrowPoints = graphsLoop.random()
            }
        } else {
            redArrowPoints = graphs.random()
            blueArrowPoints = graphs.random()
        }
        Log.d("DEBUG", "Red: $redArrowPoints")
        Log.d("DEBUG", "Blue: $blueArrowPoints")
    }

    fun resetLevel() {
        currentLevel = 0
    }

    fun addFour() {
        graphs = listOf(listOf(Pair(0,0), Pair(1,3), Pair(2,1), Pair(3,2)),
            listOf(Pair(0,1), Pair(1,0), Pair(2,3), Pair(3,2)),
            listOf(Pair(0,1), Pair(1,2), Pair(2,0), Pair(3,3)),
            listOf(Pair(0,1), Pair(1,2), Pair(2,3), Pair(3,0)),
            listOf(Pair(0,1), Pair(1,3), Pair(2,0), Pair(3,2)),
            listOf(Pair(0,1), Pair(1,3), Pair(2,2), Pair(3,0)),
            listOf(Pair(0,2), Pair(1,0), Pair(2,1), Pair(3,3)),
            listOf(Pair(0,2), Pair(1,0), Pair(2,3), Pair(3,1)),
            listOf(Pair(0,2), Pair(1,1), Pair(2,3), Pair(3,0)),
            listOf(Pair(0,2), Pair(1,3), Pair(2,0), Pair(3,1)),
            listOf(Pair(0,2), Pair(1,3), Pair(2,1), Pair(3,0)),
            listOf(Pair(0,3), Pair(1,0), Pair(2,1), Pair(3,2)),
            listOf(Pair(0,3), Pair(1,0), Pair(2,2), Pair(3,1)),
            listOf(Pair(0,3), Pair(1,1), Pair(2,0), Pair(3,2)),
            listOf(Pair(0,3), Pair(1,2), Pair(2,0), Pair(3,1)),
            listOf(Pair(0,3), Pair(1,2), Pair(2,1), Pair(3,0)))

        redArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,3), Pair(3,0))
        blueArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,3), Pair(3,0))
//        Log.d("Graphs", "${graphs[0]}")
//        Log.d("Graphs", "${graphs[1]}")
//        Log.d("Graphs", "${graphs[2]}")
//        Log.d("Graphs", "${graphs.size}")

    }

    fun currentLevel(): Int {
        return currentLevel
    }

    fun nextLevel() : Int {
        currentLevel += 1
//        Log.d("DEBUG", "$currentLevel")
        if (currentLevel > 3) loop = true
        return currentLevel
    }

    fun nextStage() : Int {
        stages += 1
//        println(stages)
        return stages
    }

    fun getCommand(): String {
        return commands[currentLevel]
    }

    fun verify(answers: List<Int>): Boolean {
        var expectedSize = 3

        if (answers.size == 4) {
            expectedSize = 4
        }
        if (answers.size == 5) {
            expectedSize = 5
        }

        val result =
            mutableListOf(Pair(0, 0), Pair(1, 1), Pair(2, 2), Pair(3, 3), Pair(4,4)).take(expectedSize).toMutableList()
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

        for (i in answers.indices) {
            if (answers[i] != result[i].second) return false
        }

        return true
    }

    //toto mi dáva lines, teda na level 2 tie buttony vyplnené
    @RequiresApi(Build.VERSION_CODES.N)
    fun verify2() : MutableList<Pair<Int,Int>> {
        var result = mutableListOf(Pair(0,0), Pair(1,1), Pair(2,2))
        val command = commands2[currentLevel].toList()
//        length = command.size
//        Log.d("DEBUG", "$command")

        for (c in command) {
            if (c == 'M') {
                for (i in result.indices) {
                    if (result[i].second == blueArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, blueArrowPoints[i].second)
                        result[i] = updatedPair
                    }
                }
            }
            if (c == 'Č') {
                for (i in result.indices) {
                    if (result[i].second == redArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, redArrowPoints[i].second)
                        result[i] = updatedPair
                    }
                }
            }
        }

//        Log.d("DEBUG", "$result")
//        Log.d("DEBUG", "$answer0, $answer1, $answer2")
//        Log.d("DEBUG", "$result")

//        if (answer0 != result[0].second) return false
//        if (answer1 != result[1].second) return false
//        if (answer2 != result[2].second) return false
        return result

//        result = mutableListOf(Pair(0,0), Pair(1,1), Pair(2,2))
    }

    // toto kontroluje daný príkaz, či sedí s tým, čo je v buttonoch
    @RequiresApi(Build.VERSION_CODES.N)
    fun verify3(answer0:Int, answer1:Int, answer2:Int, command:List<Char>) : Boolean {
        var result = mutableListOf(Pair(0,0), Pair(1,1), Pair(2,2))
//        val command = getCommand().toList()
//        length = command.size
        Log.d("DEBUG", "$command") //[M]

        for (c in command) {
            if (c == 'M') {
                for (i in result.indices) {
                    if (result[i].second == blueArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, blueArrowPoints[i].second)
                        result[i] = updatedPair
                    }
                }
            }
            if (c == 'Č') {
                for (i in result.indices) {
                    if (result[i].second == redArrowPoints[result[i].second].first) {
                        val updatedPair = Pair(result[i].first, redArrowPoints[i].second)
                        result[i] = updatedPair
                    }
                }
            }
        }

//        Log.d("DEBUG", "$result")
        Log.d("DEBUG", "$answer0, $answer1, $answer2") //[1,2,0]
        Log.d("DEBUG", "$result") //[(0, 1), (1, 2), (2, 0)]

        if (answer0 != result[0].second) return false
        if (answer1 != result[1].second) return false
        if (answer2 != result[2].second) return false

        result = mutableListOf(Pair(0,0), Pair(1,1), Pair(2,2))
        return true
    }


}