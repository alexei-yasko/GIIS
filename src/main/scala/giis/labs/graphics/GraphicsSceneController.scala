package giis.labs.graphics

import giis.labs.model.shape.{Shape, ShapeFactory}
import giis.labs.model.{Matrix, FillShapeType, ShapeType, Point}
import render.DebugRender
import actors.Actor
import java.awt.Color

/**
 * Controller for the {@link GraphicsScene}.
 *
 * @author Q-YAA
 */
class GraphicsSceneController(graphicsScene: GraphicsScene) {

    private var isInDebugMode = false

    private val scene = graphicsScene

    private var debugRender: DebugRender = null

    private var debugAnimator: DebugAnimator = null

    /**
     * Method that control shape drawing on the scene.
     *
     * @param shapeType type of the drawing shape
     * @param drawingContext drawing context for the shape
     */
    def drawShape(shapeType: ShapeType, drawingContext: DrawingContext) {
        val shape = shapeType match {
            case _: FillShapeType => ShapeFactory.createFillShape(
                shapeType, getSelectedPoints, scene.getShapeThatContainsPoint(getSelectedPoints.head))

            case _ => ShapeFactory.createShape(getSelectedPoints, shapeType)
        }

        if (shape != null) {

            isInDebugMode match {
                case true => drawShapeInDebugMode(shape, drawingContext)
                case false => drawShapeInCommonMode(shape, drawingContext)
            }
        }
    }

    /**
     * Changes controller mode. (Enable debug mode and common mode)
     */
    def changeMode() {

        if (isInDebugMode) {
            isInDebugMode = false
        }
        else {
            isInDebugMode = true
        }
    }

    /**
     * Determines if the next step in debug mode enabled.
     *
     * @return true if the next step enabled, false in other case
     */
    def isNextDebugStepEnabled: Boolean = isInDebugMode && debugRender != null && debugRender.isNextStepEnabled

    /**
     * Determines if the previous step in debug mode enabled.
     *
     * @return true if the next step enabled, false in other case
     */
    def isPreviousDebugStepEnabled: Boolean = isInDebugMode && debugRender != null && debugRender.isPreviousStepEnabled

    /**
     * Execute next step in the debug mode.
     */
    def nextDebugStep() {

        if (isNextDebugStepEnabled) {
            debugRender.nextStep()
        }

        if (debugRender != null && !isNextDebugStepEnabled) {
            finishDebug()
        }
    }

    /**
     * Method that finish debug for shape.
     */
    def finishDebug() {
        scene.clearSelectedPixels()
        debugRender.finishDebug()
        debugAnimator = null
    }

    /**
     * Execute previous step in the debug mode.
     */
    def previousDebugStep() {

        if (isPreviousDebugStepEnabled) {
            debugRender.previousStep()
        }
    }

    /**
     * Remove last drawn shape from the scene.
     */
    def cancelShapeDrawing() {
        scene.removeLastShape()
    }

    /**
     * Method that started animation thread for the debug.
     *
     * @param mainFrame main frame object, is need to allow control main frame behavior from the animation thread.
     */
    def startDebugAnimation(mainFrame: GraphicsMainFrame) {
        debugAnimator = new DebugAnimator(this, mainFrame)
        debugAnimator.start()
    }

    /**
     * Method that stops animation thread for the debug.
     */
    def stopDebugAnimation() {
        if (debugAnimator != null) {
            debugAnimator ! "stop"
            debugAnimator = null
        }
    }

    /**
     * Method that check if debug animation thread running.
     *
     * @return true if running, false in the other case
     */
    def isAnimationRunning: Boolean = debugAnimator != null && debugAnimator.getState == Actor.State.Runnable

    private def drawShapeInCommonMode(shape: Shape, drawingContext: DrawingContext) {
        scene.addShapeRender(shape.createRender(drawingContext))
        scene.clearSelectedPixels()
    }

    private def drawShapeInDebugMode(shape: Shape, drawingContext: DrawingContext) {
        debugRender = new DebugRender(shape.createRender(drawingContext))
        scene.addShapeRender(debugRender)
    }

    private def getSelectedPoints: List[Point] = for (pixel <- scene.getSelectedPixels) yield {
        pixel.point
    }

    def moveRight() {
        val matrix = new Matrix(
            Array[Array[Double]](
                Array[Double](1, 0, 0),
                Array[Double](0, 1, 0),
                Array[Double](1, 0, 1)
            )
        )
        transform(matrix)
    }

    def moveLeft() {
        val matrix = new Matrix(
            Array[Array[Double]](
                Array[Double](1, 0, 0),
                Array[Double](0, 1, 0),
                Array[Double](-1, 0, 1)
            )
        )
        transform(matrix)
    }

    def moveUp() {
        val matrix = new Matrix(
            Array[Array[Double]](
                Array[Double](1, 0, 0),
                Array[Double](0, 1, 0),
                Array[Double](0, 1, 1)
            )
        )
        transform(matrix)
    }

    def moveDown() {
        val matrix = new Matrix(
            Array[Array[Double]](
                Array[Double](1, 0, 0),
                Array[Double](0, 1, 0),
                Array[Double](0, -1, 1)
            )
        )
        transform(matrix)
    }

    def rotateLeft() {
        val angle = math.Pi / 18
        val matrix = new Matrix(
            Array[Array[Double]](
                Array[Double](math.cos(angle), math.sin(angle), 0),
                Array[Double](-math.sin(angle), math.cos(angle), 0),
                Array[Double](0, 0, 1)
            )
        )
        transform(matrix)
    }

    def transform(matrix: Matrix) {
        var point = getSelectedPoints.head
        var shape = graphicsScene.getShapeThatContainsPoint(point)
        if (shape == null) return

        var list = shape.getPointList
        var resultPoints = List[Point]()

        list.foreach {
            shapePoint =>
                val shapePointMatrix = new Matrix(
                    Array[Array[Double]](
                        Array[Double](shapePoint.x, shapePoint.y, 1)
                    )
                )
                var resultMatrix = shapePointMatrix * matrix
                var x = resultMatrix.getValue(0, 0)
                var y = resultMatrix.getValue(0, 1)
                resultPoints = new Point(x.toInt, y.toInt) :: resultPoints
        }

        val selectedPointMatrix = new Matrix(
            Array[Array[Double]](
                Array[Double](point.x, point.y, 1)
            )
        )
        var resultMatrix = selectedPointMatrix * matrix
        var x = resultMatrix.getValue(0, 0)
        var y = resultMatrix.getValue(0, 1)

        var pixel = new Pixel(new Point(x.toInt, y.toInt), Color.RED)
        graphicsScene.selectPixel(pixel)

        shape.setPoints(resultPoints)
        shape.changeUpdateState(true)
    }
}
