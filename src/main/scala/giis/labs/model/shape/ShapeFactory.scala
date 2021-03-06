package giis.labs.model.shape

import giis.labs.model.{ShapeType, Point}
import giis.labs.model.shape.Shape.Polygon

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
        case Shape.Nothing => null
        case _ => null
    }

    def createFillShape(shapeType: ShapeType, pointList: List[Point], shape: Shape): Shape = shapeType match {
        case Shape.FillPolygonByLine => createFillPolygon(shapeType, pointList.toArray, shape)
        case Shape.FloodFillPolygon => createFillPolygon(shapeType, pointList.toArray, shape)
        case _ => null
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

            val edgeArray = Array.ofDim[(Int, Int)](pointArray.length - 1)

            for (i <- 1 until pointArray.length - 1) {
                edgeArray(i - 1) = (i - 1, i)
            }
            edgeArray(pointArray.length - 2) = (pointArray.length - 2, 0)

            new Polygon(pointArray.reverse.splitAt(pointArray.length - 1)._1, edgeArray)
        }
    }

    private def createFillPolygon(fillType: ShapeType, pointArray: Array[Point], shape: Shape): FillPolygon = {
        if (pointArray.length < 1 || shape == null || !shape.isInstanceOf[Polygon]) {
            null
        }
        else {
            new FillPolygon(pointArray.head, shape.asInstanceOf[Polygon]) {
                def shapeType = fillType
            }
        }

    }
}
