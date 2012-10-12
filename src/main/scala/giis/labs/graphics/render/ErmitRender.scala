package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{Pixel, DrawingContext}
import giis.labs.model.{Point, Matrix}
import scala.Array

/**
 * Render for the ermit spline shape.
 *
 * @author Q-YAA
 */
class ErmitRender(shape: Shape, drawingContext: DrawingContext) extends Render(shape, drawingContext) {

    private val ermitMatrix = new Matrix(
        Array[Array[Double]](
            Array[Double](2, -2, 1, 1),
            Array[Double](-3, 3, -2, -1),
            Array[Double](0, 0, 1, 0),
            Array[Double](1, 0, 0, 0)
        )
    )

    protected def drawShape: List[Pixel] = {
        val point1 = shape.getPointList(0)
        val point2 = shape.getPointList(1)
        val point3 = shape.getPointList(2)
        val point4 = shape.getPointList(3)

        drawErmit(point1, point2, point3, point4)
    }

    private def drawErmit(point1: Point, point2: Point, point3: Point, point4: Point): List[Pixel] = {

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

        val dt = 1.0 / (3 * (dx + dy + 1))

        for (t <- 0d to(1, dt)) {
            val tMatrix = new Matrix(
                Array[Array[Double]](
                    Array[Double](t * t * t, t * t, t, 1)
                )
            )

            val resultMatrixX = tMatrix * (ermitMatrix * pointsMatrixX)
            val resultMatrixY = tMatrix * (ermitMatrix * pointsMatrixY)

            val newPixel = Pixel.createPixel(math.round(resultMatrixX.getValue(0, 0)).toInt,
                math.round(resultMatrixY.getValue(0, 0)).toInt, drawingContext)

            resultPixelList = Pixel.appendPixelToListIfItNotInList(newPixel, resultPixelList)
        }

        resultPixelList.reverse
    }
}
