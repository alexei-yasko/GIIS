package giis.labs.graphics

import java.awt.Color
import giis.labs.model.ShapeType
import giis.labs.model.shape.Shape

/**
 * Class that encapsulate all drawing settings.
 *
 * @author Q-YAA
 */
protected class DrawingContext(drawingColor: Color, drawingShapeType: ShapeType) {

    /**
     * Color of drawing pixel.
     */
    val color = drawingColor

    /**
     * Current type of the shape to drawing.
     */
    val shapeType = drawingShapeType

    override def toString = "drawing context { " + color + ", " + shapeType + " }"
}

/**
 * Singleton object for the drawing context class. Contains current settings for dawning and factory methods.
 */
object DrawingContext {

    private var currentColor = Color.BLACK

    private var currentShapeType: ShapeType = Shape.LineDda

    /**
     * Set color for the drawing context.
     *
     * @param color color to set
     */
    def color_=(color: Color) {
        currentColor = color
    }

    /**
     * Set current drawing shape type for the drawing context.
     *
     * @param shapeType type of the drawing shape
     */
    def shapeType_=(shapeType: ShapeType) {
        currentShapeType = shapeType
    }

    /**
     * Returns current color.
     *
     * @return current color
     */
    def color = currentColor

    /**
     * Returns current shape type.
     *
     * @return current shape type
     */
    def shapeType = currentShapeType

    /**
     * Creates drawing context object from current settings.
     *
     * @return drawing context object
     */
    def createDrawingContext: DrawingContext = new DrawingContext(currentColor, currentShapeType)

    /**
     * Create drawing context object from current settings and given color.
     *
     * @param color color for the drawing context
     * @return drawing context object
     */
    def createDrawingContext(color: Color): DrawingContext = new DrawingContext(color, currentShapeType)

    /**
     * Create drawing context object from current settings and given color.
     *
     * @param shapeType shape type for the drawing context
     * @return drawing context object
     */
    def createDrawingContext(shapeType: ShapeType): DrawingContext = new DrawingContext(currentColor, shapeType)
}


