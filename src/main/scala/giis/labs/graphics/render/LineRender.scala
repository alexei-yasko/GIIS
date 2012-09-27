package giis.labs.graphics.render

import giis.labs.model.shape.Shape
import giis.labs.model.{Point, ShapeTypeList, ShapeType}
import java.awt.Color
import giis.labs.graphics.Pixel

/**
 * @author Q-YAA
 */
class LineRender(shape: Shape, color: Color, shapeType: ShapeType) extends Render(shape, color) {

    private val beginPoint = shape.getPointList.toArray.apply(0)
    private val endPoint = shape.getPointList.toArray.apply(1)

    def draw: List[Pixel] = shapeType match {
        case ShapeTypeList.LineDda => ddaRender.reverse
        case ShapeTypeList.LineBrezenhem => brezenhemRender.reverse
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
                resultPixelList = createPixel(math.round(x).toInt, math.round(y).toInt, color) :: resultPixelList
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
        val lengthX = math.abs(x2 - x1)
        val lengthY = math.abs(y2 - y1)

        //Определяем длинну наибольшей проекции на оси координат
        val lengthMax = math.max(lengthX, lengthY)

        //отдельно отрисовываем вертикальные, горизонтальные и диогональные линии
        if (x1 == x2) {
            drawVerticalLine(x1, y1, y2)
        }
        else if (y1 == y2) {
            drawHorizontalLine(x1, x2, y1)
        }
        else if (math.abs(lengthX) == math.abs(lengthY)) {
            drawDiagonalLine(x1, x2, y1, y2)
        } else {
            var resultPixelList = List[Pixel]()

            //приращение принимаем равным шагу растра
            val stepX = (x2 - x1) / math.abs(x2 - x1)
            val stepY = (y2 - y1) / math.abs(y2 - y1)

            var x = x1
            var y = y1

            //начальное значение ошибки
            var error = 2 * lengthY - lengthX

            //является ли ось Х основной
            val isMainAxisX = lengthY < lengthX

            for (i <- 0 to lengthMax) {
                resultPixelList = createPixel(x, y, color) :: resultPixelList

                if (error >= 0 && isMainAxisX) {
                    y = y + stepY
                    error = correctErrorValue(error, lengthX)
                }
                else if (error >= 0 && !isMainAxisX) {
                    x = x + stepX
                    error = correctErrorValue(error, lengthX)
                }

                if (isMainAxisX) {
                    x = x + stepX
                    error = correctErrorValue(error, lengthY)
                }
                else if (!isMainAxisX) {
                    y = y + stepY
                    error = correctErrorValue(error, lengthY)
                }
            }

            resultPixelList
        }
    }


    private def correctErrorValue(error: Int, length: Int): Int =
        if (error >= 0) {
            error - 2 * length
        }
        else {
            error + 2 * length
        }


    private def drawHorizontalLine(x1: Int, x2: Int, y: Int): List[Pixel] = {
        var resultPixelList = List[Pixel]()

        if (x2 > x1) {
            (x1 to x2).foreach(i => resultPixelList = createPixel(i, y, color) :: resultPixelList)
        }
        else {
            (x1 to(x2, -1)).foreach(i => resultPixelList = createPixel(i, y, color) :: resultPixelList)
        }

        resultPixelList
    }

    private def drawVerticalLine(x: Int, y1: Int, y2: Int): List[Pixel] = {
        var resultPixelList = List[Pixel]()

        if (y2 > y1) {
            (y1 to y2).foreach(i => resultPixelList = createPixel(x, i, color) :: resultPixelList)
        }
        else {
            (y1 to(y2, -1)).foreach(i => resultPixelList = createPixel(x, i, color) :: resultPixelList)
        }

        resultPixelList
    }


    private def drawDiagonalLine(x1: Int, x2: Int, y1: Int, y2: Int) = {
        var resultPixelList = List[Pixel]()

        if (x2 > x1 && y2 > y1) {
            (0 to x2 - x1).foreach(i => resultPixelList = createPixel(x1 + i, y1 + i, color) :: resultPixelList)
        }
        else if (x2 > x1 && y2 < y1) {
            (0 to x2 - x1).foreach(i => resultPixelList = createPixel(x1 + i, y1 - i, color) :: resultPixelList)
        }
        else if (x2 < x1 && y2 > y1) {
            (0 to x1 - x2).foreach(i => resultPixelList = createPixel(x1 - i, y1 + i, color) :: resultPixelList)
        }
        else if (x2 < x1 && y2 < y1) {
            (0 to x1 - x2).foreach(i => resultPixelList = createPixel(x1 - i, y1 - i, color) :: resultPixelList)
        }

        resultPixelList
    }

    private def createPixel(x: Int, y: Int, color: Color): Pixel = new Pixel(new Point(x, y), color)
}