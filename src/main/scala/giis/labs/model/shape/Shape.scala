package giis.labs.model.shape

import giis.labs.model.{ShapeType, Point}
import giis.labs.graphics.render.RenderFactory

/**
 * @author Q-YAA
 */
abstract class Shape extends RenderFactory {
    def getPointList: List[Point]
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

}