package giis.labs.model

/**
 * Point.
 *
 * @author Q-YAA
 */
class PointFloat(xCoordinate: Float, yCoordinate: Float) {

    /**
     * x coordinate of the point
     */
    val x: Float = xCoordinate

    /**
     * y coordinate of the point
     */
    val y: Float = yCoordinate

    override def toString = "PointFloat {x: " + x + ", y: " + y + "}"

    override def equals(other: Any) = other match {
        case that: Point => this.x == that.x && this.y == that.y
        case _ => false
    }
}
