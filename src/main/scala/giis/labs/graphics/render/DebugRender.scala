package giis.labs.graphics.render

/**
 * Wrapper render, witch realized shape drawing in the debug mode.
 *
 * @author Q-YAA
 */
class DebugRender(render: Render) extends Render(render.shape, render.drawingContext) {

    private var stepNumber = 0

    private var isDebugFinished = false

    /**
     * Override, because we don't draw the main points of the shape in debug mode.
     *
     * @return List[Pixel] result of drawing
     */
    override def draw = drawShape

    /**
     * Signals that debug finished.
     */
    def finishDebug() {
        isDebugFinished = true
    }

    /**
     * Determine if the next debug step enabled.
     *
     * @return true if next debug step enabled, false in another case
     */
    def isNextStepEnabled = stepNumber < (render.draw.size - shape.getPointList.size)

    /**
     * Determine if the previous debug step enabled.
     *
     * @return true if previous debug step enabled, false in another case
     */
    def isPreviousStepEnabled = stepNumber > 0

    /**
     * Allow drawing of the next debug step.
     */
    def nextStep() {
        stepNumber = stepNumber + 1
    }

    /**
     * Allow drawing of the previous debug step.
     */
    def previousStep() {
        stepNumber = stepNumber - 1
    }

    protected def drawShape = {
        if (!isDebugFinished) {
            render.draw.splitAt(stepNumber)._1
        }
        else {
            render.draw.splitAt(render.draw.size - shape.getPointList.size)._1
        }
    }
}
