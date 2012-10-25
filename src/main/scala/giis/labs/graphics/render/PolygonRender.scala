package giis.labs.graphics.render

import giis.labs.model.shape.Polygon
import giis.labs.graphics.{Pixel, DrawingContext}

/**
 * @author Q-YAA
 */
class PolygonRender(polygon: Polygon, drawingContext: DrawingContext) extends Render(polygon, drawingContext) {

    /**
     * Template method for the shape drawing. Concrete renders must override this method.
     *
     * @return List[Pixel] result of drawing
     */
    def drawShape = {
        var resultPixelList = List[Pixel]()

        for (edge <- polygon.getEdgeList) {
            resultPixelList = resultPixelList ::: edge.createRender(drawingContext).draw
        }

        resultPixelList
    }
}
