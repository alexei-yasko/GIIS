package giis.labs.graphics

import java.awt.Color

/**
 * Class that encapsulate all drawing settings.
 *
 * @author Q-YAA
 */
protected class DrawingContext(drawingColor: Color) {

    /**
     * Color of drawing pixel.
     */
    val color = drawingColor

    override def toString = "drawing context { " + color + " }"
}

/**
 * Singleton object for the drawing context class. Contains current settings for dawning and factory methods.
 */
object DrawingContext {

    private var currentColor = Color.BLACK

    /**
     * Set color for the drawing context.
     *
     * @param color color to set
     */
    def color_=(color: Color) {
        currentColor = color
    }

    /**
     * Returns current color.
     *
     * @return current color
     */
    def color = currentColor

    /**
     * Creates drawing context object from current settings.
     *
     * @return drawing context object
     */
    def createDrawingContext: DrawingContext = new DrawingContext(currentColor)

    /**
     * Create drawing context object from current settings and given color.
     *
     * @param color color for the drawing context
     * @return drawing context object
     */
    def createDrawingContext(color: Color): DrawingContext = new DrawingContext(color)
}


