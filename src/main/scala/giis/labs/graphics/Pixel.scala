package giis.labs.graphics

import giis.labs.model.Point

/**
 * @author Q-YAA
 */
class Pixel(pixelPoint: Point, context: DrawingContext) {

    val point = pixelPoint

    val drawingContext = context

    override def toString = "point {" + pixelPoint.toString + ", " + drawingContext.toString + "}"

    override def hashCode() = point.hashCode()

    override def equals(other: Any) = other match {
        case that: Pixel => this.point == that.point
        case _ => false
    }
}
