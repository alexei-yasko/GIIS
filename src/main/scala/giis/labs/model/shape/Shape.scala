package giis.labs.model.shape

import giis.labs.model.{AlgorithmType, Point}
import giis.labs.graphics.render.Render
import java.awt.Color

/**
 * @author Q-YAA
 */
abstract class Shape {
    def getType: ShapeType

    def getPointList: List[Point]

    def createRender(algorithmType: AlgorithmType, color: Color): Render
}
