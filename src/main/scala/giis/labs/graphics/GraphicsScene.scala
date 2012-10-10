package giis.labs.graphics

import render.Render
import giis.labs.model.Point

/**
 * @author Q-YAA
 */
class GraphicsScene {

    private var selectedPixelBuffer = List[Pixel]()

    private var maxSelectionBufferSize = 0

    private var shapeRenderList: List[Render] = List()

    def addShapeRender(render: Render) {
        shapeRenderList = render :: shapeRenderList
    }

    def addShapeRenderList(renderList: List[Render]) {
        shapeRenderList = shapeRenderList ::: renderList
    }

    def getScenePixelList: List[Pixel] = {
        var drawingPixelList = List[Pixel]()

        shapeRenderList.foreach(render => drawingPixelList = render.draw ::: drawingPixelList)

        drawingPixelList
    }

    def selectPixel(pixel: Pixel) {

        if (selectedPixelBuffer.size < maxSelectionBufferSize) {
            selectedPixelBuffer = pixel :: selectedPixelBuffer
        }
        else {
            selectedPixelBuffer = pixel :: selectedPixelBuffer.tail
        }
    }

    def getSelectedPixels: List[Pixel] = selectedPixelBuffer

    def clearSelectedPixels() {
        selectedPixelBuffer = List()
    }

    def clear() {
        shapeRenderList = List()
        selectedPixelBuffer = List()
    }

    def setMaxSelectionBufferSize(bufferSize: Int) {
        maxSelectionBufferSize = bufferSize
    }

    def removeLastShape() {
        if (shapeRenderList.size > 0) {
            shapeRenderList = shapeRenderList.tail
        }
    }

    def movePoint(from: Point, to: Point) {
        for (render <- shapeRenderList) {

            if (render.shape.isPointBelongsTo(from)) {
                render.shape.movePoint(from, to)
                return
            }
        }
    }
}
