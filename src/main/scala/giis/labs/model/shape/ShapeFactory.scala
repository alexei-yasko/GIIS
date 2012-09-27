package giis.labs.model.shape

import giis.labs.model.{ShapeTypeList, ShapeType, Point}

/**
 * @author Q-YAA
 */
object ShapeFactory {

    def createShape(pointList: List[Point], shapeType: ShapeType): Shape = shapeType match {
        case ShapeTypeList.LineDda => createLine(pointList.toArray)
        case ShapeTypeList.LineBrezenhem => createLine(pointList.toArray)
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
