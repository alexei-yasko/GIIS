package giis.labs.graphics.render

import giis.labs.model.shape.{Line, Shape, Polygon, FillPolygon}
import giis.labs.graphics.{Pixel, DrawingContext}
import giis.labs.model.Point
import collection.mutable.ArrayBuffer
import collection.mutable

/**
 * Render for polygon fill
 *
 * @author Q-YAA
 */
class FillPolygonRender(
    fillPolygon: FillPolygon, drawingContext: DrawingContext, polygon: Polygon) extends Render(fillPolygon, drawingContext) {

    private val isCacheOn = false
    private var drawingCache: List[Pixel] = null

    /**
     * Draw shape that represented by the shape render.
     *
     * @return List[Pixel] result of drawing
     */
    override def draw: List[Pixel] = isCacheOn match {
        case true => if (drawingCache == null || polygon.isStateUpdated) {
            polygon.changeUpdateState
            drawingCache = drawShape ::: Pixel.createPixelList(shape.getPointList, mainPixelsDrawingContext.color)
            drawingCache
        } else {
            drawingCache
        }
        case false => drawShape ::: Pixel.createPixelList(shape.getPointList, mainPixelsDrawingContext.color)
    }

    /**
     * Template method for the shape drawing. Concrete renders must override this method.
     *
     * @return List[Pixel] result of drawing
     */
    def drawShape: List[Pixel] = {
        try {
            fillPolygon.shapeType match {
                case Shape.FillPolygonByLine => fillByLine
                case Shape.FloodFillPolygon => floodFill
            }
        }
        catch {
            case ex: ArrayIndexOutOfBoundsException => List[Pixel]()
        }
    }

    /**
     * Update shape render state. It simply indicates that the model changed.
     */
    override def update() {
        drawingCache = drawShape
    }

    private def floodFill: List[Pixel] = {
        val sortedByY = polygon.getPointList.sortBy[Int](p => p.y)
        val sortedByX = polygon.getPointList.sortBy[Int](p => p.x)

        val maxX = sortedByX.reverse.head.x
        val minX = sortedByX.head.x
        val maxY = sortedByY.reverse.head.y
        val minY = sortedByY.head.y

        val resultMatrix = Array.ofDim[Boolean](maxY - minY + 1, maxX - minX + 1)

        val floodPoint = shape.getPointList.head

        val polygonPixels = polygon.createRender(drawingContext).drawShape

        val resultPixels = ArrayBuffer[Pixel]()
        val floodStack = mutable.Stack[Point]()
        floodStack.push(floodPoint)

        while (!floodStack.isEmpty) {
            val currentPoint = floodStack.pop()

            resultMatrix(currentPoint.y - minY)(currentPoint.x - minX) = true
            resultPixels.append(Pixel.createPixel(currentPoint, drawingContext.fillColor))

            checkAndPushPoint(
                new Point(currentPoint.x + 1, currentPoint.y), floodStack, polygonPixels, resultMatrix, minX, minY)
            checkAndPushPoint(
                new Point(currentPoint.x, currentPoint.y + 1), floodStack, polygonPixels, resultMatrix, minX, minY)
            checkAndPushPoint(
                new Point(currentPoint.x - 1, currentPoint.y), floodStack, polygonPixels, resultMatrix, minX, minY)
            checkAndPushPoint(
                new Point(currentPoint.x, currentPoint.y - 1), floodStack, polygonPixels, resultMatrix, minX, minY)
        }

        resultPixels.toList
    }

    private def checkAndPushPoint(
        point: Point, floodStack: mutable.Stack[Point], polygonPixels: List[Pixel], resultMatrix: Array[Array[Boolean]],
        minX: Int, minY: Int) {

        if (resultMatrix(point.y - minY)(point.x - minX) != true && polygonPixels.find(p => p.point == point).isEmpty &&
            !floodStack.contains(point)) {

            floodStack.push(point)
        }
    }

    /**
     * Fill polygon by the line
     *
     * @return result pixels
     */
    private def fillByLine: List[Pixel] = {
        val sortedByY = polygon.getPointList.sortBy[Int](p => p.y)
        val sortedByX = polygon.getPointList.sortBy[Int](p => p.x)

        val maxX = sortedByX.reverse.head.x
        val minX = sortedByX.head.x
        val maxY = sortedByY.reverse.head.y
        val minY = sortedByY.head.y

        var rowIntersections = List[(Point, Point, Boolean)]()
        for (i <- minY to maxY) {
            rowIntersections = rowIntersections ::: findRowAndEdgesIntersections(i, minX, maxX)
        }

        val sortedIntervalList = sortIntersections(rowIntersections)

        val filteredIntervalList = filterRepeatedVertexPoints(sortedIntervalList)

        drawIntervalList(filteredIntervalList)
    }

    private def drawIntervalList(intervalList: List[(Point, Point)]): List[Pixel] = {
        var resultPixelList = List[Pixel]()

        var i = 1d
        while (i < (intervalList.length - 1) / 2) {
            var firstIntervalPoint = intervalList((i * 2).toInt)
            var secondIntervalPoint = intervalList((i * 2 + 1).toInt)

            // Skip wrong points in the interval
            if (firstIntervalPoint._2.y > secondIntervalPoint._1.y) {
                firstIntervalPoint = secondIntervalPoint
                secondIntervalPoint = intervalList((i * 2 + 2).toInt)
                i += 0.5
            }

            if (firstIntervalPoint._2.y < secondIntervalPoint._1.y) {
                secondIntervalPoint = firstIntervalPoint
                firstIntervalPoint = intervalList((i * 2 - 1).toInt)
                i -= 0.5
            }

            System.out.println(firstIntervalPoint + "   " + secondIntervalPoint)

            val from = firstIntervalPoint._2
            val to = secondIntervalPoint._1

            // Draw points that belongs to the found interval
            val polygonPixels = polygon.createRender(drawingContext).drawShape
            for (j <- (from.x + 1) until to.x) {
                val newPixel = Pixel.createPixel(j, from.y, drawingContext.fillColor)

                if (!polygonPixels.exists(p => p.point == newPixel.point)) {
                    resultPixelList = newPixel :: resultPixelList
                }
            }

            i += 1
        }

        resultPixelList.reverse
    }

    private def sortIntersections(intersection: List[(Point, Point, Boolean)]): List[(Point, Point, Boolean)] = {
        intersection.sortWith(
            (p1, p2) => (p1._1.y < p2._1.y || (p1._1.y == p2._1.y && p1._1.x <= p2._1.x))
        )
    }

    private def filterRepeatedVertexPoints(sortedIntervalList: List[(Point, Point, Boolean)]): List[(Point, Point)] = {
        var resultIntervalList = List[(Point, Point)]()

        System.out.println(sortedIntervalList)
        System.out.println("\n\n\n")

        var i = 1
        while (i < sortedIntervalList.length) {
            val firstElement = sortedIntervalList(i - 1)
            val secondElement = sortedIntervalList(i)

            // check if interval elements partially equals
            val check = firstElement._1 == secondElement._1 || firstElement._1 == secondElement._2 ||
                firstElement._2 == secondElement._2 || firstElement._2 == secondElement._1

            // if interval elements partially equals,
            // and they belongs to the vertex but doesn't placed in the local minimum or maximum, leave only the left element
            if ((check && firstElement._3)) {
                resultIntervalList = resultIntervalList ::: List[(Point, Point)]((firstElement._1, firstElement._2))
                i += 1
            }
            else {
                resultIntervalList = resultIntervalList ::: List[(Point, Point)]((firstElement._1, firstElement._2))
            }

            i += 1
        }

        resultIntervalList ::: List[(Point, Point)]((sortedIntervalList.reverse.head._1, sortedIntervalList.reverse.head._2))
    }

    private def findRowAndEdgesIntersections(row: Int, minX: Int, maxX: Int): List[(Point, Point, Boolean)] = {
        // (firstPoint, secondPoint, isMinOrMax)
        // firstPoint - this is beginning of the set of intersection points
        //
        // secondPoint - this is end of the set of intersection points
        //
        // isMinOrMax - this is flag that shows if intersection points contains vertex
        // that placed in local minimum or maximum (true - if contains or points doesn't belong to the vertex,
        // false - in the other case)

        var intersectionList = List[(Point, Point, Boolean)]()

        for (edge <- polygon.getEdgeList) {
            val p1 = edge.getPointList(0)
            val p2 = edge.getPointList(1)

            val intersectionPoints = intersection(edge, row, minX, maxX)

            // check if some of the intersection points belongs to the vertex
            val isSomePointVertex = intersectionPoints != null &&
                (isVertex(intersectionPoints._1, polygon) || isVertex(intersectionPoints._2, polygon))

            // check if intersection points placed in local minimum or maximum
            val isFirstVertexInLocalMinOrMax = intersectionPoints != null &&
                isVertex(intersectionPoints._1, polygon) && isVertexInLocalMinOrMax(intersectionPoints._1, polygon.getEdgeList)

            val isSecondVertexInLocalMinOrMax = intersectionPoints != null &&
                isVertex(intersectionPoints._2, polygon) && isVertexInLocalMinOrMax(intersectionPoints._2, polygon.getEdgeList)

            val isSomePointInLocalMinOrMax = isFirstVertexInLocalMinOrMax || isSecondVertexInLocalMinOrMax

            // create intersections interval elements
            if (isSomePointInLocalMinOrMax) {
                intersectionList = (intersectionPoints._1, intersectionPoints._2, false) :: intersectionList
            }
            else if (isSomePointVertex) {
                intersectionList = (intersectionPoints._1, intersectionPoints._2, true) :: intersectionList
            }
            else if (intersectionPoints != null) {
                intersectionList = (intersectionPoints._1, intersectionPoints._2, false) :: intersectionList
            }
        }

        intersectionList
    }

    private def isVertex(point: Point, polygon: Polygon): Boolean = {
        polygon.getPointList.contains(point)
    }

    private def intersection(edge: Line, row: Int, minX: Int, maxX: Int): (Point, Point) = {
        val intersectionPixels = edge.createRender(drawingContext).drawShape
            .filter(p => p.point.y == row && p.point.x >= minX && p.point.x <= maxX)

        if (intersectionPixels.length > 1) {
            (intersectionPixels.head.point, intersectionPixels.reverse.head.point)
        }
        else if (intersectionPixels.length == 1) {
            (intersectionPixels.head.point, intersectionPixels.head.point)
        }
        else {
            null
        }
    }

    private def isVertexInLocalMinOrMax(vertex: Point, edgeList: List[Line]): Boolean = {
        val connectedEdges = edgeList.filter(edge => edge.getPointList.contains(vertex))

        if (connectedEdges.length < 2) {
            return false
        }

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
