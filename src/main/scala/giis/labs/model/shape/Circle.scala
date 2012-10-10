package giis.labs.model.shape

import giis.labs.model.Point
import giis.labs.graphics.DrawingContext
import giis.labs.graphics.render.CircleRender

/**
 * @author AS
 */
class Circle(point1: Point, point2: Point) extends Shape {

    var center = point1

    var end = point2

    def getPointList = List(center, end)

    def movePoint(from: Point, to: Point) {
        if (center == from) {
            end = new Point(end.x + (to.x - center.x), end.y + (to.y - center.y))
            center = to
        }
        if (end == from) {
            end = to
        }
    }

    def createRender(drawingContext: DrawingContext) = new CircleRender(this, drawingContext)
}
