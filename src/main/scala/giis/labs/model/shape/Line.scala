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

    override def movePoint(from: Point, to: Point) {
        if (begin == from) {
            begin = to
        }
        else if (end == from) {
            end = to
        }
    }

    override def isPointInsideShape(point: Point) = {
        point.x == begin.x && point.y == begin.y || point.x == end.x && point.y == end.y
    }

    override def setPoints(points: List[Point]) {
        begin = points.head
        end = points.tail.head
    }

    def createRender(drawingContext: DrawingContext) = new LineRender(this, drawingContext)
}


