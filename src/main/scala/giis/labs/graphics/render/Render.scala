package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{DrawingContext, Pixel}
import java.awt.Color
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
abstract class Render(renderedShape: Shape, context: DrawingContext) {
    val mainPixelsColor = Color.GREEN

    def drawingContext = context

    def shape = renderedShape

    def draw: List[Pixel]

    protected def createPixel(x: Int, y: Int, drawingContext: DrawingContext): Pixel = new Pixel(new Point(x, y), drawingContext)

    protected def createPixel(point: Point, drawingContext: DrawingContext): Pixel = new Pixel(point, drawingContext)

    protected def createPixelList(pointList: List[Point], drawingContext: DrawingContext) = for (point <- pointList) yield {
        createPixel(point, drawingContext)
    }
}
