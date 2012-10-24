package giis.labs.model.shape

import giis.labs.model.Point
import giis.labs.graphics.DrawingContext
import giis.labs.graphics.render.{HyperbolaRender, CircleRender}

/**
 * Circle shape.
 *
 * @author AS
 */
class Hyperbola(point1: Point, point2: Point, point3: Point) extends Shape {

    private var start = point1

    private var end = point2

    private var height = point3

    def getPointList = List(start, end, height)

    def movePoint(from: Point, to: Point) {
        if (end == from) {
            end = to
        } else if (start == from) {
            end = new Point(end.x + (to.x - start.x), end.y + (to.y - start.y))
            start = to
        } else if (height == from) {
            height = to
        }
    }

    def createRender(drawingContext: DrawingContext) = new HyperbolaRender(this, drawingContext)

    /**
     * Get type of the shape object.
     *
     * @return ShapeType type of the shape object
     */
    def shapeType = Shape.Hyperbola
}
