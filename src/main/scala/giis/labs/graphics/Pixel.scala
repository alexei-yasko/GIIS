package giis.labs.graphics

import giis.labs.model.Point
import java.awt.Color

/**
 * @author Q-YAA
 */
class Pixel(pixelPoint: Point, context: DrawingContext) {

    val point = pixelPoint

    private var pixelColor = context.color

    def color = pixelColor

    def color_=(color: Color) {
        pixelColor = color
    }

    override def toString = "point {" + pixelPoint.toString + ", " + pixelColor.toString + "}"

    override def hashCode() = point.hashCode()

    override def equals(other: Any) = other match {
        case that: Pixel => this.point == that.point
        case _ => false
    }
}

object Pixel {

    def createPixel(x: Int, y: Int, drawingContext: DrawingContext): Pixel = new Pixel(new Point(x, y), drawingContext)

    def createPixel(point: Point, drawingContext: DrawingContext): Pixel = new Pixel(point, drawingContext)

    def createPixelList(pointList: List[Point], drawingContext: DrawingContext) = for (point <- pointList) yield {
        createPixel(point, drawingContext)
    }

    def appendPixelToListIfItNotInList(pixel: Pixel, pixelList: List[Pixel]): List[Pixel] = {
        if (!pixelList.contains(pixel)) {
            pixel :: pixelList
        }
        else {
            pixelList
        }
    }
}

