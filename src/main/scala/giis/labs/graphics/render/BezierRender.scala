package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{Pixel, DrawingContext}
import giis.labs.model.{Matrix, Point}

/**
 * @author Q-YAA
 */
class BezierRender(shape: Shape, drawingContext: DrawingContext) extends Render(shape, drawingContext) {

    private val bezierMatrix = new Matrix(
        Array[Array[Double]](
            Array[Double](-1, 3, -3, 1),
            Array[Double](3, -6, 3, 0),
            Array[Double](-3, 3, 0, 0),
            Array[Double](1, 0, 0, 0)
        )
    )

    protected def drawShape: List[Pixel] = {
        val point1 = shape.getPointList(0)
        val point2 = shape.getPointList(1)
        val point3 = shape.getPointList(2)
        val point4 = shape.getPointList(3)

        drawBezier(point1, point2, point3, point4)
    }

    private def drawBezier(point1: Point, point2: Point, point3: Point, point4: Point): List[Pixel] = {

        var resultPixelList = List[Pixel]()

        val pointsMatrixX = new Matrix(
            Array[Array[Double]](
                Array(point1.x), Array(point2.x), Array(point3.x), Array(point4.x)
            )
        )

        val pointsMatrixY = new Matrix(
            Array[Array[Double]](
                Array(point1.y), Array(point2.y), Array(point3.y), Array(point4.y)
            )
        )

        val minX = math.min(math.min(point1.x, point2.x), math.min(point3.x, point4.x))
        val maxX = math.max(math.max(point1.x, point2.x), math.max(point3.x, point4.x))

        val minY = math.min(math.min(point1.y, point2.y), math.min(point3.y, point4.y))
        val maxY = math.max(math.max(point1.y, point2.y), math.max(point3.y, point4.y))

        val dx: Double = math.abs(maxX - minX)
        val dy: Double = math.abs(maxY - minY)

        val dt: Double = 1 / ((dx + 1) * (dy + 1))

        for (t <- 0d to(1, dt)) {
            val tMatrix = new Matrix(
                Array[Array[Double]](
                    Array[Double](t * t * t, t * t, t, 1)
                )
            )

            val resultMatrixX = tMatrix * (bezierMatrix * pointsMatrixX)
            val resultMatrixY = tMatrix * (bezierMatrix * pointsMatrixY)


            val newPixel = createPixel(
                math.round(resultMatrixX.getValue(0, 0)).toInt, math.round(resultMatrixY.getValue(0, 0)).toInt, drawingContext)

            resultPixelList = appendPixelToList(newPixel, resultPixelList)
        }

        resultPixelList.reverse
    }

    private def appendPixelToList(pixel: Pixel, pixelList: List[Pixel]): List[Pixel] = {
        if (!pixelList.contains(pixel)) {
            pixel :: pixelList
        }
        else {
            pixelList
        }
    }
}
