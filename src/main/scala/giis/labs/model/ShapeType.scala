package giis.labs.model

/**
 * @author Q-YAA
 */
trait ShapeType {
    def name: String
}

object ShapeTypeList {

    case object LineDda extends ShapeType {
        val name = "LineDda"
    }

    case object LineBrezenhem extends ShapeType {
        val name = "LineBrezenhem"
    }
}
