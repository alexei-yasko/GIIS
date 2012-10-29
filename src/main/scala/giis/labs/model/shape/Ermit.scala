package giis.labs.model.shape

import giis.labs.model.Point
import giis.labs.graphics.render.ErmitRender
import giis.labs.graphics.DrawingContext

/**
 * Ermit spline shape.
 *
 * @author Q-YAA
 */
class Ermit(p1: Point, p2: Point, p3: Point, p4: Point) extends Shape {

    private var point1 = p1
    private var point2 = p2
    private var point3 = p3
    private var point4 = p4

    def getPointList = List(point1, point2, point3, point4)

    override def movePoint(from: Point, to: Point) {
        if (point1 == from) {
            point1 = to
        }
        else if (point2 == from) {
            point2 = to
        }
        else if (point3 == from) {
            point3 = to
        }
        else if (point4 == from) {
            point4 = to
        }
    }

    def createRender(drawingContext: DrawingContext) = new ErmitRender(this, drawingContext)

    /**
     * Get type of the shape object.
     *
     * @return ShapeType type of the shape object
     */
    def shapeType = Shape.Ermit
}
