package giis.labs.model.shape

import giis.labs.model.{Point, Point3D}
import xml.XML
import giis.labs.graphics.DrawingContext
import giis.labs.utils.Utils3D
import giis.labs.graphics.render.Rect3DRender

/**
 * @author Q-YAA
 */
class Rect3D(vertexList: List[Point3D], edgeList: List[(Int, Int)], centerX: Int, centerY: Int) extends Shape {

    private val x = centerX
    private val y = centerY

    private val vertexes = vertexList.toArray

    private val edges = edgeList.toArray

    /**
     * Returns point list that define the shape.
     *
     * @return List[Point] point list
     */
    def getPointList = (for (vertex <- vertexes) yield {
        val projectPoint = Utils3D.projectPoint(vertex)
        new Point(projectPoint.x + x, projectPoint.y + y)
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
        val projectVertexes = getPointList
        var lineList = List[Line]()

        for (edge <- edges) {

            val line = new Line(projectVertexes(edge._1), projectVertexes(edge._2)) {
                def shapeType = Shape.LineBrezenhem
            }

            lineList = line :: lineList
        }

        lineList.reverse
    }

    def rotate(angle: Double, rotateType: String) {
        for (i <- 0 until vertexes.length) {
            vertexes(i) = Utils3D.rotatePoint(vertexes(i), angle, rotateType)
        }
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
