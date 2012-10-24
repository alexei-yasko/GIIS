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
        case Shape.LineDda => createLine(Shape.LineDda, pointList.toArray, shapeType.definingPointQuantity)
        case Shape.LineBrezenhem => createLine(Shape.LineBrezenhem, pointList.toArray, shapeType.definingPointQuantity)
        case Shape.Circle => createCircle(pointList.toArray, shapeType.definingPointQuantity)
        case Shape.Bezier => createBezier(pointList.toArray, shapeType.definingPointQuantity)
        case Shape.Ermit => createErmit(pointList.toArray, shapeType.definingPointQuantity)
        case Shape.Hyperbola => createHyperbola(pointList.toArray, shapeType.definingPointQuantity)
        case Shape.Polygon => createPolygon(pointList.toArray)
        case null => null
    }

    private def createLine(drawingShapeType: ShapeType, pointArray: Array[Point], definingPointQuantity: Int): Line = {
        if (pointArray.length < definingPointQuantity) {
            null
        }
        else {
            new Line(pointArray(pointArray.length - 1), pointArray(pointArray.length - 2)) {
                def shapeType = drawingShapeType
            }
        }
    }

    private def createCircle(pointArray: Array[Point], definingPointQuantity: Int): Circle = {
        if (pointArray.length < definingPointQuantity) {
            null
        }
        else {
            new Circle(
                pointArray(pointArray.length - 1),
                pointArray(pointArray.length - 2)
            )
        }
    }

    private def createBezier(pointArray: Array[Point], definingPointQuantity: Int): Bezier = {
        if (pointArray.length < definingPointQuantity) {
            null
        }
        else {
            new Bezier(
                pointArray(pointArray.length - 1),
                pointArray(pointArray.length - 2),
                pointArray(pointArray.length - 3),
                pointArray(pointArray.length - 4)
            )
        }
    }

    private def createErmit(pointArray: Array[Point], definingPointQuantity: Int): Ermit = {
        if (pointArray.length < definingPointQuantity) {
            null
        }
        else {
            new Ermit(
                pointArray(pointArray.length - 1),
                pointArray(pointArray.length - 2),
                pointArray(pointArray.length - 3),
                pointArray(pointArray.length - 4)
            )
        }
    }

    private def createHyperbola(pointArray: Array[Point], definingPointQuantity: Int): Hyperbola = {
        if (pointArray.length < definingPointQuantity) {
            null
        }
        else {
            new Hyperbola(
                pointArray(pointArray.length - 1),
                pointArray(pointArray.length - 2),
                pointArray(pointArray.length - 3)
            )
        }
    }

    private def createPolygon(pointArray: Array[Point]): Polygon = {
        if (pointArray.length < 4 || pointArray(0) != pointArray(pointArray.length - 1)) {
            null
        }
        else {
            new Polygon(pointArray.reverse.splitAt(pointArray.length - 1)._1)
        }
    }
}
