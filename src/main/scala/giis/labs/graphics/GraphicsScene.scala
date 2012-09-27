package giis.labs.graphics

import render.Render

/**
 * @author Q-YAA
 */
class GraphicsScene {

    private var selectedPixelBuffer = Set[Pixel]()

    private var shapeRenderList: List[Render] = List()

    def addShapeRender(render: Render) {
        shapeRenderList = render :: shapeRenderList
    }

    def addShapeRenderList(renderList: List[Render]) {
        shapeRenderList = shapeRenderList ::: renderList
    }

    def getScenePixelSet: Set[Pixel] = {
        var drawingPixelSet = Set[Pixel]()

        shapeRenderList.foreach(render => drawingPixelSet = drawingPixelSet ++ render.draw)

        drawingPixelSet
    }

    def selectPixel(pixel: Pixel) {
        selectedPixelBuffer = selectedPixelBuffer + pixel
    }

    def getSelectedPixels: Set[Pixel] = selectedPixelBuffer

    def clearSelectedPixels() {
        selectedPixelBuffer = Set()
    }

    def clear() {
        shapeRenderList = List()
    }
}
