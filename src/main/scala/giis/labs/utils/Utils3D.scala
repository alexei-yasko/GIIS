package giis.labs.utils

import giis.labs.model.{Point, Point3D, Matrix}
import scala.Array

/**
 * @author Q-YAA
 */
object Utils3D {

    def projectPoint(point: Point3D): Point = {

        val perspectiveMatrix = new Matrix(
            Array[Array[Double]](
                Array[Double](1d, 0, 0, 0),
                Array[Double](0, 1d, 0, 0),
                Array[Double](0, 0, 1d, 0),
                Array[Double](0, 0, 0, 0)
            )
        )

        val pointMatrix = createPointMatrix(point)

        val resultMatrix = pointMatrix * perspectiveMatrix

        val pointTest = new Point(
            math.round(resultMatrix.getValue(0, 0) / (resultMatrix.getValue(0, 3) + 1)).toInt,
            math.round(resultMatrix.getValue(0, 1) / (resultMatrix.getValue(0, 3) + 1)).toInt
        )

        pointTest
    }

    def rotatePoint(point: Point3D, angle: Double, rotateType: String): Point3D = {
        val radAngle = angle * math.Pi / 180.0

        val xRotationMatrix = rotateType match {
            case "Ox" => new Matrix(
                Array[Array[Double]](
                    Array[Double](1d, 0, 0, 0),
                    Array[Double](0, math.cos(radAngle), math.sin(radAngle), 0),
                    Array[Double](0, -math.sin(radAngle), math.cos(radAngle), 0),
                    Array[Double](0, 0, 0, 1)
                )
            )
            case "Oy" => new Matrix(
                Array[Array[Double]](
                    Array[Double](math.cos(radAngle), 0, -math.sin(radAngle), 0),
                    Array[Double](0, 1, 0, 0),
                    Array[Double](math.sin(radAngle), 0, math.cos(radAngle), 0),
                    Array[Double](0, 0, 0, 1)
                )
            )
            case "Oz" => new Matrix(
                Array[Array[Double]](
                    Array[Double](math.cos(radAngle), math.sin(radAngle), 0, 0),
                    Array[Double](-math.sin(radAngle), math.cos(radAngle), 0, 0),
                    Array[Double](0, 0, 1, 0),
                    Array[Double](0, 0, 0, 1)
                )
            )
        }

        val pointMatrix = createPointMatrix(point)

        val resultMatrix = pointMatrix * xRotationMatrix

        new Point3D(
            math.round(resultMatrix.getValue(0, 0) / resultMatrix.getValue(0, 3)).toInt,
            math.round(resultMatrix.getValue(0, 1) / resultMatrix.getValue(0, 3)).toInt,
            math.round(resultMatrix.getValue(0, 2) / resultMatrix.getValue(0, 3)).toInt
        )
    }

    private def createPointMatrix(point: Point3D): Matrix = {
        new Matrix(Array[Array[Double]](Array[Double](point.x, point.y, point.z, 1)))
    }
}
