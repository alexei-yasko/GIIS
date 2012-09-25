package giis.labs.model

/**
 * @author Q-YAA
 */
trait AlgorithmType {
    def name: String
}

object AlgorithmTypeList {

    case object LineDda extends AlgorithmType {
        val name = "LineDda"
    }

    case object LineBrezenhem extends AlgorithmType {
        val name = "LineBrezenhem"
    }
}
