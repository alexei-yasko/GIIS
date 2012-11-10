package giis.labs.model.shape

import giis.labs.model.{Axis, AxisType, Point, Point3D}
import xml.XML
import giis.labs.utils.Utils3D
import giis.labs.graphics.render.Rect3DRender
import giis.labs.graphics.DrawingContext

/**
 * @author Q-YAA
 */
class Rect3D(vertexList: List[Point3D], edgeList: List[(Int, Int)], centerX: Int, centerY: Int) extends Shape {

    private val x = centerX
    private val y = centerY

    private var rotateAngleX = 0d
    private var rotateAngleY = 0d
    private var rotateAngleZ = 0d

    private var offsetX = 0
    private var offsetY = 0
    private var offsetZ = 0

    private var scaleX = 1d
    private var scaleY = 1d
    private var scaleZ = 1d

    private var project = false

    private val vertexes = vertexList.toArray

    private val edges = edgeList.toArray

    /**
     * Returns point list that define the shape.
     *
     * @return List[Point] point list
     */
    def getPointList = (for (vertex <- vertexes) yield {
//        var point = Utils3D.movePoint(vertex, offsetX, offsetY, offsetZ)
//
//        point = Utils3D.rotatePoint(point, rotateAngleX, Axis.Ox)
//        point = Utils3D.rotatePoint(point, rotateAngleY, Axis.Oy)
//        point = Utils3D.rotatePoint(point, rotateAngleZ, Axis.Oz)

        var point = Utils3D.scalePoint(vertex, scaleX, scaleY, scaleZ)

        point = Utils3D.rotatePoint(point, rotateAngleX, Axis.Ox)
        point = Utils3D.rotatePoint(point, rotateAngleY, Axis.Oy)
        point = Utils3D.rotatePoint(point, rotateAngleZ, Axis.Oz)

        point = Utils3D.movePoint(point, offsetX, offsetY, offsetZ)

        var resultPoint: Point = null
        if (project) {
            resultPoint = Utils3D.projectPoint(new Point3D(point.x + x, point.y + y, point.z))
        }
        else {
            resultPoint = Utils3D.displayFlatPoint(new Point3D(point.x + x, point.y + y, point.z))
        }

        resultPoint
    }).toList

    /**
     * Get type of the shape object.
     *
     * @return ShapeType type of the shape object
     */
    def shapeType = null

    /**
     * Creates render for shape.
     *
     * @param drawingContext drawing context object
     * @return created shape render
     */
    def createRender(drawingContext: DrawingContext) = new Rect3DRender(this, drawingContext)

    def getEdgeList: List[Line] = {
        val pointList = getPointList
        var lineList = List[Line]()

        for (edge <- edges) {

            val line = new Line(pointList(edge._1), pointList(edge._2)) {
                def shapeType = Shape.LineBrezenhem
            }

            lineList = line :: lineList
        }

        lineList.reverse
    }

    def rotate(angle: Double, axis: AxisType) {
        axis match {
            case Axis.Ox => rotateAngleX += angle
            case Axis.Oy => rotateAngleY += angle
            case Axis.Oz => rotateAngleZ += angle
        }
    }

    def move(dx: Int, dy: Int, dz: Int) {
        offsetX += dx
        offsetY += dy
        offsetZ += dz
    }

    def scale(scaleDx: Double, scaleDy: Double, scaleDz: Double) {
        scaleX += scaleDx
        scaleY += scaleDy
        scaleZ += scaleDz
    }

    def switchProject() {
        project = !project
    }
}

object Rect3D {

    def load(fileName: String): Rect3D = {
        val rectInputStream = getClass.getClassLoader.getResourceAsStream(fileName)

        val rootNode = XML.load(rectInputStream)

        val centerX = (rootNode \ "@x").text.toInt
        val centerY = (rootNode \ "@y").text.toInt

        val vertexListNode = rootNode \ "vertexes"
        val edgeListNode = rootNode \ "edges"

        var vertexList = List[Point3D]()
        for (vertexNode <- (vertexListNode \ "vertex")) {
            val x = (vertexNode \ "@x").text.toInt
            val y = (vertexNode \ "@y").text.toInt
            val z = (vertexNode \ "@z").text.toInt

            vertexList = (new Point3D(x, y, z)) :: vertexList
        }

        var edgeList = List[(Int, Int)]()
        for (edgeNode <- (edgeListNode \ "edge")) {
            val first = (edgeNode \ "@first").text.toInt
            val second = (edgeNode \ "@second").text.toInt

            edgeList = (first, second) :: edgeList
        }

        new Rect3D(vertexList.reverse, edgeList, centerX, centerY)
    }
}
