package giis.labs.model.shape

import giis.labs.model.{AlgorithmTypeList, AlgorithmType, Point}

/**
 * @author Q-YAA
 */
object ShapeFactory {
    def createShape(pointList: List[Point], algorithmType: AlgorithmType): Shape = algorithmType match {
        case AlgorithmTypeList.LineDda => createLine(pointList.toArray)
        case AlgorithmTypeList.LineBrezenhem => createLine(pointList.toArray)
        case null => null
    }

    private def createLine(pointArray: Array[Point]): Line = {
        if (pointArray.length < 2) {
            null
        }
        else {
            new Line(pointArray.apply(pointArray.length - 2), pointArray.apply(pointArray.length - 1))
        }
    }

}
