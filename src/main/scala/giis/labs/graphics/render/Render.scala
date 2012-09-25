package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.Pixel
import java.awt.Color

/**
 * @author Q-YAA
 */
abstract class Render(renderedShape: Shape, shapeColor: Color) {
    def color = shapeColor
    def shape = renderedShape
    def render: List[Pixel]
}
