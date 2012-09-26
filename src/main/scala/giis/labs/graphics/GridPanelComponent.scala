package giis.labs.graphics

import swing.Panel
import javax.swing.BorderFactory
import java.awt.{BasicStroke, Graphics2D, Color}
import swing.event.MouseClicked
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
class GridPanelComponent(scene: GraphicsScene) extends Panel {

    private val AXIS_LINE_THICKNESS = 2f
    private val AXIS_LINE_COLOR = Color.GRAY

    private val GRID_LINE_THICKNESS = 1f
    private val GRID_LINE_COLOR = Color.LIGHT_GRAY

    private val SELECTED_PIXEL_COLOR = Color.RED

    private val DEFAULT_PIXEL_SIZE = 15
    private val SCALE = 1

    private var selectedPixelSet = Set[Pixel]()

    border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2)

    listenTo(mouse.clicks)

    reactions += {
        case MouseClicked(source, point, modifiers, clicks, triggersPopup) => selectClickedPixel(point)
    }

    def selectedPointSet: Set[Point] = for (pixel <- selectedPixelSet) yield {
        pixel.point
    }

    def removeSelectedPoints(elements: List[Point]) {
        selectedPixelSet = selectedPixelSet.filter(pixel => !elements.contains(pixel.point))
    }

    override protected def paintComponent(graphics: Graphics2D) {
        super.paintComponent(graphics)

        drawGrid(graphics)
        drawAxis(graphics)

        drawPixelSet(selectedPixelSet, graphics)
        drawPixelSet(scene.getScenePixelSet, graphics)
    }

    private def drawAxis(graphics: Graphics2D) {
        graphics.setColor(AXIS_LINE_COLOR)
        graphics.setStroke(new BasicStroke(AXIS_LINE_THICKNESS))

        for (i <- 0 to(size.height, pixelSize)) {
            graphics.drawRect(getCenterCoordinateX, i, pixelSize, pixelSize)
        }

        for (i <- 0 to(size.width, pixelSize)) {
            graphics.drawRect(i, getCenterCoordinateY, pixelSize, pixelSize)
        }
    }

    private def drawGrid(graphics: Graphics2D) {
        graphics.setColor(GRID_LINE_COLOR)
        graphics.setStroke(new BasicStroke(GRID_LINE_THICKNESS))

        for (i <- 0 to(size.height, pixelSize)) {
            graphics.drawLine(0, i, size.width, i)
        }

        for (i <- 0 to(size.width, pixelSize)) {
            graphics.drawLine(i, 0, i, size.height)
        }
    }

    private def drawPixelSet(pixelsSet: Set[Pixel], graphics: Graphics2D) {
        pixelsSet.foreach(drawPixel(_, graphics))
    }

    private def drawPixel(pixel: Pixel, graphics: Graphics2D) {
        graphics.setColor(pixel.color)

        val x = getCenterCoordinateX + (pixel.point.x) * pixelSize
        val y = getCenterCoordinateY - (pixel.point.y) * pixelSize

        graphics.fillRect(x, y, pixelSize, pixelSize)
    }

    private def getCenterCoordinateX = size.width / 2 - (size.width / 2 % pixelSize)

    private def getCenterCoordinateY = size.height / 2 - (size.height / 2 % pixelSize)

    private def pixelSize = DEFAULT_PIXEL_SIZE * SCALE

    private def selectClickedPixel(pointAwt: java.awt.Point) {
        val pixel = new Pixel(convertToPoint(pointAwt), SELECTED_PIXEL_COLOR)
        selectedPixelSet = selectedPixelSet + pixel

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
}


