package giis.labs.model.shape

import giis.labs.graphics.render.LineRender
import giis.labs.graphics.DrawingContext
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
class Line(point1: Point, point2: Point) extends Shape {

    val begin = point1

    val end = point2

    def getPointList = List(begin, end)

    def createRender(drawingContext: DrawingContext) = new LineRender(this, drawingContext)
}
