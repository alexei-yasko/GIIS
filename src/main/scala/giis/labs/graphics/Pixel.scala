package giis.labs.graphics

import java.awt.Color
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
class Pixel(pixelPoint: Point, pixelColor: Color) {

    val point = pixelPoint

    val color = pixelColor

    override def toString = "point: " + pixelPoint.toString + ", color: " + pixelColor
}
