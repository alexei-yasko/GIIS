package giis.labs.model.shape

import giis.labs.model.{AlgorithmType, Point}
import java.awt.Color
import giis.labs.graphics.render.{RenderFactory, LineRender}

/**
 * @author Q-YAA
 */
class Line(point1: Point, point2: Point) extends Shape with RenderFactory {

    val begin = point1

    val end = point2

    def getPointList = List(begin, end)

    def getType = ShapeTypeList.LineType

    def createRender(algorithmType: AlgorithmType, color: Color) = new LineRender(this, color, algorithmType)
}
