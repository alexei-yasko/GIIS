package giis.labs.graphics.render

import giis.labs.graphics.DrawingContext

/**
 * Trait for the creation shape renders.
 *
 * @author Q-YAA
 */
trait RenderFactory {

    /**
     * Creates render for shape.
     *
     * @param drawingContext drawing context object
     * @return created shape render
     */
    def createRender(drawingContext: DrawingContext): Render
}
