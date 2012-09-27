package giis.labs.model

/**
 * @author Q-YAA
 */
trait ShapeType {
    def name: String
    def definingPointQuantity: Int
}

object ShapeTypeList {

    case object LineDda extends ShapeType {
        val name = "LineDda"
        val definingPointQuantity = 2
    }

    case object LineBrezenhem extends ShapeType {
        val name = "LineBrezenhem"
        val definingPointQuantity = 2
    }
}
