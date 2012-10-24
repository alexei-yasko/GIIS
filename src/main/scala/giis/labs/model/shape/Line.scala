package giis.labs.model.shape

import giis.labs.graphics.render.LineRender
import giis.labs.graphics.DrawingContext
import giis.labs.model.{ShapeType, Point}

/**
 * Line shape.
 *
 * @author Q-YAA
 */
abstract class Line(point1: Point, point2: Point) extends Shape {

    private var begin = point1

    private var end = point2

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


