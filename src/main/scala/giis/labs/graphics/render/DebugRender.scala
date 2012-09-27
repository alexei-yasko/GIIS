package giis.labs.graphics.render

/**
 * @author Q-YAA
 */
class DebugRender(render: Render) extends Render(render.shape, render.color) {

    private var stepNumber = 0

    def draw = render.draw.splitAt(stepNumber)._1

    def isNextStepEnabled = stepNumber + 1 < render.draw.size

    def nextStep() {
        stepNumber = stepNumber + 1
    }
}
