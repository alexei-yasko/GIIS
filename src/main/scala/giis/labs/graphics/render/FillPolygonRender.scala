package giis.labs.graphics.render

import giis.labs.model.shape.{Line, Polygon, Shape, FillPolygon}
import giis.labs.graphics.{Pixel, DrawingContext}
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
class FillPolygonRender(
    fillPolygon: FillPolygon, drawingContext: DrawingContext, polygon: Polygon) extends Render(fillPolygon, drawingContext) {

    /**
     * Template method for the shape drawing. Concrete renders must override this method.
     *
     * @return List[Pixel] result of drawing
     */
    def drawShape = fillPolygon.shapeType match {
        case Shape.FillPolygonByLine => fillByLine
    }

    private def fillByLine: List[Pixel] = {
        val sortedByY = polygon.getPointList.sortBy[Int](p => p.y)
        val sortedByX = polygon.getPointList.sortBy[Int](p => p.x)

        val maxX = sortedByX.reverse.head.x
        val minX = sortedByX.head.x
        val maxY = sortedByY.reverse.head.y
        val minY = sortedByY.head.y

        var rowIntersections = List[(Point, Boolean)]()
        for (i <- minY to maxY) {
            rowIntersections = rowIntersections ::: findRowIntersections(i, minX, maxX)
        }

        val sortedIntervalList =
            rowIntersections.sortWith((p1, p2) => p1._1.y < p2._1.y || (p1._1.y == p2._1.y && p1._1.x <= p2._1.x))

        //System.out.println(sortedIntervalList)
        //System.out.println("\n\n")

        val filteredIntervalList = removeWastePointsFromIntervalList(sortedIntervalList)

        drawIntervalList(filteredIntervalList)
    }

    private def drawIntervalList(intervalList: List[Point]): List[Pixel] = {
        var resultPixelList = List[Pixel]()

        for (i <- 0 until intervalList.length / 2) {
            val firstIntervalPoint = intervalList(i * 2)
            val secondIntervalPoint = intervalList(i * 2 + 1)

            //System.out.println(firstIntervalPoint + "   " + secondIntervalPoint)

            for (j <- (firstIntervalPoint.x + 1) until secondIntervalPoint.x) {
                val newPixel = Pixel.createPixel(j, firstIntervalPoint.y, drawingContext.fillColor)

                if (!polygon.createRender(drawingContext).drawShape.exists(p => p.point == newPixel.point)) {
                    resultPixelList = newPixel :: resultPixelList
                }
            }
        }

        //System.out.println("\n\n")

        resultPixelList.reverse
    }

    private def removeWastePointsFromIntervalList(sortedIntervalList: List[(Point, Boolean)]): List[Point] = {
        var resultIntervalList = List[Point]()

        for (i <- 1 until sortedIntervalList.length) {
            val firstElement = sortedIntervalList(i - 1)
            val secondElement = sortedIntervalList(i)

            if (firstElement._1 == secondElement._1 && firstElement._2) {
                //resultIntervalList = resultIntervalList ::: List[Point](firstElement._1)
            }
            else {
                resultIntervalList = resultIntervalList ::: List[Point](firstElement._1)
            }
        }

        resultIntervalList
    }

    private def findRowIntersections(row: Int, minX: Int, maxX: Int): List[(Point, Boolean)] = {
        var intersectionList = List[(Point, Boolean)]()

        for (edge <- polygon.getEdgeList if edge.getPointList(0).y != edge.getPointList(1).y) {

            var intersectionPoint: Point = null
            val edgePixels: List[Pixel] = edge.createRender(drawingContext).drawShape

            val p1 = edge.getPointList(0)
            val p2 = edge.getPointList(1)

            intersectionPoint = intersection(minX, row, maxX, row, p1.x, p1.y, p2.x, p2.y)
            //            val x = p1.x + ((p2.x - p1.x) * (row - p1.y)) / (p2.y - p1.y)
            //            if (!(x > maxX || x < minX || x > math.max(p1.x, p2.x) || x < math.min(p1.x, p2.x))) {
            //                intersectionPoint = new Point(x, row)
            //            }

            //            for (i <- minX to maxX if edgePixels.exists(p => p.point.y == row && p.point.x == i) && intersectionPoint == null) {
            //                intersectionPoint = new Point(i, row)
            //            }

            val isVertex = intersectionPoint != null && polygon.getPointList.contains(intersectionPoint)
            val isVertexInLocalMinOrMax = isVertex && this.isVertexInLocalMinOrMax(intersectionPoint, polygon.getEdgeList)

            if (isVertexInLocalMinOrMax) {
                intersectionList = (intersectionPoint, false) :: intersectionList
            }
            else if (isVertex) {
                intersectionList = (intersectionPoint, true) :: intersectionList
            }
            else if (intersectionPoint != null) {
                intersectionList = (intersectionPoint, false) :: intersectionList
            }
        }

        intersectionList
    }

    private def intersection(x1: Int, y1: Int, x2: Int, y2: Int, x3: Int, y3: Int, x4: Int, y4: Int): Point = {
        val d: Double = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
        if (d == 0) {
            return null
        }

        val xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d + 0.5
        val yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d + 0.5

        if (xi > math.max(x1, x2) || xi < math.min(x1, x2) || xi > math.max(x3, x4) || xi < math.min(x3, x4)) {
            return null
        }

        if (yi > math.max(y1, y2) || yi < math.min(y1, y2) || yi > math.max(y3, y4) || yi < math.min(y3, y4)) {
            return null
        }

        new Point(xi.toInt, yi.toInt)
    }

    private def isVertexInLocalMinOrMax(vertex: Point, edgeList: List[Line]): Boolean = {
        val connectedEdges = edgeList.filter(edge => edge.getPointList.contains(vertex))

        val firstEdgeVertex = getOtherVertex(vertex, connectedEdges(0))
        val secondEdgeVertex = getOtherVertex(vertex, connectedEdges(1))

        if ((firstEdgeVertex.y > vertex.y && secondEdgeVertex.y > vertex.y) ||
            (firstEdgeVertex.y < vertex.y && secondEdgeVertex.y < vertex.y)) {

            true
        }
        else {
            false
        }
    }

    private def getOtherVertex(vertex: Point, edge: Line): Point = {
        if (edge.getPointList(0) == vertex) {
            edge.getPointList(1)
        }
        else if (edge.getPointList(1) == vertex) {
            edge.getPointList(0)
        }
        else {
            null
        }
    }
}
