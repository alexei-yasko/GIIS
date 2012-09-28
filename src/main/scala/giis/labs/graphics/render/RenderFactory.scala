package giis.labs.graphics.render

import giis.labs.graphics.DrawingContext

/**
 * @author Q-YAA
 */
trait RenderFactory {
    def createRender(drawingContext: DrawingContext): Render
}
