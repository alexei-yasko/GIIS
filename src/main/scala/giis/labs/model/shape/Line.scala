package giis.labs.model.shape

import giis.labs.graphics.render.LineRender
import giis.labs.graphics.DrawingContext
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
class Line(point1: Point, point2: Point) extends Shape {

    var begin = point1

    var end = point2

    def getPointList = List(begin, end)

    def movePoint(from: Point, to: Point) {
        if (begin == from) {
            begin = to
        }
        else if (end == from) {
            end = to
        }
    }

    def createRender(drawingContext: DrawingContext) = new LineRender(this, drawingContext)
}
