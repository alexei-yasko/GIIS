package giis.labs.graphics.render

import giis.labs.model.shape.{Polygon, Shape}
import giis.labs.graphics.{Pixel, DrawingContext}

/**
 * @author Q-YAA
 */
class PolygonRender(shape: Shape, drawingContext: DrawingContext) extends Render(shape, drawingContext) {

    /**
     * Template method for the shape drawing. Concrete renders must override this method.
     *
     * @return List[Pixel] result of drawing
     */
    def drawShape = {
        var resultPixelList = List[Pixel]()
        val polygon = shape.asInstanceOf[Polygon]

        for (polygonLine <- polygon.getPolygonLines) {
            val linePixels = polygonLine.createRender(DrawingContext.createDrawingContext).drawShape
            resultPixelList = resultPixelList ::: linePixels
        }

        resultPixelList
    }
}
