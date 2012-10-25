package giis.labs.graphics.render

import giis.labs.model.shape.{Shape, Line, FillPolygon}
import giis.labs.graphics.{Pixel, DrawingContext}
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
class FillPolygonRender(
    fillPolygon: FillPolygon, drawingContext: DrawingContext, render: Render) extends Render(fillPolygon, drawingContext) {

    /**
     * Template method for the shape drawing. Concrete renders must override this method.
     *
     * @return List[Pixel] result of drawing
     */
    def drawShape = {

        var resultPixelList = List[Pixel]()

        System.out.println(render.drawShape)

        val sortedByY = render.drawShape.sortBy[Int](p => p.point.y)
        val sortedByX = sortedByY.sortBy[Int](p => p.point.x)

        val maxX = sortedByX.reverse.head.point.x
        val minX = sortedByX.head.point.x

        val maxY = sortedByY.reverse.head.point.y
        val minY = sortedByY.head.point.y

        for (i <- minY to maxY) {
            var drawCondition = false
            var isOtherLine = false

            for (j <- minX to maxX) {
                val pixels = sortedByX.filter(p => p.point.y == i && p.point.x == j)
                val previousPixels = sortedByX.find(p => p.point.y == i && p.point.x == j - 1)

                drawCondition = resolveDrawCondition(drawCondition, pixels)

                if (!pixels.isEmpty && previousPixels.isEmpty) {
                    isOtherLine = !isOtherLine
                }

                for (i <- 1 until pixels.length) {
                    isOtherLine = !isOtherLine
                }

                if (pixels.isEmpty && isOtherLine) {
                    resultPixelList = Pixel.createPixel(j, i, drawingContext.fillColor) :: resultPixelList
                }
            }

        }

//        val polygonLines = createPolygonLines(render.shape)
//
//        for (i <- minY to maxY) {
//            val intervalList = createIntervals(i, maxX, minX, polygonLines)
//
//            for (k <- 1 until intervalList.length) {
//                val left = intervalList.reverse(k - 1)
//                val right = intervalList.reverse(k)
//
//                if (left.x + 1 != right.x) {
//                    resultPixelList =
//                        resultPixelList ::: drawPixelsFromInterval(left, right)
//                }
//            }
//        }

        resultPixelList
    }

    private def resolveDrawCondition(currentDrawCondition: Boolean, currentPixels: List[Pixel]): Boolean = {
        var drawCondition = currentDrawCondition

        for (i <- 0 until currentPixels.length) {
            drawCondition = !drawCondition
        }

        drawCondition
    }

    private def drawPixelsFromInterval(left: Point, right: Point): List[Pixel] = {
        (for (i <- (left.x + 1) until right.x) yield {
            Pixel.createPixel(i, left.y, drawingContext.fillColor)
        }).toList
    }

    private def createPolygonLines(shape: Shape): Array[LineRender] = {
        val shapePointArray = shape.getPointList.toArray
        val lines = Array.ofDim[LineRender](shapePointArray.length)

        for (i <- 1 until shapePointArray.length) {
            val line = new Line(shapePointArray(i - 1), shapePointArray(i)) {
                def shapeType = Shape.LineBrezenhem
            }

            lines(i - 1) = new LineRender(line, DrawingContext.createDrawingContext)
        }

        val line = new Line(shapePointArray.last, shapePointArray.head) {
            def shapeType = Shape.LineBrezenhem
        }

        lines(shapePointArray.length - 1) = new LineRender(line, DrawingContext.createDrawingContext)

        lines
    }

    private def createIntervals(row: Int, maxX: Int, minX: Int, lines: Array[LineRender]): List[Point] = {
        var intervals = List[Point]()

        for (i <- 1 until lines.length) {
            var firstPixel: Point = null
            var secondPixel: Point = null

            val firstLine = lines(i - 1).drawShape
            val secondLine = lines(i).drawShape

            for (j <- minX to maxX) {
                val first = firstLine.find(p => p.point.y == row && p.point.x == j)

                if (!first.isEmpty) {
                    firstPixel = first.head.point
                }

                val second = secondLine.find(p => p.point.y == row && p.point.x == j)

                if (!second.isEmpty) {
                    secondPixel = second.head.point
                }
            }

            if (firstPixel != null && secondPixel != null) {
                intervals = intervals ::: List(firstPixel, secondPixel)
            }
        }

        //        for (i <- minX to maxX) {
        //
        //            for (j <- 1 until lines.length) {
        //                val firstPixels = line(j - 1).drawShape.find(p => p.point.y == row && p.point.x == i)
        //                val secondPixels
        //                if (!pixels.isEmpty) {
        //                    intervals = pixels.head.point :: intervals
        //                }
        //            }
        //
        //            //val nextPixels = sortedByX.find(p => p.point.y == i && p.point.x == j + 1)
        //            //val previousPixels = sortedByX.find(p => p.point.y == i && p.point.x == j - 1)
        //        }

        intervals
    }
}
