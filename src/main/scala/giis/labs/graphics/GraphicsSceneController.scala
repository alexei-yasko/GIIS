package giis.labs.graphics

import giis.labs.model.shape.{Shape, ShapeFactory}
import giis.labs.model.Point
import render.DebugRender

/**
 * @author Q-YAA
 */
class GraphicsSceneController(graphicsScene: GraphicsScene) {

    private var isInDebugMode = false

    private val scene = graphicsScene

    private var debugRender: DebugRender = null

    def drawShape(drawingContext: DrawingContext) {
        val shape = ShapeFactory.createShape(getSelectedPoints, drawingContext.shapeType)

        isInDebugMode match {
            case true => drawShapeInDebugMode(shape, drawingContext)
            case false => drawShapeInCommonMode(shape, drawingContext)
        }
    }

    def changeMode() {

        if (isInDebugMode) {
            isInDebugMode = false
        }
        else {
            isInDebugMode = true
        }
    }

    def isNextDebugStepEnabled: Boolean = debugRender != null && debugRender.isNextStepEnabled

    def nextDebugStep() {

        if (debugRender.isNextStepEnabled) {
            debugRender.nextStep()
        }

        if (!debugRender.isNextStepEnabled) {
            scene.clearSelectedPixels()
        }
    }

    def previousDebugStep() {

        if (debugRender.isPreviousStepEnabled) {
            debugRender.previousStep()
        }
    }

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
