package giis.labs.utils

import giis.labs.model.{PointFloat, Point}
import giis.labs.model.shape.{Shape, ShapeFactory}
import giis.labs.graphics.{GraphicsScene, DrawingContext}
import scala.Float
import java.awt.Color

/**
 * @author cidyuk
 */
class Clipping(graphicsScene: GraphicsScene) {

    def clip(polygonList: List[Point], pointList: List[Point]) {
        val point1 = pointList.head
        val point2 = pointList.tail.head

        var listLine = List[giis.labs.model.Point]()
        listLine = point1 :: listLine
        listLine = point2 :: listLine

        val shapeLine = ShapeFactory.createShape(listLine, Shape.LineBrezenhem)
        DrawingContext.color_=(Color.LIGHT_GRAY)
        graphicsScene.addShapeRender(shapeLine.createRender(DrawingContext.createDrawingContext))

        val normals = getNormals(polygonList.toArray)
        val polygonArray = polygonList.toArray

        var t1: Float = 0
        var t2: Float = 1

        // compute the direction vector
        val dirV = new PointFloat(point2.x - point1.x, point2.y - point1.y)
        var visible = true
        var i = 0

        while (i < polygonList.size && visible) {

            val q0 = new PointFloat(point1.x - polygonArray.apply(i).x, point1.y - polygonArray.apply(i).y)
            val qi = dotProduct(normals.apply(i), q0)
            val pi = dotProduct(normals.apply(i), dirV)

            if (pi == 0) {
                // Parallel or Point
                // parallel - if outside then forget the line; if inside then there are no
                // intersections with this side
                // but there may be with other edges, so in this case just keep going
                if (qi > 0) {
                    //   Parallel and outside or point (p1 == p2) and outside
                    visible = false
                }
            } else {
                val t = -(qi.toFloat / pi.toFloat)
                if (pi < 0) {
                    // entering
                    if (t > t1) {
                        t1 = t
                    }
                } else {
                    if (t < t2) {
                        // exiting
                        t2 = t
                    }
                }
            }
            i = i + 1
        }

        if (t1 <= t2 && t1 >= 0 && t2 <= 1) {
            val p11 =
                new Point(math.round(point1.x.toFloat + t1 * dirV.x.toFloat), math.round(point1.y.toFloat + t1 * dirV.y.toFloat))

            val p22 =
                new Point(math.round(point1.x.toFloat + t2 * dirV.x.toFloat), math.round(point1.y.toFloat + t2 * dirV.y.toFloat))

            var listLine = List[giis.labs.model.Point]()
            listLine = p11 :: listLine
            listLine = p22 :: listLine

            val shapeLine = ShapeFactory.createShape(listLine, Shape.LineBrezenhem)
            DrawingContext.color_=(Color.BLACK)
            graphicsScene.addShapeRender(shapeLine.createRender(DrawingContext.createDrawingContext))
        } else {
            visible = false
        }
    }

    def getNormals(polygonList: Array[Point]): Array[PointFloat] = {
        var normals = List[PointFloat]()
        var d = polygonList.length
        for (i <- 0 to (polygonList.length - 1)) {
            val j = (i + 1) % polygonList.length
            val k = (i + 2) % polygonList.length

            val p1 = new PointFloat(-(polygonList.apply(j).y.toFloat - polygonList.apply(i).y.toFloat) /
                (polygonList.apply(j).x.toFloat - polygonList.apply(i).x.toFloat), 1)

            val v1 = new PointFloat(polygonList.apply(k).x.toFloat - polygonList.apply(i).x.toFloat,
                polygonList.apply(k).y.toFloat - polygonList.apply(i).y.toFloat)

            // inner normal
            var koef: Float = 0

            if (dotProduct(p1, v1) > 0)
                koef = -1
            else koef = 1

            normals = new PointFloat(koef * p1.x.toFloat, koef) :: normals
        }
        normals.reverse.toArray
    }

    def dotProduct(v1: PointFloat, v2: PointFloat): Float = {
        v1.x * v2.x + v1.y * v2.y
    }
}
