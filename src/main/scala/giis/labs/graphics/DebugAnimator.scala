package giis.labs.graphics

import actors.Actor

/**
 * @author Q-YAA
 */
class DebugAnimator(controller: GraphicsSceneController, mainFrame: GraphicsMainFrame) extends Actor {

    def act() {
        while (controller.isNextDebugStepEnabled) {
            receiveWithin(1) {
                case "stop" => exit()
                case _ =>
            }

            Thread.sleep(50)
            controller.nextDebugStep()
            mainFrame.repaint()
        }

        controller.finishDebug()
        mainFrame.changeDebugMode()
    }
}
