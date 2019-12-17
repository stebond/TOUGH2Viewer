/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

/**
 *
 * @author stebond
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class SplashScreen extends JWindow {

    public void showSplashAndExit() {
        JPanel content = (JPanel) getContentPane();
        content.setOpaque(false);
        setSize(543, 277);
        setLocationRelativeTo(null);
        JLabel label = new JLabel(new ImageIcon("splash.jpg"));
        content.add(label, BorderLayout.CENTER);
        setVisible(true);

        Timer timer = new Timer(3000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        timer.start();
    }
}
