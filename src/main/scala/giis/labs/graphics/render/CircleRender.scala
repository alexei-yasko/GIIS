package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{Pixel, DrawingContext}
import giis.labs.model.Point

/**
 * @author AS
 */
class CircleRender(shape: Shape, drawingContext: DrawingContext) extends Render(shape, drawingContext) {

    private val end = shape.getPointList.toArray.apply(0)
    private val center = shape.getPointList.toArray.apply(1)

    def draw: List[Pixel] = {

        val x0 = center.x
        val y0 = center.y

        val radius: Int = scala.math.sqrt(scala.math.pow((end.x - center.x), 2) + scala.math.pow((end.y - center.y), 2)).toInt

        var x = 0
        var y = radius
        var delta = 2 - 2 * radius
        var error = 0
        var resultPixelList = List[Pixel]()

        //Мичнер
        delta = 3 - 2 * radius;
        while (x < y) {

            resultPixelList = createPixel(x0 + x, y0 + y, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 + y, y0 + x, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 + y, y0 - x, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 + x, y0 - y, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - x, y0 - y, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - y, y0 - x, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - y, y0 + x, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - x, y0 + y, drawingContext) :: resultPixelList

            if (delta < 0) {
                delta = delta + 4 * x + 6
            }
            else {
                delta = delta + 4 * (x - y) + 10;
                y = y - 1;
            }
            x = x + 1;
        }
        if (x == y) {
            resultPixelList = createPixel(x0 + x, y0 + y, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 + y, y0 + x, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 + y, y0 - x, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 + x, y0 - y, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - x, y0 - y, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - y, y0 - x, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - y, y0 + x, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - x, y0 + y, drawingContext) :: resultPixelList
        }
        resultPixelList

        //Брезенхем
        /*        while (y >= 0) {
            resultPixelList = createPixel(x0 + x, y0 + y, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 + x, y0 - y, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - x, y0 + y, drawingContext) :: resultPixelList
            resultPixelList = createPixel(x0 - x, y0 - y, drawingContext) :: resultPixelList
            error = 2 * (delta + y) - 1
            if (delta < 0 && error <= 0) {
                x = x + 1
                delta += 2 * x + 1
            }
            else {
                error = 2 * (delta - x) - 1
                if (delta > 0 && error > 0) {
                    y = y - 1
                    delta += 1 - 2 * y
                }
                else {
                    x = x + 1
                    delta += 2 * (x - y)
                    y = y - 1
                }
            }
        }
        resultPixelList*/
    }

    private def createPixel(x: Int, y: Int, drawingContext: DrawingContext): Pixel = new Pixel(new Point(x, y), drawingContext)
}
