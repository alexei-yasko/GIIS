package giis.labs.model.shape

import giis.labs.model.{AlgorithmTypeList, AlgorithmType, Point}

/**
 * @author Q-YAA
 */
object ShapeFactory {
    def createShape(pointList: List[Point], algorithmType: AlgorithmType): Shape = algorithmType match {
        case AlgorithmTypeList.LineDda => createLine(pointList)
        case AlgorithmTypeList.LineBrezenhem => createLine(pointList)
    }

    private def createLine(pointList: List[Point]) : Line = {
        val beginPoint = pointList.head
        val endPoint = pointList.tail.head

        new Line(beginPoint, endPoint)
    }

}
