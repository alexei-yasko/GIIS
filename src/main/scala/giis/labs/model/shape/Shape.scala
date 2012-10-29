package giis.labs.model.shape

import giis.labs.model.{FillShapeType, ShapeType, Point}
import giis.labs.graphics.render.RenderFactory

/**
 * Abstract class for all shapes.
 *
 * @author Q-YAA
 */
abstract class Shape extends RenderFactory {

    /**
     * Returns point list that define the shape.
     *
     * @return List[Point] point list
     */
    def getPointList: List[Point]

    /**
     * Determines belong the given point to the shape or not.
     *
     * @param point point to determine
     * @return true if the point belong to the shape, false in the other case
     */
    def isPointBelongsTo(point: Point) = getPointList.contains(point)

    /**
     * Determines the given point inside the shape or not.
     *
     * @param point point to determine
     * @return true if the point inside the shape, false in the other case
     */
    def isPointInsideShape(point: Point): Boolean = false

    /**
     * Move point from one position to another.
     *
     * @param from origin position
     * @param to new position
     */
    def movePoint(from: Point, to: Point)

    /**
     * Get type of the shape object.
     *
     * @return ShapeType type of the shape object
     */
    def shapeType: ShapeType
}

/**
 * Companion object for the shape class. Contains all types of the possible shapes.
 *
 * <p>If definingPointQuantity equals -1 then they not limited.</p>
 */
object Shape {

    case object LineDda extends ShapeType {
        val name = "LineDda"
        val definingPointQuantity = 2
    }

    case object LineBrezenhem extends ShapeType {
        val name = "LineBrezenhem"
        val definingPointQuantity = 2
    }

    case object Circle extends ShapeType {
        val name = "Circle"
        val definingPointQuantity = 2
    }

    case object Bezier extends ShapeType {
        val name = "Bezier"
        val definingPointQuantity = 4
    }

    case object Ermit extends ShapeType {
        val name = "Ermit"
        val definingPointQuantity = 4
    }

    case object Hyperbola extends ShapeType {
        val name = "Hyperbola"
        val definingPointQuantity = 3
    }

    case object Polygon extends ShapeType {
        val name = "Polygon"
        val definingPointQuantity = -1
    }

    case object FillPolygonByLine extends FillShapeType {
        val name = "FillPolygonByLine"
        val definingPointQuantity = 1
    }

    case object FloodFillPolygon extends FillShapeType {
        val name = "FloodFillPolygon"
        val definingPointQuantity = 1
    }
}