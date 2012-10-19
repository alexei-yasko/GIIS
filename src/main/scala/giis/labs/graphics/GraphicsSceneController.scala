package giis.labs.graphics

import giis.labs.model.shape.{Shape, ShapeFactory}
import giis.labs.model.Point
import render.DebugRender
import actors.Actor

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
     * @param drawingContext drawing context for the shape
     */
    def drawShape(drawingContext: DrawingContext) {
        val shape = ShapeFactory.createShape(getSelectedPoints, drawingContext.shapeType)

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
}
