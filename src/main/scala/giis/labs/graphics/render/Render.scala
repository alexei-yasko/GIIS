package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{DrawingContext, Pixel}

/**
 * @author Q-YAA
 */
abstract class Render(renderedShape: Shape, context: DrawingContext) {
    def drawingContext = context

    def shape = renderedShape

    def draw: List[Pixel]
}
