package giis.labs.model.shape

import giis.labs.graphics.DrawingContext
import giis.labs.model.Point
import giis.labs.graphics.render.PolygonRender

/**
 * @author Q-YAA
 */
class Polygon(points: Array[Point]) extends Shape {

    /**
     * Returns point list that define the shape.
     *
     * @return List[Point] point list
     */
    def getPointList = points.toList

    /**
     * Move point from one position to another.
     *
     * @param from origin position
     * @param to new position
     */
    def movePoint(from: Point, to: Point) {

        for (i <- 0 until points.length) {
            if (points(i) == from) {
                points(i) = to
                return
            }
        }
    }

    /**
     * Creates render for shape.
     *
     * @param drawingContext drawing context object
     * @return created shape render
     */
    def createRender(drawingContext: DrawingContext) = new PolygonRender(this, drawingContext)

    def shapeType = Shape.Polygon
}
