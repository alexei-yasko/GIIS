package giis.labs.graphics

import java.awt.{Color, Dimension}
import giis.labs.model.{ShapeType, ShapeTypeList}
import giis.labs.model.shape.ShapeFactory
import render.DebugRender
import swing._
import event.ButtonClicked

/**
 * @author Q-YAA
 */
class GraphicsMainFrame extends MainFrame {

    private val defaultWidth = 800
    private val defaultHeight = 600

    private var debugRender: DebugRender = null

    private val drawingButton = new Button("Draw")
    private val startDebugButton = new Button("Debug")
    private val nextDebugStepButton = new Button("Next")

    private val buttonPanel = new FlowPanel() {
        contents += drawingButton
        contents += startDebugButton
        contents += nextDebugStepButton
    }

    private val graphicsScene = new GraphicsScene
    private val gridPanel: GridPanelComponent = new GridPanelComponent(graphicsScene)

    private var shapeType: ShapeType = ShapeTypeList.LineDda

    private val lineBrezenhemMenuItem = new RadioMenuItem("Brezenhem")
    private val lineDdaMenuItem = new RadioMenuItem("Dda")

    private val shapesMenuGroup = new ButtonGroup(lineDdaMenuItem, lineBrezenhemMenuItem) {
        listenTo(lineBrezenhemMenuItem, lineDdaMenuItem)
        reactions += {
            case ButtonClicked(`lineDdaMenuItem`) => shapeType = ShapeTypeList.LineDda
            case ButtonClicked(`lineBrezenhemMenuItem`) => shapeType = ShapeTypeList.LineBrezenhem
        }
    }

    shapesMenuGroup.select(lineDdaMenuItem)
    nextDebugStepButton.enabled_=(false)

    title = "Graphics editor"
    size = new Dimension(defaultWidth, defaultHeight)
    preferredSize = new Dimension(defaultWidth, defaultHeight)
    centerOnScreen()
    menuBar = new MenuBar {
        contents += new Menu("Algorithms") {
            contents += new Menu("Line") {
                contents += lineDdaMenuItem
                contents += lineBrezenhemMenuItem
            }
        }
    }

    listenTo(drawingButton, startDebugButton, nextDebugStepButton)

    reactions += {
        case ButtonClicked(`drawingButton`) => draw()
        case ButtonClicked(`startDebugButton`) => startDebug()
        case ButtonClicked(`nextDebugStepButton`) => nextDebugStep()
    }

    contents = new BorderPanel {
        add(gridPanel, BorderPanel.Position.Center)
        add(buttonPanel, BorderPanel.Position.South)
    }

    private def draw() {
        val shape = ShapeFactory.createShape(gridPanel.selectedPointSet.toList, shapeType)

        if (shape != null) {
            graphicsScene.addShapeRender(shape.createRender(shapeType, Color.BLACK))
            gridPanel.removeSelectedPoints(shape.getPointList)
        }

        gridPanel.repaint()
    }

    private def startDebug() {
        val shape = ShapeFactory.createShape(gridPanel.selectedPointSet.toList, shapeType)

        if (shape != null) {
            debugRender = new DebugRender(shape.createRender(shapeType, Color.BLACK))
            graphicsScene.addShapeRender(debugRender)
            nextDebugStepButton.enabled_=(true)
        }
    }

    private def nextDebugStep() {

        if (debugRender.isNextStepEnabled) {
            debugRender.nextStep()
        }
        else {
            gridPanel.removeSelectedPoints(debugRender.shape.getPointList)
            nextDebugStepButton.enabled_=(false)
        }

        gridPanel.repaint()
    }
}
