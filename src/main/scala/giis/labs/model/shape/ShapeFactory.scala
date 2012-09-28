package giis.labs.model.shape

import giis.labs.model.{ShapeType, Point}

/**
 * Shape factory object.
 *
 * <p>Creates the shape of the given type from the given list of the points.</p>
 *
 * @author Q-YAA
 */
object ShapeFactory {

    /**
     * Creates the shape.
     *
     * @param pointList list of the points that must define shape
     * @param shapeType type of the shape
     * @return created shape object
     */
    def createShape(pointList: List[Point], shapeType: ShapeType): Shape = shapeType match {
        case Shape.LineDda => createLine(pointList.toArray)
        case Shape.LineBrezenhem => createLine(pointList.toArray)
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
