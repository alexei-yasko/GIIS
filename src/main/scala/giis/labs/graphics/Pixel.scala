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

    override def hashCode() = point.hashCode()

    override def equals(other: Any) = other match {
        case that: Pixel => this.point == that.point
        case _ => false
    }
}
