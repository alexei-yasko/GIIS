package giis.labs.graphics

import swing.Panel
import javax.swing.BorderFactory
import java.awt.{Cursor, BasicStroke, Graphics2D, Color}
import giis.labs.model.Point
import swing.event._
import swing.event.MousePressed
import swing.event.MouseWheelMoved
import swing.event.MouseClicked

/**
 * Panel that display the grid with the axis and all shapes.
 *
 * @author Q-YAA
 */
class GridPanelComponent(scene: GraphicsScene, controller: GraphicsSceneController) extends Panel {

    private val axisLineThickness = 2f
    private val axisLineColor = Color.GRAY

    private val gridLineThickness = 1f
    private val gridLineColor = Color.LIGHT_GRAY

    private val selectedPixelDrawingContext = DrawingContext.createDrawingContext(Color.RED)

    private val defaultPixelSize = 15
    private var scale = 1d
    private var relativeCenterPosition = new Point(0, 0)

    border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2)

    cursor = new Cursor(Cursor.CROSSHAIR_CURSOR)

    listenTo(mouse.clicks, mouse.wheel, mouse.moves)

    var movePointFunction: (java.awt.Point) => () => Unit = null

    reactions += {
        case MouseClicked(source, point, modifiers, clicks, triggersPopup) => executeAndRepaint(selectClickedPixel(point))

        case MouseWheelMoved(source, point, modifiers, rotation) => executeAndRepaint(scaleGrid(rotation))

        case MousePressed(source, point, modifiers, 1, triggersPopup) => {
            movePointFunction = movePoint(point, _)
        }

        case MouseDragged(source, point, modifiers) => {
            executeAndRepaint(movePointFunction(point))
            movePointFunction = movePoint(point, _)
        }

        case MouseReleased(source, point, modifiers, 1, triggersPopup) => cursor = new Cursor(Cursor.CROSSHAIR_CURSOR)
    }

    override protected def paintComponent(graphics: Graphics2D) {
        super.paintComponent(graphics)

        drawGrid(graphics)
        drawAxis(graphics)

        drawPixelList(scene.getScenePixelList, graphics)
        drawPixelList(scene.getSelectedPixels, graphics)
    }

    private def drawAxis(graphics: Graphics2D) {
        graphics.setColor(axisLineColor)
        graphics.setStroke(new BasicStroke(axisLineThickness))

        for (i <- 0 to(size.height, pixelSize)) {
            graphics.drawRect(getCenterCoordinateX, i, pixelSize, pixelSize)
        }

        for (i <- 0 to(size.width, pixelSize)) {
            graphics.drawRect(i, getCenterCoordinateY, pixelSize, pixelSize)
        }
    }

    private def drawGrid(graphics: Graphics2D) {
        graphics.setColor(gridLineColor)
        graphics.setStroke(new BasicStroke(gridLineThickness))

        for (i <- 0 to(size.height, pixelSize)) {
            graphics.drawLine(0, i, size.width, i)
        }

        for (i <- 0 to(size.width, pixelSize)) {
            graphics.drawLine(i, 0, i, size.height)
        }
    }

    private def drawPixelList(pixelsList: List[Pixel], graphics: Graphics2D) {
        pixelsList.foreach(drawPixel(_, graphics))
    }

    private def drawPixel(pixel: Pixel, graphics: Graphics2D) {
        graphics.setColor(pixel.color)

        val x = getCenterCoordinateX + (pixel.point.x) * pixelSize
        val y = getCenterCoordinateY - (pixel.point.y) * pixelSize

        graphics.fillRect(x, y, pixelSize, pixelSize)
    }

    private def getCenterCoordinateX = size.width / 2 - (size.width / 2 % pixelSize) + relativeCenterPosition.x * pixelSize

    private def getCenterCoordinateY = size.height / 2 - (size.height / 2 % pixelSize) + relativeCenterPosition.y * pixelSize

    private def pixelSize = (defaultPixelSize * scale).toInt

    private def selectClickedPixel(pointAwt: java.awt.Point)() {
        val pixel = new Pixel(convertToPoint(pointAwt), selectedPixelDrawingContext)
        scene.selectPixel(pixel)

        controller.drawShape(DrawingContext.createDrawingContext)
    }

    private def scaleGrid(rotation: Int)() {
        scale = scale - 0.05 * rotation

        if (scale <= 1 / defaultPixelSize + 0.1) {
            scale = 1d / defaultPixelSize
        }
    }

    private def executeAndRepaint(function: () => Unit) {
        function()
        repaint()
    }

    private def convertToPoint(pointAwt: java.awt.Point) =
        new Point(calculatePointX(pointAwt.getX), calculatePointY(pointAwt.getY))

    private def calculatePointY(graphicCoordinateY: Double) = {
        if (graphicCoordinateY < getCenterCoordinateY) {
            ((getCenterCoordinateY - graphicCoordinateY) / pixelSize).toInt + 1
        } else {
            ((getCenterCoordinateY - graphicCoordinateY) / pixelSize).toInt
        }
    }

    private def calculatePointX(graphicCoordinateX: Double) = {
        if (graphicCoordinateX < getCenterCoordinateX) {
            ((graphicCoordinateX - getCenterCoordinateX) / pixelSize).toInt - 1
        } else {
            ((graphicCoordinateX - getCenterCoordinateX) / pixelSize).toInt
        }
    }

    private def movePoint(from: java.awt.Point, to: java.awt.Point)() {

        val fromPoint : Point = convertToPoint(from)
        val toPoint : Point = convertToPoint(to)

        if (scene.isPointPlacedOnPosition(fromPoint)) {
            cursor = new Cursor(Cursor.HAND_CURSOR)

            scene.movePoint(fromPoint, toPoint)
        }
        else {
            cursor = new Cursor(Cursor.MOVE_CURSOR)

            relativeCenterPosition = new Point(
                relativeCenterPosition.x + (toPoint.x - fromPoint.x),
                relativeCenterPosition.y + (fromPoint.y - toPoint.y)
            )
        }
    }
}


