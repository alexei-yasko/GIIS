package giis.labs.model.shape

import giis.labs.model.Point
import giis.labs.graphics.DrawingContext
import giis.labs.graphics.render.CircleRender

/**
 * Circle shape.
 *
 * @author AS
 */
class Circle(point1: Point, point2: Point) extends Shape {

    private var center = point1

    private var end = point2

    def getPointList = List(center, end)

    def movePoint(from: Point, to: Point) {
        if (end == from) {
            end = to
        } else if (center == from) {
            end = new Point(end.x + (to.x - center.x), end.y + (to.y - center.y))
            center = to
        }
    }

    def createRender(drawingContext: DrawingContext) = new CircleRender(this, drawingContext)

    /**
     * Get type of the shape object.
     *
     * @return ShapeType type of the shape object
     */
    def shapeType = Shape.Circle
}
