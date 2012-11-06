package giis.labs.graphics

import render.{LineRender, Render}
import giis.labs.model.{Matrix, Point}
import giis.labs.model.shape.{ShapeFactory, Shape}
import java.awt.Color

/**
 * Class that represent graphic scene for shape dawning.
 *
 * @author Q-YAA
 */
class GraphicsScene {

    private var selectedPixelBuffer = List[Pixel]()

    private var maxSelectionBufferSize = 0

    private var shapeRenderList: List[Render] = List()

    /**
     * Adds shape render to the scene.
     *
     * @param render shape render
     */
    def addShapeRender(render: Render) {
        shapeRenderList = render :: shapeRenderList
    }

    /**
     * Adds shape render list to the scene.
     * @param renderList list of the shape renders
     */
    def addShapeRenderList(renderList: List[Render]) {
        shapeRenderList = shapeRenderList ::: renderList
    }

    /**
     * Returns all pixels from the shapes that placed in the scene.
     *
     * @return List[Pixel] list of all pixels
     */
    def getScenePixelList: List[Pixel] = {
        var drawingPixelList = List[Pixel]()

        shapeRenderList.foreach(render => drawingPixelList = render.draw ::: drawingPixelList)

        drawingPixelList
    }

    /**
     * Set the pixel selected.
     *
     * <p>If maxSelectionBufferSize equals -1 then selectionBufferSize not limited.</p>
     *
     * @param pixel pixel to select
     */
    def selectPixel(pixel: Pixel) {

        if (maxSelectionBufferSize == -1 || selectedPixelBuffer.size < maxSelectionBufferSize) {
            selectedPixelBuffer = pixel :: selectedPixelBuffer
        }
        else {
            selectedPixelBuffer = pixel :: selectedPixelBuffer.tail
        }
    }

    /**
     * Returns all selected pixels from scene.
     *
     * @return List[Pixel] selected pixels
     */
    def getSelectedPixels: List[Pixel] = selectedPixelBuffer

    /**
     * Unselect all pixels on the scene.
     */
    def clearSelectedPixels() {
        selectedPixelBuffer = List()
    }

    /**
     * Remove all shapes from the scene.
     */
    def clear() {
        shapeRenderList = List()
        selectedPixelBuffer = List()
    }

    /**
     * Set max selected pixels quantity.
     *
     * @param bufferSize max quantity of the selected pixels
     */
    def setMaxSelectionBufferSize(bufferSize: Int) {
        maxSelectionBufferSize = bufferSize
    }

    /**
     * Remove last drawn shape from scene.
     */
    def removeLastShape() {
        if (shapeRenderList.size > 0) {
            shapeRenderList = shapeRenderList.tail
        }
    }

    /**
     * Move point on the scene from one given position to another.
     *
     * @param from origin position
     * @param to new position
     */
    def movePoint(from: Point, to: Point) {
        for (render <- shapeRenderList) {

            if (render.shape.isPointBelongsTo(from)) {
                render.shape.movePoint(from, to)
                return
            }
        }
    }

    /**
     * Determines if point placed on the scene.
     *
     * @param point point to determine
     * @return true if point placed on the scene, false in other case
     */
    def isPointPlacedOnPosition(point: Point): Boolean = {
        !shapeRenderList.filter(render => render.shape.isPointBelongsTo(point)).isEmpty
    }

    /**
     * Return the shape that contains given point.
     *
     * @param point given point
     * @return shape
     */
    def getShapeThatContainsPoint(point: Point): Shape = {
        for (render <- shapeRenderList if render.shape.isPointInsideShape(point)) {
            return render.shape
        }

        null
    }
}