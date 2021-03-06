package giis.labs.model.shape

import giis.labs.model.Point
import giis.labs.graphics.DrawingContext
import giis.labs.graphics.render.{LineRender, PolygonRender}

/**
 * @author Q-YAA
 */
class Polygon(vertexes: Array[Point], edges: Array[(Int, Int)]) extends Shape {

    /**
     * Returns point list that define the shape.
     *
     * @return List[Point] point list
     */
    def getPointList = vertexes.toList

    def getEdgeList: List[Line] = (for (edge <- edges) yield {
        new Line(vertexes(edge._1), vertexes(edge._2)) {
            def shapeType = Shape.LineBrezenhem
        }
    }).toList

    /**
     * Move point from one position to another.
     *
     * @param from origin position
     * @param to new position
     */
    override def movePoint(from: Point, to: Point) {

        for (i <- 0 until vertexes.length) {
            if (vertexes(i) == from) {
                vertexes(i) = to

                super.movePoint(from, to)

                return
            }
        }
    }

    /**
     * Determines the given point inside the shape or not.
     *
     * @param point point to determine
     * @return true if the point inside the shape, false in the other case
     */
    override def isPointInsideShape(point: Point) = {
        val sortedByY = getPointList.sortBy[Int](p => p.y)
        val sortedByX = getPointList.sortBy[Int](p => p.x)

        point.y > sortedByY.head.y && point.y < sortedByY.reverse.head.y &&
            point.x > sortedByX.head.x && point.x < sortedByX.reverse.head.x
    }

    /**
     * Creates render for shape.
     *
     * @param drawingContext drawing context object
     * @return created shape render
     */
    def createRender(drawingContext: DrawingContext) = new PolygonRender(this, drawingContext)

    def shapeType = Shape.Polygon
}
