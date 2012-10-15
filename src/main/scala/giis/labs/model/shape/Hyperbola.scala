package giis.labs.model.shape

import giis.labs.model.Point
import giis.labs.graphics.DrawingContext
import giis.labs.graphics.render.{HyperbolaRender, CircleRender}

/**
 * Circle shape.
 *
 * @author AS
 */
class Hyperbola(point1: Point, point2: Point) extends Shape {

    private var start = point1

    private var end = point2

    def getPointList = List(start, end)

    def movePoint(from: Point, to: Point) {
        if (end == from) {
            end = to
        } else if (start == from) {
            end = new Point(end.x + (to.x - start.x), end.y + (to.y - start.y))
            start = to
        }
    }

    def createRender(drawingContext: DrawingContext) = new HyperbolaRender(this, drawingContext)
}
