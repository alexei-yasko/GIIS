package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.model.{Point, AlgorithmTypeList, AlgorithmType}
import java.awt.Color
import giis.labs.graphics.Pixel

/**
 * @author Q-YAA
 */
class LineRender(shape: Shape, color: Color, algorithmType: AlgorithmType) extends Render(shape, color) {

    def render: List[Pixel] = algorithmType match {
        case AlgorithmTypeList.LineDda => ddaRender
        case AlgorithmTypeList.LineBrezenhem => brezenhemRender
    }

    def ddaRender: List[Pixel] = {
        List(new Pixel(new Point(0, 0), color), new Pixel(new Point(1, 1), color))
    }

    def brezenhemRender: List[Pixel] = {
        null
    }
}
