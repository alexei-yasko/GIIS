package giis.labs.graphics.render

/**
 * @author Q-YAA
 */
class DebugRender(render: Render) extends Render(render.shape, render.drawingContext) {

    private var stepNumber = 0

    private var isDebugFinished = false

    override def draw = drawShape

    def finishDebug() {
        isDebugFinished = true
    }

    def isNextStepEnabled = stepNumber < (render.draw.size - shape.getPointList.size)

    def isPreviousStepEnabled = stepNumber > 0

    def nextStep() {
        stepNumber = stepNumber + 1
    }

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
