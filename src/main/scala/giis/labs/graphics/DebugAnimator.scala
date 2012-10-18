package giis.labs.graphics

import actors.Actor

/**
 * @author Q-YAA
 */
class DebugAnimator(controller: GraphicsSceneController, mainFrame: GraphicsMainFrame) extends Actor {

    def act() {

        //        react {
        //            case "startPaint" => {
        //                while (true) {
        //                    Thread.sleep(200)
        //                    if (controller.isNextDebugStepEnabled) {
        //                        controller.nextDebugStep()
        //                        mainFrame.repaint()
        //                    }
        //                }
        //            }
        //            case "stop" => exit()
        //        }

        receive {
            case "stop" => exit()
        }

        while (true) {
            Thread.sleep(200)
            if (controller.isNextDebugStepEnabled) {
                controller.nextDebugStep()
                mainFrame.repaint()
            }
        }
    }
}
