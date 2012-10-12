package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{DrawingContext, Pixel}
import java.awt.Color
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
abstract class Render(renderedShape: Shape, context: DrawingContext) {

    private val mainPixelsDrawingContext = DrawingContext.createDrawingContext(Color.GREEN)

    def drawingContext = context

    def shape = renderedShape

    def draw: List[Pixel] = drawShape ::: Pixel.createPixelList(shape.getPointList, mainPixelsDrawingContext)

    protected def drawShape: List[Pixel]
}
