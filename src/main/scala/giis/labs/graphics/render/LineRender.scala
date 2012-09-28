package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.graphics.{DrawingContext, Pixel}
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
class LineRender(shape: Shape, drawingContext: DrawingContext) extends Render(shape, drawingContext) {

    private val beginPoint = shape.getPointList.toArray.apply(0)
    private val endPoint = shape.getPointList.toArray.apply(1)

    def draw: List[Pixel] = drawingContext.shapeType match {
        case Shape.LineDda => ddaRender
        case Shape.LineBrezenhem => brezenhemRender
    }

    /**
     * Функция разложения отрезка в растр методо ЦДА.
     *
     * @return List[Pixel] список пикселей отрезка
     */
    private def ddaRender: List[Pixel] = {

        val x1 = beginPoint.x
        val x2 = endPoint.x
        val y1 = beginPoint.y
        val y2 = endPoint.y

        // рассчитываем максимальную длину проекции отрезка на оси координат
        val length = math.max(math.abs(x2 - x1), math.abs(y2 - y1))

        val dx = (x2 - x1).toDouble / length
        val dy = (y2 - y1).toDouble / length

        //отдельно отрисовываем вертикальные, горизонтальные и диогональные линии
        if (x1 == x2) {
            drawVerticalLine(x1, y1, y2)
        }
        else if (y1 == y2) {
            drawHorizontalLine(x1, x2, y1)
        }
        else if (math.abs(dx) == math.abs(dy)) {
            drawDiagonalLine(x1, x2, y1, y2)
        }
        else {
            var resultPixelList = List[Pixel]()

            var x: Double = x1
            var y: Double = y1

            //вычисляем координаты последующих точек отрезка
            for (i <- 0 to length) {
                resultPixelList = createPixel(math.round(x).toInt, math.round(y).toInt, drawingContext) :: resultPixelList
                x = x + dx
                y = y + dy
            }

            resultPixelList
        }
    }

    private def brezenhemRender: List[Pixel] = {
        val x1 = beginPoint.x
        val x2 = endPoint.x
        val y1 = beginPoint.y
        val y2 = endPoint.y

        //Вычисляем длины проекций на оси координат
        var dx = math.abs(x2 - x1)
        var dy = math.abs(y2 - y1)

        //Определяем длинну наибольшей проекции на оси координат
        val lengthMax = math.max(dx, dy)

        //отдельно отрисовываем вертикальные, горизонтальные и диогональные линии
        if (x1 == x2) {
            drawVerticalLine(x1, y1, y2)
        }
        else if (y1 == y2) {
            drawHorizontalLine(x1, x2, y1)
        }
        else if (math.abs(dx) == math.abs(dy)) {
            drawDiagonalLine(x1, x2, y1, y2)
        } else {
            var resultPixelList = List[Pixel]()

            //приращение принимаем равным шагу растра
            val stepX = (x2 - x1) / math.abs(x2 - x1)
            val stepY = (y2 - y1) / math.abs(y2 - y1)

            var x = x1
            var y = y1

            //является ли ось Х основной
            val isMainAxisX = dy < dx

            //если Y основная ось, меняем прокции местами
            if (!isMainAxisX) {
                dx = dx + dy
                dy = dx - dy
                dx = dx - dy
            }

            //начальное значение ошибки
            var error = 2 * dy - dx

            for (i <- 0 to lengthMax) {
                resultPixelList = createPixel(x, y, drawingContext) :: resultPixelList

                // если значение ошибки неотрицательно, отрезок проходит выше середины пикселя
                if (error >= 0 && isMainAxisX) {
                    y = y + stepY
                    error = error - 2 * dx
                }
                else if (error >= 0 && !isMainAxisX) {
                    x = x + stepX
                    error = error - 2 * dx
                }

                if (isMainAxisX) {
                    x = x + stepX
                    error = error + 2 * dy
                }
                else if (!isMainAxisX) {
                    y = y + stepY
                    error = error + 2 * dy
                }
            }

            resultPixelList
        }
    }

    private def drawHorizontalLine(x1: Int, x2: Int, y: Int): List[Pixel] = {
        var resultPixelList = List[Pixel]()

        if (x2 > x1) {
            (x1 to x2).foreach(i => resultPixelList = createPixel(i, y, drawingContext) :: resultPixelList)
        }
        else {
            (x1 to(x2, -1)).foreach(i => resultPixelList = createPixel(i, y, drawingContext) :: resultPixelList)
        }

        resultPixelList
    }

    private def drawVerticalLine(x: Int, y1: Int, y2: Int): List[Pixel] = {
        var resultPixelList = List[Pixel]()

        if (y2 > y1) {
            (y1 to y2).foreach(i => resultPixelList = createPixel(x, i, drawingContext) :: resultPixelList)
        }
        else {
            (y1 to(y2, -1)).foreach(i => resultPixelList = createPixel(x, i, drawingContext) :: resultPixelList)
        }

        resultPixelList
    }


    private def drawDiagonalLine(x1: Int, x2: Int, y1: Int, y2: Int) = {
        var resultPixelList = List[Pixel]()

        if (x2 > x1 && y2 > y1) {
            (0 to x2 - x1).foreach(i => resultPixelList = createPixel(x1 + i, y1 + i, drawingContext) :: resultPixelList)
        }
        else if (x2 > x1 && y2 < y1) {
            (0 to x2 - x1).foreach(i => resultPixelList = createPixel(x1 + i, y1 - i, drawingContext) :: resultPixelList)
        }
        else if (x2 < x1 && y2 > y1) {
            (0 to x1 - x2).foreach(i => resultPixelList = createPixel(x1 - i, y1 + i, drawingContext) :: resultPixelList)
        }
        else if (x2 < x1 && y2 < y1) {
            (0 to x1 - x2).foreach(i => resultPixelList = createPixel(x1 - i, y1 - i, drawingContext) :: resultPixelList)
        }

        resultPixelList
    }

    private def createPixel(x: Int, y: Int, drawingContext: DrawingContext): Pixel = new Pixel(new Point(x, y), drawingContext)
}