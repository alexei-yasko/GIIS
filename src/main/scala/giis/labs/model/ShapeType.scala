package giis.labs.model

/**
 * Shape type.
 *
 * @author Q-YAA
 */
trait ShapeType {

    /**
     * Returns the name of shape type.
     *
     * @return name of the shape type
     */
    def name: String

    /**
     * Returns the quantity of the points that must define shape of this type.
     *
     * @return quantity of points
     */
    def definingPointQuantity: Int
}
