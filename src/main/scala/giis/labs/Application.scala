package giis.labs

import graphics.GraphicsMainFrame
import swing.SimpleSwingApplication
import javax.swing.UIManager

/**
 * Application.
 *
 * <p>Entry point of the program.</p>
 *
 * @author Q-YAA
 */
object Application extends SimpleSwingApplication {

//    UIManager.setLookAndFeel("org.jb2011.lnf.beautyeye.BeautyEyeLookAndFeelCross")
    UIManager.put("RootPane.setupButtonVisible", false)

    def top = new GraphicsMainFrame
}
