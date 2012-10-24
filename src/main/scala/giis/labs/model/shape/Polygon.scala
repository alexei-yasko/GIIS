package giis.labs.model.shape

import giis.labs.graphics.DrawingContext
import giis.labs.model.Point
import giis.labs.graphics.render.PolygonRender

/**
 * @author Q-YAA
 */
class Polygon(lines: List[Line]) extends Shape {

    private val polygonLines = List[Line]()

    /**
     * Returns point list that define the shape.
     *
     * @return List[Point] point list
     */
    def getPointList = {
        var resultList = List[Point]()
        for (line <- lines) {
            resultList = resultList ::: line.getPointList
        }
        resultList
    }

    /**
     * Move point from one position to another.
     *
     * @param from origin position
     * @param to new position
     */
    def movePoint(from: Point, to: Point) {
        for (line <- lines if line.isPointBelongsTo(from)) {
            line.movePoint(from, to)
        }
    }

    /**
     * Creates render for shape.
     *
     * @param drawingContext drawing context object
     * @return created shape render
     */
    def createRender(drawingContext: DrawingContext) = new PolygonRender(this, drawingContext)

    /**
     * Return all lines that define polygon.
     *
     * @return all lines of polygon
     */
    def getPolygonLines = lines

    def shapeType = Shape.Polygon
}
