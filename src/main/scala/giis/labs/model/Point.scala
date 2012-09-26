package giis.labs.model

/**
 * @author Q-YAA
 */
class Point(xCoordinate: Int, yCoordinate: Int) {

    val x = xCoordinate

    val y = yCoordinate

    override def toString = "x: " + x + ", y: " + y

    override def hashCode = 41 * (41 + x) + y

    override def equals(other: Any) = other match {
        case that: Point => this.x == that.x && this.y == that.y
        case _ => false
    }
}
