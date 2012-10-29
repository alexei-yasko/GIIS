package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{DrawingContext, Pixel}
import java.awt.Color
import giis.labs.model.Point

/**
 * Abstract class for the shape renders.
 *
 * @author Q-YAA
 */
abstract class Render(renderedShape: Shape, context: DrawingContext) {

    val mainPixelsDrawingContext = DrawingContext.createDrawingContext(Color.GREEN)

    /**
     * Returns drawing context of the shape render.
     *
     * @return drawing context object
     */
    def drawingContext = context

    /**
     * Returns shape of the shape render.
     *
     * @return shape object
     */
    def shape = renderedShape

    /**
     * Draw shape that represented by the shape render.
     *
     * @return List[Pixel] result of drawing
     */
    def draw: List[Pixel] = drawShape ::: Pixel.createPixelList(shape.getPointList, mainPixelsDrawingContext.color)

    /**
     * Template method for the shape drawing. Concrete renders must override this method.
     *
     * @return List[Pixel] result of drawing
     */
    def drawShape: List[Pixel]
}
