package giis.labs.model.shape

/**
 * @author Q-YAA
 */
trait ShapeType {
    def name: String
}

object ShapeTypeList {

    case object LineType extends ShapeType {
        val name = "LineType"
    }
}
