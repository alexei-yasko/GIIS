package giis.labs.graphics

import java.awt.Color

/**
 * Class that encapsulate all drawing settings.
 *
 * @author Q-YAA
 */
protected class DrawingContext(drawingColor: Color, drawingFillColor: Color) {

    /**
     * Color of drawing pixel.
     */
    val color = drawingColor

    val fillColor = drawingFillColor

    override def toString = "drawing context { " + color + ", " + fillColor + " }"
}

/**
 * Singleton object for the drawing context class. Contains current settings for dawning and factory methods.
 */
object DrawingContext {

    private var currentColor = Color.BLACK

    private var currentFillColor = Color.BLUE

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
     * Set fill color for the drawing context.
     *
     * @param color color to set
     */
    def fillColor_=(color: Color) {
        currentFillColor = color
    }

    /**
     * Returns fill current color.
     *
     * @return current color
     */
    def fillColor = currentFillColor

    /**
     * Creates drawing context object from current settings.
     *
     * @return drawing context object
     */
    def createDrawingContext: DrawingContext = new DrawingContext(currentColor, currentFillColor)

    /**
     * Create drawing context object from current settings and given color.
     *
     * @param color color for the drawing context
     * @return drawing context object
     */
    def createDrawingContext(color: Color): DrawingContext = new DrawingContext(color, currentFillColor)
}


