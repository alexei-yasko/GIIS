package giis.labs.graphics

import giis.labs.model.shape.{Shape, ShapeFactory}
import giis.labs.model.Point
import render.DebugRender

/**
 * Controller for the {@link GraphicsScene}.
 *
 * @author Q-YAA
 */
class GraphicsSceneController(graphicsScene: GraphicsScene) {

    private var isInDebugMode = false

    private val scene = graphicsScene

    private var debugRender: DebugRender = null

    /**
     * Method that control shape drawing on the scene.
     *
     * @param drawingContext drawing context for the shape
     */
    def drawShape(drawingContext: DrawingContext) {
        val shape = ShapeFactory.createShape(getSelectedPoints, drawingContext.shapeType)

        isInDebugMode match {
            case true => drawShapeInDebugMode(shape, drawingContext)
            case false => drawShapeInCommonMode(shape, drawingContext)
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
     * Execute next step in the debug mode.
     */
    def nextDebugStep() {

        if (debugRender.isNextStepEnabled) {
            debugRender.nextStep()
        }

        if (!debugRender.isNextStepEnabled) {
            scene.clearSelectedPixels()
            debugRender.finishDebug()
        }
    }

    /**
     * Execute previous step in the debug mode.
     */
    def previousDebugStep() {

        if (debugRender.isPreviousStepEnabled) {
            debugRender.previousStep()
        }
    }

    /**
     * Remove last drawn shape from the scene.
     */
    def cancelShapeDrawing() {
        scene.removeLastShape()
    }

    private def drawShapeInCommonMode(shape: Shape, drawingContext: DrawingContext) {
        if (shape != null) {
            scene.addShapeRender(shape.createRender(drawingContext))
            scene.clearSelectedPixels()
        }
    }

    private def drawShapeInDebugMode(shape: Shape, drawingContext: DrawingContext) {

        if (shape != null) {
            debugRender = new DebugRender(shape.createRender(drawingContext))
            scene.addShapeRender(debugRender)
        }
    }

    private def getSelectedPoints: List[Point] = for (pixel <- scene.getSelectedPixels) yield {
        pixel.point
    }
}
