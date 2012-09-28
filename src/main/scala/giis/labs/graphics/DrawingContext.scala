package giis.labs.graphics

import java.awt.Color
import giis.labs.model.ShapeType
import giis.labs.model.shape.Shape

/**
 * @author Q-YAA
 */
protected class DrawingContext(drawingColor: Color, drawingShapeType: ShapeType) {

    val color = drawingColor

    val shapeType = drawingShapeType

    override def toString = "drawing context { " + color + ", " + shapeType + " }"
}

object DrawingContext {

    private var currentColor = Color.BLACK

    private var currentShapeType: ShapeType = Shape.LineDda

    def color_=(color: Color) {
        currentColor = color
    }

    def shapeType_=(shapeType: ShapeType) {
        currentShapeType = shapeType
    }

    def color = currentColor

    def shapeType = currentShapeType

    def createDrawingContext: DrawingContext = new DrawingContext(currentColor, currentShapeType)

    def createDrawingContext(color: Color): DrawingContext = new DrawingContext(color, currentShapeType)
}


