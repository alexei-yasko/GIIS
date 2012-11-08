package giis.labs.graphics.render

import giis.labs.model.shape.Rect3D
import giis.labs.graphics.{Pixel, DrawingContext}

/**
 * @author Q-YAA
 */
class Rect3DRender(rect3D: Rect3D, drawingContext: DrawingContext) extends Render(rect3D, drawingContext) {

    /**
     * Template method for the shape drawing. Concrete renders must override this method.
     *
     * @return List[Pixel] result of drawing
     */
    def drawShape = {
        var resultPixelList = List[Pixel]()

        for (edge <- rect3D.getEdgeList) {
            resultPixelList = resultPixelList ::: edge.createRender(drawingContext).drawShape.reverse
        }

        resultPixelList
    }
}
