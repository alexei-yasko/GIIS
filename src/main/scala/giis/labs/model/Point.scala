package giis.labs.model

/**
 * Point.
 *
 * @author Q-YAA
 */
class Point(xCoordinate: Int, yCoordinate: Int) {

    /**
     * x coordinate of the point
     */
    val x = xCoordinate

    /**
     * y coordinate of the point
     */
    val y = yCoordinate

    override def toString = "Point {x: " + x + ", y: " + y + "}"

    override def hashCode = 41 * (41 + x) + y

    override def equals(other: Any) = other match {
        case that: Point => this.x == that.x && this.y == that.y
        case _ => false
    }
}
