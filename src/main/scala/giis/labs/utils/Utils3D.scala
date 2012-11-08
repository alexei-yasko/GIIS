package giis.labs.utils

import giis.labs.model._
import scala.Array

/**
 * @author Q-YAA
 */
object Utils3D {

    def displayFlatPoint(point: Point3D): Point = {

        val perspectiveMatrix = new Matrix(
            Array[Array[Double]](
                Array[Double](1d, 0, 0, 0),
                Array[Double](0, 1d, 0, 0),
                Array[Double](0, 0, 1d, 0),
                Array[Double](0, 0, 0, 0)
            )
        )

        val pointMatrix = createMatrixFromPoint(point)

        val resultMatrix = pointMatrix * perspectiveMatrix

        val pointTest = new Point(
            math.round(resultMatrix.getValue(0, 0) / (resultMatrix.getValue(0, 3) + 1)).toInt,
            math.round(resultMatrix.getValue(0, 1) / (resultMatrix.getValue(0, 3) + 1)).toInt
        )

        pointTest
    }

    def projectPoint(point: Point3D): Point = {

        val perspectiveMatrix = new Matrix(
            Array[Array[Double]](
                Array[Double](1d, 0, 0, 0),
                Array[Double](0, 1d, 0, 0),
                Array[Double](0, 0, 1d, 1d / 10d),
                Array[Double](0, 0, 0, 0)
            )
        )

        val pointMatrix = createMatrixFromPoint(point)

        val resultMatrix = pointMatrix * perspectiveMatrix

        val pointTest = new Point(
            math.round(resultMatrix.getValue(0, 0) / (resultMatrix.getValue(0, 3) + 1)).toInt,
            math.round(resultMatrix.getValue(0, 1) / (resultMatrix.getValue(0, 3) + 1)).toInt
        )

        pointTest
    }

    def rotatePoint(point: Point3D, angle: Double, axis: AxisType): Point3D = {
        val radAngle = angle * math.Pi / 180.0

        val rotationMatrix = axis match {
            case Axis.Ox => new Matrix(
                Array[Array[Double]](
                    Array[Double](1d, 0, 0, 0),
                    Array[Double](0, math.cos(radAngle), math.sin(radAngle), 0),
                    Array[Double](0, -math.sin(radAngle), math.cos(radAngle), 0),
                    Array[Double](0, 0, 0, 1d)
                )
            )
            case Axis.Oy => new Matrix(
                Array[Array[Double]](
                    Array[Double](math.cos(radAngle), 0, -math.sin(radAngle), 0),
                    Array[Double](0, 1d, 0, 0),
                    Array[Double](math.sin(radAngle), 0, math.cos(radAngle), 0),
                    Array[Double](0, 0, 0, 1d)
                )
            )
            case Axis.Oz => new Matrix(
                Array[Array[Double]](
                    Array[Double](math.cos(radAngle), math.sin(radAngle), 0, 0),
                    Array[Double](-math.sin(radAngle), math.cos(radAngle), 0, 0),
                    Array[Double](0, 0, 1d, 0),
                    Array[Double](0, 0, 0, 1d)
                )
            )
        }

        val pointMatrix = createMatrixFromPoint(point)

        createPointFromMatrix(pointMatrix * rotationMatrix)
    }

    def movePoint(point: Point3D, dx: Int, dy: Int, dz: Int): Point3D = {

        val moveMatrix = new Matrix(
            Array[Array[Double]](
                Array[Double](1d, 0, 0, 0),
                Array[Double](0, 1d, 0, 0),
                Array[Double](0, 0, 1d, 0),
                Array[Double](dx, dy, dz, 1d)
            )
        )

        val pointMatrix = createMatrixFromPoint(point)

        createPointFromMatrix(pointMatrix * moveMatrix)
    }

    def scalePoint(point: Point3D, scaleX: Double, scaleY: Double, scaleZ: Double): Point3D = {

        val scaleMatrix = new Matrix(
            Array[Array[Double]](
                Array[Double](scaleX, 0, 0, 0),
                Array[Double](0, scaleY, 0, 0),
                Array[Double](0, 0, scaleZ, 0),
                Array[Double](0, 0, 0, 1d)
            )
        )

        val pointMatrix = createMatrixFromPoint(point)

        createPointFromMatrix(pointMatrix * scaleMatrix)
    }

    private def createMatrixFromPoint(point: Point3D): Matrix = {
        new Matrix(Array[Array[Double]](Array[Double](point.x, point.y, point.z, 1)))
    }

    private def createPointFromMatrix(matrix: Matrix): Point3D = new Point3D(
        math.round(matrix.getValue(0, 0) / matrix.getValue(0, 3)).toInt,
        math.round(matrix.getValue(0, 1) / matrix.getValue(0, 3)).toInt,
        math.round(matrix.getValue(0, 2) / matrix.getValue(0, 3)).toInt
    )
}
