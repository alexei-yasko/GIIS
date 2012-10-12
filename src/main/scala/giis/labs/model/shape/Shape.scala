package giis.labs.model.shape

import giis.labs.model.{ShapeType, Point}
import giis.labs.graphics.render.RenderFactory

/**
 * @author Q-YAA
 */
abstract class Shape extends RenderFactory {
    def getPointList: List[Point]

    def isPointBelongsTo(point: Point): Boolean = getPointList.contains(point)

    def movePoint(from: Point, to: Point)
}

object Shape {

    case object LineDda extends ShapeType {
        val name = "LineDda"
        val definingPointQuantity = 2
    }

    case object LineBrezenhem extends ShapeType {
        val name = "LineBrezenhem"
        val definingPointQuantity = 2
    }

    case object Circle extends ShapeType {
        val name = "Circle"
        val definingPointQuantity = 2
    }

    case object Bezier extends ShapeType {
        val name = "Bezier"
        val definingPointQuantity = 4
    }

    case object Ermit extends ShapeType {
        val name = "Ermit"
        val definingPointQuantity = 4
    }
}