package giis.labs.model.shape

import giis.labs.model.Point
import giis.labs.graphics.DrawingContext
import giis.labs.graphics.render.CircleRender

/**
 * @author AS
 */
class Circle(point1: Point, point2: Point) extends Shape {

    val center = point1

    val end = point2

    def getPointList = List(center, end)

    def createRender(drawingContext: DrawingContext) = new CircleRender(this, drawingContext)
}
