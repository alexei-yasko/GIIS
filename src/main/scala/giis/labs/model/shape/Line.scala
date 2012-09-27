package giis.labs.model.shape

import java.awt.Color
import giis.labs.graphics.render.{RenderFactory, LineRender}
import giis.labs.model.{ShapeType, Point}

/**
 * @author Q-YAA
 */
class Line(point1: Point, point2: Point) extends Shape {

    val begin = point1

    val end = point2

    def getPointList = List(begin, end)

    def createRender(shapeType: ShapeType, color: Color) = new LineRender(this, color, shapeType)
}
