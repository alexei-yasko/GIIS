package giis.labs.graphics.render

import giis.labs.model.ShapeType
import java.awt.Color

/**
 * @author Q-YAA
 */
trait RenderFactory {
    def createRender(shapeType: ShapeType, color: Color): Render
}
