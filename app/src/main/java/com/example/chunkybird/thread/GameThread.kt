package com.example.chunkybird.thread

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log
import android.view.SurfaceHolder
import com.example.chunkybird.R
import com.example.chunkybird.model.*
import java.io.*
import kotlin.random.Random

class GameThread(
    filePath: String,
    context: Context,
    private var holder: SurfaceHolder,
    resources: Resources
) : Thread() {

    private val tag: String = "GameThread"
    var isRunning: Boolean = false
    private val fps: Int = (1000.0 / 40.0).toInt()
    private val backgroundImage = BackgroundImage()
    private var bitmapImage: Bitmap? = null
    private var startTime: Long = 0
    private var frameTime: Long = 0
    private var velocity = ScreenSize.SCREEN_WIDTH / 240

    private val bird: Bird
    private var state: Int = 0
    private var velocityBird: Int = 0
    private var weight = ScreenSize.SCREEN_HEIGHT / 360

    private var cot: Cot? = null
    private val numCot = 2
    private var velocityCot = ScreenSize.SCREEN_WIDTH / 102
    private val minY = ScreenSize.SCREEN_HEIGHT / 4
    private val maxY = ScreenSize.SCREEN_HEIGHT - minY - ScreenSize.SCREEN_HEIGHT / 3
    private val kc = ScreenSize.SCREEN_WIDTH * 3 / 4
    private var cotArray: ArrayList<Cot> = arrayListOf()
    private var ran: Random = Random

    private var iCot = 0
    private var birdDie: BirdDie
    private var isDead = false
    private var fell = false
    private var isPaused = false
    private var score: Int = 0

    private var bug: Bug? = null
    private var bugArray: ArrayList<Bug> = arrayListOf()
    private var yum: Int = 0

    private val pixelFont1 = Typeface.createFromAsset(context.assets, "retro_font.ttf")
    private val pixelFont2 = Typeface.createFromAsset(context.assets, "flappy_font.ttf")
    private val file = File(filePath)

    private var pause : Pause

    init {
        isRunning = true
        bird = Bird(resources)
        bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.run_background)
        bitmapImage = bitmapImage?.let { scaleResize(it) }
        cot = Cot(resources)
        createCot(resources)
        birdDie = BirdDie(resources)
        bug = Bug(resources)
        pause = Pause(resources)
    }

    private fun createCot(resources: Resources) {
        for (i in 0 until numCot) {
            val cot = Cot(resources)
            cot.x = ScreenSize.SCREEN_WIDTH + kc * i
            cot.ccY = ran.nextInt(maxY - minY) + minY
            val bug = Bug(resources)
            bug.x = cot.x + (cot.w / 2 - bug.w / 2)
            if (isBugged()) {
                bug.y = cot.getBottomY() - (ScreenSize.SCREEN_HEIGHT / 3) / 2 - bug.h / 2
            } else {
                bug.y = -1
            }
            cotArray.add(cot)
            bugArray.add(bug)
        }
    }

    override fun run() {
        Log.d(tag, "Thread started")
        while (isRunning) {
            if (!isPaused) {
                startTime = System.nanoTime()
                val canvas = holder.lockCanvas()
                if (canvas != null) {
                    try {
                        synchronized(holder) {
                            renderBack(canvas)
                            renderBird(canvas)
                            renderCot(canvas)
                            renderPause(canvas)
                            renderScore(canvas)
                            renderDie(canvas)
                            renderDeath(canvas)
                        }
                    } finally {
                        holder.unlockCanvasAndPost(canvas)
                    }
                }
                frameTime = (System.nanoTime() - startTime) / 1000000
                if (frameTime < fps) {
                    try {
                        sleep(fps - frameTime)
                    } catch (e: InterruptedException) {
                        Log.e("Interrupted", "Thread sleep error")
                    }
                }
            }
        }
        Log.d(tag, "Thread finish")
    }

    private fun renderPause(canvas: Canvas?) {                                                          // RENDER PAUSE BUTTON
        if (!isDead) {
            canvas!!.drawBitmap(pause.image, (ScreenSize.SCREEN_WIDTH / 36).toFloat(),
                (ScreenSize.SCREEN_HEIGHT / 20 - pause.h).toFloat(), null)
        }
    }

    private fun renderScore(canvas: Canvas?) {                                                          // DISPLAY SCORE ON TOP
        if (!isDead) {
            val paint = Paint()
            paint.color = Color.GRAY
            paint.typeface = pixelFont1
            paint.textSize = (ScreenSize.SCREEN_WIDTH / 7).toFloat()
            canvas!!.drawText(score.toString(),                                                         // DRAW SCORE VALUE
                ScreenSize.SCREEN_WIDTH / 2 - paint.measureText(score.toString()) / 2,
                (ScreenSize.SCREEN_HEIGHT / 13).toFloat(), paint)
            paint.textSize = (ScreenSize.SCREEN_WIDTH / 12).toFloat()
            canvas.drawText(yum.toString(),                                                             // DRAW BUG EATEN VALUE
                ScreenSize.SCREEN_WIDTH - (paint.measureText(yum.toString()) + bug!!.w + 20),
                (ScreenSize.SCREEN_HEIGHT / 20 - 5).toFloat(), paint)
            canvas.drawBitmap(bug!!.bug, (ScreenSize.SCREEN_WIDTH - (bug!!.w + 10)).toFloat(),
                (ScreenSize.SCREEN_HEIGHT / 20 - bug!!.h).toFloat(), paint)
        }
    }

    private fun renderDeath(canvas: Canvas?) {                                                          // DISPLAY DEATH SCREEN
        if (!isRunning) {
            val paint = Paint()

            paint.typeface = pixelFont2                                                                 // GAME OVER
            paint.color = Color.BLACK
            paint.textSize = (ScreenSize.SCREEN_WIDTH / 4).toFloat()
            val over = "GAME OVER"
            canvas!!.drawText(                                                                          // DRAW "GAME OVER"
                over, ScreenSize.SCREEN_WIDTH / 2 - paint.measureText(over) / 2,
                (ScreenSize.SCREEN_HEIGHT / 4).toFloat(), paint)

            val rectOuter = RectF(                                                                      // DEATH SCREEN BOX
                DeathScreen.x.toFloat(),
                DeathScreen.y.toFloat(),
                DeathScreen.x2.toFloat(),
                DeathScreen.y2.toFloat())
            val rectInner = RectF(
                (DeathScreen.x + 10).toFloat(),
                (DeathScreen.y + 10).toFloat(),
                (DeathScreen.x2 - 10).toFloat(),
                (DeathScreen.y2 - 10).toFloat())
            val rectReplay = RectF(
                DeathScreen.rx.toFloat(),
                DeathScreen.ry.toFloat(),
                DeathScreen.rx2.toFloat(),
                DeathScreen.ry2.toFloat())
            val rectMenu = RectF(
                DeathScreen.mx.toFloat(),
                DeathScreen.my.toFloat(),
                DeathScreen.mx2.toFloat(),
                DeathScreen.my2.toFloat())
            canvas.apply {                                                                      // DRAW DEATH SCREEN BOX
                paint.color = Color.rgb(75, 75, 75)
                drawRect(rectOuter, paint)
                paint.color = Color.rgb(128, 128, 128)
                drawRect(rectInner, paint)
                paint.color = Color.rgb(120, 120, 120)
                drawRect(rectReplay, paint)
                paint.color = Color.rgb(120, 120, 120)
                drawRect(rectMenu, paint)
            }

            val highScore = try {                                                                       // READ HIGHEST SCORE DATA FROM FILE
                val fileInputStream = FileInputStream(file)
                val inputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val dataAsString = bufferedReader.readText()
                bufferedReader.close()
                dataAsString.toInt()
            } catch (e: IOException) {
                0                                                                                       // HIGHEST SCORE VALUE IS 0 WHEN NO DATA
            }

            paint.typeface = pixelFont1

            if (highScore < score) {                                                                    // REWRITE HIGH SCORE IF LOWER THAN SCORE
                try {
                    val data = score.toString()
                    val fileOutputStream = FileOutputStream(file)
                    val outputStreamWriter = OutputStreamWriter(fileOutputStream)
                    outputStreamWriter.write(data)
                    outputStreamWriter.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                paint.color = Color.RED
                paint.textSize = (ScreenSize.SCREEN_WIDTH / 20).toFloat()
                val new = "NEW"
                canvas.drawText(new,                                                                    // DISPLAY "NEW" FOR BEST
                    DeathScreen.sx - paint.measureText(new) * 2,
                    (DeathScreen.sy - 10).toFloat(), paint)
            }

            paint.color = Color.BLACK                                                                   // SCORE
            paint.textSize = (ScreenSize.SCREEN_WIDTH / 12).toFloat()
            val scoreText = "SCORE"
            canvas.drawText(scoreText,                                                                  // DRAW SCORE LABEL
                DeathScreen.sx - paint.measureText(scoreText) / 2,
                DeathScreen.hsy.toFloat(), paint)
            val scoreVal = score.toString()
            canvas.drawText(scoreVal,                                                                   // DRAW SCORE VALUE
                DeathScreen.sx - paint.measureText(scoreVal) / 2,
                (DeathScreen.hsy + DeathScreen.hsy / 8).toFloat(), paint)

            val highScoreText = "BEST"                                                                  // HIGHEST SCORE
            canvas.drawText(highScoreText,                                                              // DRAW HIGHEST SCORE LABEL
                DeathScreen.sx - paint.measureText(highScoreText) / 2,
                DeathScreen.sy.toFloat(), paint)
            val highScoreVal = try {                                                                    // READ HIGHEST SCORE VALUE FROM FILE
                val fileInputStream = FileInputStream(file)
                val inputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val dataAsString = bufferedReader.readText()
                bufferedReader.close()
                dataAsString
            } catch (e: IOException) {
                "0"                                                                                     // HIGHEST SCORE IS 0 WHEN NO DATA
            }
            canvas.drawText(highScoreVal,                                                               // DRAW HIGHEST SCORE
                DeathScreen.sx - paint.measureText(highScoreVal) / 2,
                (DeathScreen.sy + DeathScreen.sy / 10).toFloat(), paint)

            paint.textSize = (ScreenSize.SCREEN_WIDTH / 10).toFloat()                                    // REPLAY BUTTON
            val replayText = "REPLAY"
            val replayTextX =
                (DeathScreen.rx + (DeathScreen.rx2 - DeathScreen.rx) / 2 - paint.measureText(
                    replayText) / 2)
            val replayTextY = DeathScreen.ry + DeathScreen.h2 * 4 / 5
            canvas.drawText(replayText, replayTextX, replayTextY.toFloat(), paint)                      // DRAW REPLAY LABEL

            val menuText = "MENU"                                                                       // MENU BUTTON
            val menuTextX =
                (DeathScreen.mx + (DeathScreen.mx2 - DeathScreen.mx) / 2 - paint.measureText(
                    menuText) / 2)
            val menuTextY = DeathScreen.my + DeathScreen.h2 * 4 / 5
            canvas.drawText(menuText, menuTextX, menuTextY.toFloat(), paint)                            // DRAW MENU LABEL
        }
    }

    private fun renderDie(canvas: Canvas?) {                                                            // RENDER DEAD BIRD
        if (isDead) {
            velocity = 0                                                                                // STOP BACKGROUND
            velocityCot = 0                                                                             // STOP OBSTACLES
            velocityBird += weight                                                                      // DROP BIRD
            birdDie.y += velocityBird
            var i: Int = birdDie.currentFrame
            if (fell) {                                                                                 // ADJUST BIRD IF DIED IN BOTTOM
                canvas!!.drawBitmap(birdDie.getBirdDie(i), bird.x.toFloat(),
                    (birdDie.y - (birdDie.getBirdDie(0).height - 20)).toFloat(), null)
            } else {
                canvas!!.drawBitmap(birdDie.getBirdDie(i), bird.x.toFloat(),
                    birdDie.y.toFloat(), null)
            }
            i++
            if (i == birdDie.maxFrame) {
                i = 0
            }
            birdDie.currentFrame = i
            if (birdDie.y > (ScreenSize.SCREEN_HEIGHT - birdDie.getBirdDie(0).height) - 35) {         // END GAME WHEN LANDED
                isRunning = false
            }
        }
    }

    private fun renderCot(canvas: Canvas?) {                                                            // RENDER OBSTACLE
        if (state == 1) {
            if (cotArray[iCot].x < bird.x - cot!!.w) {
                iCot++
                score++                                                                                 // ADD SCORE WHEN OBSTACLE IS PASSED
                if (iCot > numCot - 1) {
                    iCot = 0
                }
            } else if (doBoxesIntersect(bird.x.toFloat(), bird.y.toFloat(),
                    (bird.x + bird.getBird1(0).width).toFloat(),
                    (bird.y + bird.getBird1(0).height).toFloat(),
                    (cotArray[iCot].x + cot!!.w / 2).toFloat(), cotArray[iCot].getBottomY().toFloat(),
                    cotArray[iCot].x.toFloat(), (cotArray[iCot].getBottomY() + cot!!.h).toFloat(),
                (cotArray[iCot].x + cot!!.w).toFloat(), (cotArray[iCot].getBottomY() + cot!!.h).toFloat())) {
                isDead = true                                                                           // BIRD DIE BOTTOM
            } else if (doBoxesIntersect(bird.x.toFloat(), bird.y.toFloat(),
                    (bird.x + bird.getBird1(0).width).toFloat(),
                    (bird.y + bird.getBird1(0).height).toFloat(),
                    cotArray[iCot].x.toFloat(), cotArray[iCot].getTopY().toFloat(),
                    (cotArray[iCot].x + cot!!.w).toFloat(), cotArray[iCot].getTopY().toFloat(),
                    (cotArray[iCot].x + cot!!.w / 2).toFloat(), cotArray[iCot].ccY.toFloat())) {
                isDead = true                                                                           // BIRD DIE TOP
            } else if (bugArray[iCot].x < bird.x + bird.getBird1(0).width &&
                bugArray[iCot].y > bird.y) {
                yum++
                bugArray[iCot].y = -1
                if (yum == 50 || yum == 100 || yum == 300) {                                            // ADD WEIGHT TO BIRD
                    velocityCot++
                    weight++
                }
            }
            for (i in 0 until numCot) {
                if (cotArray[i].x < -cot!!.w) {
                    cotArray[i].x += numCot * kc
                    cotArray[i].ccY = ran.nextInt(maxY - minY) + minY
                    bugArray[i].x = cotArray[i].x + (cotArray[i].w / 2 - bug!!.w / 2)
                    if (isBugged()) {
                        bugArray[i].y = cotArray[i].getBottomY() -
                                (ScreenSize.SCREEN_HEIGHT / 3) / 2 - bug!!.h / 2
                    } else {
                        bugArray[i].y = -1
                    }
                }
                cotArray[i].x -= velocityCot                                             // MOVING THE OBSTACLE
                bugArray[i].x -= velocityCot
                canvas!!.drawBitmap(cot!!.cotTop, cotArray[i].x.toFloat(),                              // DRAW TOP OBSTACLE
                    cotArray[i].getTopY().toFloat(), null)
                canvas.drawBitmap(cot!!.cotBottom, cotArray[i].x.toFloat(),                             // DRAW BOTTOM OBSTACLE
                    cotArray[i].getBottomY().toFloat(), null)
                if (bugArray[i].y >= 0) {
                    canvas.drawBitmap(bug!!.bug, bugArray[i].x.toFloat(),
                        bugArray[i].y.toFloat(), null)
                }
            }
        }
    }

    data class Edge(val x1: Float, val y1: Float, val x2: Float, val y2: Float)

    private fun doBoxesIntersect(
        squareX1: Float, squareY1: Float, squareX2: Float, squareY2: Float,
        triangleX1: Float, triangleY1: Float, triangleX2: Float, triangleY2: Float, triangleX3: Float, triangleY3: Float
    ): Boolean {
        val triangleVerticesInsideSquare =
            isPointInsideRectangle(triangleX1, triangleY1, squareX1, squareY1, squareX2, squareY2) ||
                    isPointInsideRectangle(triangleX2, triangleY2, squareX1, squareY1, squareX2, squareY2) ||
                    isPointInsideRectangle(triangleX3, triangleY3, squareX1, squareY1, squareX2, squareY2)
        val squareEdges = arrayOf(
            Edge(squareX1, squareY1, squareX2, squareY1),
            Edge(squareX2, squareY1, squareX2, squareY2),
            Edge(squareX2, squareY2, squareX1, squareY2),
            Edge(squareX1, squareY2, squareX1, squareY1))
        val triangleEdges = arrayOf(
            Edge(triangleX1, triangleY1, triangleX2, triangleY2),
            Edge(triangleX2, triangleY2, triangleX3, triangleY3),
            Edge(triangleX3, triangleY3, triangleX1, triangleY1))
        val triangleEdgeIntersectsSquareEdge =
            squareEdges.any { squareEdge ->
                triangleEdges.any { triangleEdge ->
                    doEdgesIntersect(squareEdge, triangleEdge)
                }
            }
        return triangleVerticesInsideSquare || triangleEdgeIntersectsSquareEdge
    }

    private fun isPointInsideRectangle(x: Float, y: Float, rectX1: Float, rectY1: Float, rectX2: Float, rectY2: Float): Boolean {
        return x in rectX1..rectX2 && y >= rectY1 && y <= rectY2
    }

    private fun doEdgesIntersect(edge1: Edge, edge2: Edge): Boolean {
        val det = (edge1.x1 - edge1.x2) * (edge2.y1 - edge2.y2) - (edge1.y1 - edge1.y2) * (edge2.x1 - edge2.x2)
        if (det == 0f) return false
        val t = ((edge1.x1 - edge2.x1) * (edge2.y1 - edge2.y2) - (edge1.y1 - edge2.y1) * (edge2.x1 - edge2.x2)) / det
        val u = -((edge1.x1 - edge1.x2) * (edge1.y1 - edge2.y1) - (edge1.y1 - edge1.y2) * (edge1.x1 - edge2.x1)) / det
        return t in 0f..1f && u >= 0f && u <= 1f
    }

    private fun isBugged(): Boolean {
        return ran.nextInt(4) == 0
    }

    private fun renderBird(canvas: Canvas?) {                                                           // RENDER BIRD FLYING
        if (state == 1) {
            if (bird.y < (ScreenSize.SCREEN_HEIGHT - bird.getBird1(0).height) ||                 // BIRD JUMP AND FALL
                velocityBird < 0) {
                velocityBird += weight
                bird.y += velocityBird
                birdDie.y = bird.y                                                                      // SYNC BIRD COORDINATE FOR BIRD DYING
                if (bird.y > (ScreenSize.SCREEN_HEIGHT - bird.getBird1(0).height)) {             // BIRD DIE UPON FALLING TO LAND
                    isDead = true
                    fell = true
                }
            }
        }
        if (!isDead) {                                                                                  // DRAW BIRD
            var current: Int = bird.currentFrame
            if (yum >= 300) {
                canvas!!.drawBitmap(bird.getBird4(current), bird.x.toFloat(),
                    bird.y.toFloat(), null)
            } else if (yum >= 100) {
                canvas!!.drawBitmap(bird.getBird3(current), bird.x.toFloat(),
                    bird.y.toFloat(), null)
            } else if (yum >= 50) {
                canvas!!.drawBitmap(bird.getBird2(current), bird.x.toFloat(),
                    bird.y.toFloat(), null)
            } else {
                canvas!!.drawBitmap(bird.getBird1(current), bird.x.toFloat(),
                    bird.y.toFloat(), null)
            }
            current++
            if (current > bird.maxFrame) {
                current = 0
            }
            bird.currentFrame = current
        }
    }

    private fun renderBack(canvas: Canvas?) {                                                           // RENDER BACKGROUND
        backgroundImage.x -= velocity
        if (backgroundImage.x < -bitmapImage!!.width) {                                                 // LOOP BACKGROUND
            backgroundImage.x = 0
        }
        bitmapImage?.let {                                                                      // DRAW BACKGROUND
            canvas!!.drawBitmap(it, (backgroundImage.x).toFloat(),
                (backgroundImage.y).toFloat(), null)
        }
        if (backgroundImage.x < -bitmapImage!!.width + ScreenSize.SCREEN_WIDTH) {                       // DRAW THE NEXT LOOP
            bitmapImage?.let {
                canvas!!.drawBitmap(it, (backgroundImage.x + bitmapImage!!.width).toFloat(),
                    (backgroundImage.y).toFloat(), null)
            }
        }
    }

    private fun scaleResize(bitmap: Bitmap): Bitmap {                                                   // RESIZE BACKGROUND TO SCREEN SIZE
        val ratio: Float = (bitmap.width / bitmap.height).toFloat()
        val scaleWidth: Int = (ratio * ScreenSize.SCREEN_WIDTH).toInt()
        return Bitmap.createScaledBitmap(bitmap, scaleWidth, ScreenSize.SCREEN_HEIGHT, false)
    }

    fun jump() {                                                                                        // BIRD JUMP
        if (!isDead) {
            if (isPaused) {                                                                             // RESUME GAME
                isPaused = false
                Log.d(tag, "Game resumed")
            }
            state = 1                                                                                   // START THE GAME
            if (bird.y > 40) {                                                                          // PREVENT JUMP WHEN ON TOP
                velocityBird = -ScreenSize.SCREEN_HEIGHT / 36
            }
        }
    }

    fun pause() {                                                                                       // PAUSE OR RESUME GAME
        isPaused = if (isPaused) {
            Log.d(tag, "Game resumed")
            false
        } else {
            Log.d(tag, "Game paused")
            true
        }
    }
}