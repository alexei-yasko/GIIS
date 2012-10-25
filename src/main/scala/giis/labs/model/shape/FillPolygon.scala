package giis.labs.model.shape

import giis.labs.graphics.DrawingContext
import giis.labs.model.Point
import giis.labs.graphics.render.FillPolygonRender

/**
 * @author Q-YAA
 */
abstract class FillPolygon(point: Point, shape: Polygon) extends Shape {

    private var basePoint = point

    /**
     * Returns point list that define the shape.
     *
     * @return List[Point] point list
     */
    def getPointList = List(basePoint)

    /**
     * Move point from one position to another.
     *
     * @param from origin position
     * @param to new position
     */
    def movePoint(from: Point, to: Point) {
        if (basePoint == from) {
            basePoint = to
        }
    }

    /**
     * Creates render for shape.
     *
     * @param drawingContext drawing context object
     * @return created shape render
     */
    def createRender(drawingContext: DrawingContext) = new FillPolygonRender(this, drawingContext, shape)
}
