package com.example.projekt

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt
import kotlin.random.Random

class ViewModel : ViewModel() {

    private val commands = listOf("ČM", "M", "ČM", "MČ", "MM", "ČM", "MČ", "ČČ", "ČMM", "MČM")
    private val commands2 = listOf("M", "Č", "MČ", "ČM", "ČČ", "MM", "ČM", "MČ", "MČM", "MČČ")
    private var currentLevel = 0
    private var currentStage = 1
    private var loop = false
    private var coin = 0
    var stages = 1

//    var graphs = listOf(listOf(Pair(0,1), Pair(1,2), Pair(2,0)),
//        listOf(Pair(0,2), Pair(1,0), Pair(2,1)))
//
//    private var graphsLoop = listOf(listOf(Pair(0,0), Pair(1,2), Pair(2,1)),
//        listOf(Pair(0,2), Pair(1,1), Pair(2,0)),
//        listOf(Pair(0,1), Pair(1,0), Pair(2,2)))

    // 12.7. code
    private val vertices = listOf('A', 'B', 'C', 'D', 'E')
    private var numberOfVertices = 3

    var redArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,0))
    var blueArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,0))

    fun setNumberOfVerticesForGraph(int: Int){numberOfVertices = int}

    fun getNumberOfVerticesForGraph():Int{return numberOfVertices}

//    fun getListOfVertices():List<Char>{return vertices.subList(0,numberOfVertices)}

    fun graphWithoutLoops(startingVertex: Int):MutableList<Pair<Int,Int>>{
        if (numberOfVertices <= 0) return mutableListOf() // Check for valid number of vertices

        val vertices = (0 until numberOfVertices).toMutableList()
        val result = mutableListOf<Pair<Int, Int>>()

        if (startingVertex > -1) {
            vertices.remove(startingVertex)
        }

        vertices.shuffle()

            for (i in 0 until vertices.size - 1) {
                result.add(Pair(vertices[i], vertices[i + 1]))
            }
            // Add the edge that closes the cycle
            result.add(Pair(vertices.last(), vertices.first()))
//        }

        return result
    }

    fun graphWithLoop(): MutableList<Pair<Int, Int>> {
        if (numberOfVertices <= 0) return mutableListOf() // Check for valid number of vertices

        // Pick a random vertex for the loop
        val loopVertex = (0 until numberOfVertices).random()

        // Start with the loop
        val result = mutableListOf<Pair<Int, Int>>()
        result.add(Pair(loopVertex, loopVertex))

        // Generate the rest of the graph without loops
        val remainingEdges = graphWithoutLoops(loopVertex)

        // Add the remaining edges to the result
        result.addAll(remainingEdges)

        return result
    }

    fun createGraph(){
//        redArrowPoints = graphWithLoop().toList()
//        blueArrowPoints = graphWithLoop().toList()
        redArrowPoints = graphWithoutLoops(-1).toList()
        Log.d("GRAPHS","Red:${redArrowPoints}")
        blueArrowPoints = graphWithoutLoops(-1).toList()
//        Log.d("DEBUG", "Red: $redArrowPoints")
//        Log.d("DEBUG", "Blue: $blueArrowPoints")
    }

    // end

//    fun arrows() {
//        if (loop) {
//            coin = Math.random().roundToInt()
//            if ((coin == 1)) {
//                redArrowPoints = graphsLoop.random()
//                blueArrowPoints = graphs.random()
//            } else {
//                redArrowPoints = graphs.random()
//                blueArrowPoints = graphsLoop.random()
//            }
//        } else {
//            redArrowPoints = graphs.random()
//            blueArrowPoints = graphs.random()
//        }
//
//    }

    fun resetLevel() {
        currentLevel = 0
    }

//    fun addFour() {
//        graphs = listOf(listOf(Pair(0,1), Pair(1,0), Pair(2,3), Pair(3,2)),
//            listOf(Pair(0,1), Pair(1,2), Pair(2,3), Pair(3,0)),
//            listOf(Pair(0,1), Pair(1,3), Pair(2,0), Pair(3,2)),
//            listOf(Pair(0,2), Pair(1,0), Pair(2,3), Pair(3,1)),
//            listOf(Pair(0,2), Pair(1,3), Pair(2,0), Pair(3,1)),
//            listOf(Pair(0,2), Pair(1,3), Pair(2,1), Pair(3,0)),
//            listOf(Pair(0,3), Pair(1,0), Pair(2,1), Pair(3,2)),
//            listOf(Pair(0,3), Pair(1,2), Pair(2,0), Pair(3,1)),
//            listOf(Pair(0,3), Pair(1,2), Pair(2,1), Pair(3,0)))
//
//        graphsLoop = listOf(listOf(Pair(0,0), Pair(1,3), Pair(2,1), Pair(3,2)),
//            listOf(Pair(0,1), Pair(1,2), Pair(2,0), Pair(3,3)),
//            listOf(Pair(0,1), Pair(1,3), Pair(2,2), Pair(3,0)),
//            listOf(Pair(0,2), Pair(1,0), Pair(2,1), Pair(3,3)),
//            listOf(Pair(0,2), Pair(1,1), Pair(2,3), Pair(3,0)),
//            listOf(Pair(0,3), Pair(1,0), Pair(2,2), Pair(3,1)),
//            listOf(Pair(0,3), Pair(1,1), Pair(2,0), Pair(3,2)))
//
//        redArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,3), Pair(3,0))
//        blueArrowPoints = listOf(Pair(0,1), Pair(1,2), Pair(2,3), Pair(3,0))
//    }

    fun currentLevel(): Int {
        return currentLevel
    }

    fun nextLevel() : Int {
        currentLevel += 1
        if (currentLevel > 3) loop = true
        return currentLevel
    }

    fun nextStage() : Int {
        stages += 1
        return stages
    }

    fun getCommand(): String {
        return commands[currentLevel]
    }

    var resultVerify = mutableListOf(Pair(0, 0), Pair(1, 1), Pair(2, 2))

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

//        Log.d("LOGIKA", "Red: $redArrowPoints")
//        Log.d("LOGIKA", "Blue: $blueArrowPoints")
        Log.d("DEBUG", "Result: $result")
        Log.d("DEBUG", "Command: $command")

        // command sa nemeni
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

        Log.d("DEBUG", "Result finished: $result")
        for (i in answers.indices) {
            if (answers[i] != result[i].second) return false
        }

        return true
    }

    //toto mi dáva lines, teda na level 2 tie buttony vyplnené
    @RequiresApi(Build.VERSION_CODES.N)
    fun verify2() : MutableList<Pair<Int,Int>> {
        var expectedSize = 3

//        if (answers.size == 4) {
//            expectedSize = 4
//        }
//        if (answers.size == 5) {
//            expectedSize = 5
//        }

        val result =
            mutableListOf(Pair(0, 0), Pair(1, 1), Pair(2, 2), Pair(3, 3), Pair(4,4)).take(expectedSize).toMutableList()
        val command = getCommand().toList()

//        Log.d("LOGIKA", "Red: $redArrowPoints")
//        Log.d("LOGIKA", "Blue: $blueArrowPoints")
        Log.d("DEBUG", "Result: $result")
        Log.d("DEBUG", "Command: $command")

        // command sa nemeni
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