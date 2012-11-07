package giis.labs.model

/**
 * @author Q-YAA
 */
class Point3D(val x: Int, val y: Int, val z: Int) {

    override def toString = "Point3D {x: " + x + ", y: " + y + ", z: " + z + "}"

    override def hashCode = 41 * (41 + x) + 51* (51 + y) + z

    override def equals(other: Any) = other match {
        case that: Point3D => this.x == that.x && this.y == that.y && this.z == that.z
        case _ => false
    }
}
