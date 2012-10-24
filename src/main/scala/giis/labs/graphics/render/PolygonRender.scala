package giis.labs.graphics.render

import giis.labs.model.shape.{Line, Shape}
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

        val pointArray = shape.getPointList.toArray

        for (i <- 1 until pointArray.length) {
            val line = new Line(pointArray(i - 1), pointArray(i)) {
                def shapeType = Shape.LineBrezenhem
            }
            resultPixelList = resultPixelList ::: new LineRender(line, DrawingContext.createDrawingContext).drawShape
        }

        val line = new Line(pointArray.last, pointArray.head) {
            def shapeType = Shape.LineBrezenhem
        }
        resultPixelList = resultPixelList ::: new LineRender(line, DrawingContext.createDrawingContext).drawShape

        resultPixelList
    }
}
