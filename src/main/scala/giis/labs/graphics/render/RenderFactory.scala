package giis.labs.graphics.render

import giis.labs.model.AlgorithmType
import java.awt.Color

/**
 * @author Q-YAA
 */
trait RenderFactory {
    def createRender(algorithmType: AlgorithmType, color: Color): Render
}
