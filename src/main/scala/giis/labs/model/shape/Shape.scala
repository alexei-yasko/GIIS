package giis.labs.model.shape

import giis.labs.model.Point
import giis.labs.graphics.render.RenderFactory

/**
 * @author Q-YAA
 */
abstract class Shape extends RenderFactory {

    def getPointList: List[Point]
}
