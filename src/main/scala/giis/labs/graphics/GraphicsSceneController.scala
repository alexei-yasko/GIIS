package giis.labs.graphics

import giis.labs.model.shape.ShapeFactory
import giis.labs.model.{Point, ShapeType}
import java.awt.Color
import render.DebugRender

/**
 * @author Q-YAA
 */
class GraphicsSceneController(graphicsScene: GraphicsScene) {

    private val scene = graphicsScene

    private var debugRender: DebugRender = null

    def drawShape(shapeType: ShapeType, color: Color) {
        val shape = ShapeFactory.createShape(getSelectedPoints, shapeType)

        if (shape != null) {
            scene.addShapeRender(shape.createRender(shapeType, color))
            scene.clearSelectedPixels()
        }
    }

    def isDebugEnabled(shapeType: ShapeType): Boolean = ShapeFactory.createShape(getSelectedPoints, shapeType) != null

    def isNextDebugStepEnabled: Boolean = debugRender != null && debugRender.isNextStepEnabled

    def isDebugStart: Boolean = debugRender != null

    def drawShapeInDebugMode(shapeType: ShapeType, color: Color) {
        val shape = ShapeFactory.createShape(getSelectedPoints, shapeType)

        if (shape != null) {
            debugRender = new DebugRender(shape.createRender(shapeType, color))
            scene.addShapeRender(debugRender)
        }
    }

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

    private def getSelectedPoints: List[Point] = for (pixel <- scene.getSelectedPixels) yield {
        pixel.point
    }
}
