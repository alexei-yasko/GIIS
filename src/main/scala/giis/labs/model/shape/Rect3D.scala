package giis.labs.model.shape

import giis.labs.model.Point3D
import xml.XML
import giis.labs.graphics.DrawingContext

/**
 * @author Q-YAA
 */
class Rect3D(vertexList: List[Point3D], edgeList: List[(Int, Int)]) extends Shape {

    private val vertexes = vertexList

    private val edges = edgeList

    /**
     * Returns point list that define the shape.
     *
     * @return List[Point] point list
     */
    def getPointList = null

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
    def createRender(drawingContext: DrawingContext) = null
}

object Rect3D {

    def load(fileName: String): Rect3D = {
        val rectInputStream = getClass.getClassLoader.getResourceAsStream(fileName)

        val rootNode = XML.load(rectInputStream)

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

        new Rect3D(vertexList.reverse, edgeList)
    }
}
