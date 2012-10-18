package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{Pixel, DrawingContext}
import giis.labs.model.Point

/**
 * Render for the circle shape.
 *
 * @author AS
 */
class CircleRender(shape: Shape, drawingContext: DrawingContext) extends Render(shape, drawingContext) {

    protected def drawShape: List[Pixel] = {
        val end = shape.getPointList.toArray.apply(1)
        val center = shape.getPointList.toArray.apply(0)

        drawMichner(center, end)
    }

    //Мичнер
    private def drawMichner(center: Point, end: Point): List[Pixel] = {

        val x0 = center.x
        val y0 = center.y

        val radius: Int = math.sqrt(math.pow((end.x - center.x), 2) + math.pow((end.y - center.y), 2)).toInt

        var x = 0
        var y = radius
        var delta = 2 - 2 * radius
        var resultPixelList = List[Pixel]()
        var finalPixelList = List[Pixel]()

        delta = 3 - 2 * radius
        while (x < y) {

            resultPixelList = Pixel.createPixel(x0 + x, y0 + y, drawingContext) :: resultPixelList

            if (delta < 0) {
                delta = delta + 4 * x + 6
            }
            else {
                delta = delta + 4 * (x - y) + 10
                y = y - 1
            }
            x = x + 1
        }
        if (x == y) {
            resultPixelList = Pixel.createPixel(x0 + x, y0 + y, drawingContext) :: resultPixelList
        }

        finalPixelList = resultPixelList
        resultPixelList.foreach {
            pixelok =>
                x = pixelok.point.x - x0
                y = pixelok.point.y - y0
                finalPixelList = Pixel.createPixel(x0 + y, y0 + x, drawingContext) :: finalPixelList
                finalPixelList = Pixel.createPixel(x0 + y, y0 - x, drawingContext) :: finalPixelList
                finalPixelList = Pixel.createPixel(x0 + x, y0 - y, drawingContext) :: finalPixelList
                finalPixelList = Pixel.createPixel(x0 - x, y0 - y, drawingContext) :: finalPixelList
                finalPixelList = Pixel.createPixel(x0 - y, y0 - x, drawingContext) :: finalPixelList
                finalPixelList = Pixel.createPixel(x0 - y, y0 + x, drawingContext) :: finalPixelList
                finalPixelList = Pixel.createPixel(x0 - x, y0 + y, drawingContext) :: finalPixelList
        }

        finalPixelList.reverse
    }
}
