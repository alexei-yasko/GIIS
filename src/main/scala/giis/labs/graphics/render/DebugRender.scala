package giis.labs.graphics.render

/**
 * @author Q-YAA
 */
class DebugRender(render: Render) extends Render(render.shape, render.drawingContext) {

    private var stepNumber = 0

    private var isDebugFinished = false

    def draw = {
        if (!isDebugFinished) {
            render.draw.splitAt(stepNumber)._1
        }
        else {
            render.draw
        }
    }

    def finishDebug() {
        isDebugFinished = true
    }

    def isNextStepEnabled = stepNumber < render.draw.size

    def isPreviousStepEnabled = stepNumber > 0

    def nextStep() {
        stepNumber = stepNumber + 1
    }

    def previousStep() {
        stepNumber = stepNumber - 1
    }
}
