package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{Pixel, DrawingContext}
import giis.labs.model.Point

/**
 * Render for the hyperbola shape.
 *
 * @author AS
 */
class HyperbolaRender(shape: Shape, drawingContext: DrawingContext) extends Render(shape, drawingContext) {

    def drawShape: List[Pixel] = {
        val start = shape.getPointList.toArray(0)
        val end = shape.getPointList.toArray(1)
        val height = shape.getPointList.toArray(2)

        drawHyperbola(start, end, height)
    }

    /*
    * метод,реализующий отрисовку гиперболы
    */
    private def drawHyperbola(start: Point, end: Point, height: Point): List[Pixel] = {

        var resultPixelList = List[Pixel]()
        var finalPixelList = List[Pixel]()

        // координаты точек, заданных пользователем
        val ax = start.x
        val bx = end.x
        val ay = start.y
        val by = end.y
        var hy = height.y - start.y

        // параметры а и b из уравнения гиперболы
        val a = math.abs(bx - ax)
        val b = math.abs(by - ay)

        // координаты начальной точки
        var x = 0
        var y = 0

        // сдвиг точек
        var lim = 0
        var limx = 0

        if (ay > by) {
            y = b
            lim = 2 * ay - by - y
            limx = bx
        } else {
            y = b
            lim = 2 * by - ay - 2 * y
            limx = bx
        }
        // начальное значение ошибки
        var delta = b * b - 2 * a * a * b - a * a
        var sigma = 0

        resultPixelList = Pixel.createPixel(x + limx, y + lim, drawingContext.color) :: resultPixelList

        // проверяем знак ошибки
        while (hy > 0) {
            hy = hy - 1
            // если ошибка положительна, выбираем между вертикальным и диогональным пикселями
            if (delta > 0) {
                // для определения того, какой из пикселей выбрать вычисляем разность между ними
                sigma = 2 * delta - b * b * (2 * x + 1)
                //если разность меньше нуля, выбираем диогональнай пиксель
                if (sigma < 0) {
                    x = x + 1
                    y = y + 1
                    //  пересчитываем ошибку
                    delta = delta + b * b * (2 * x + 1) - a * a * (2 * y + 1)
                    resultPixelList = Pixel.createPixel(x + limx, y + lim, drawingContext.color) :: resultPixelList
                }
                // если разность положительна, выбираем вертикальный пиксель
                else {
                    y = y + 1
                    // пересчитываем ошибку
                    delta = delta - a * a * (2 * y + 1)
                    resultPixelList = Pixel.createPixel(x + limx, y + lim, drawingContext.color) :: resultPixelList
                }

            }
            // если ошибка отрицательна, выбираем между горизонтальным и диогональным пикселями
            else if (delta < 0) {
                // для определения того, какой из пикселей выбрать вычисляем разность между ними
                sigma = 2 * delta + a * a * (2 * y + 1)
                // если разность положительна, выбираем диогональный пиксель
                if (sigma > 0) {
                    x = x + 1
                    y = y + 1
                    //  пересчитываем ошибку
                    delta = delta + b * b * (2 * x + 1) - a * a * (2 * y + 1)
                    resultPixelList = Pixel.createPixel(x + limx, y + lim, drawingContext.color) :: resultPixelList
                }
                // если разность меньше нуля, выбираем горизонтальный пиксель
                else {
                    x = x + 1
                    // пересчитываем ошибку
                    delta = delta + b * b * (2 * x + 1)
                    resultPixelList = Pixel.createPixel(x + limx, y + lim, drawingContext.color) :: resultPixelList
                }
            }
            // если ошибка равна нулю, то выбираем диогональный пиксель
            else {
                x = x + 1
                y = y + 1
                //  пересчитываем ошибку
                delta = delta + b * b * (2 * x + 1) - a * a * (2 * y + 1)
                resultPixelList = Pixel.createPixel(x + limx, y + lim, drawingContext.color) :: resultPixelList
            }
        }

        // отражаем гиперболу в другие плоскости
        finalPixelList = resultPixelList
        resultPixelList.reverse
        val firstXPoint = end.x
        resultPixelList.foreach {
            pixelok =>
                val rx = pixelok.point.x
                val ry = pixelok.point.y

                finalPixelList = Pixel.createPixel(-rx + 2 * firstXPoint, ry, drawingContext.color) :: finalPixelList
                finalPixelList = Pixel.createPixel(-rx + 2 * firstXPoint, -ry + 2 * ay, drawingContext.color) :: finalPixelList
                finalPixelList = Pixel.createPixel(rx, -ry + 2 * ay, drawingContext.color) :: finalPixelList
        }
        finalPixelList
    }
}