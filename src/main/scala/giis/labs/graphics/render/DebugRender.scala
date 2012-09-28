package giis.labs.graphics.render

/**
 * @author Q-YAA
 */
class DebugRender(render: Render) extends Render(render.shape, render.drawingContext) {

    private var stepNumber = 0

    def draw = render.draw.splitAt(stepNumber)._1

    def isNextStepEnabled = stepNumber < render.draw.size

    def isPreviousStepEnabled = stepNumber > 0

    def nextStep() {
        stepNumber = stepNumber + 1
    }

    def previousStep() {
        stepNumber = stepNumber - 1
    }
}
