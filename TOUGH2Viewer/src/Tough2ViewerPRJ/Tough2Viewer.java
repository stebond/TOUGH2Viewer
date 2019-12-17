/*
 * Tough2Viewer.java
 */
package Tough2ViewerPRJ;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JOptionPane;

/**
 * The main class of the application.
 */
public class Tough2Viewer extends javax.swing.JFrame {

    public static GlobalVariables dataobj;
    public static TOUGH2ViewerGUI tough2viewerGUI;

    /**
     * A convenient static getter for the application instance.
     *
     * @return the instance of Tough2Viewer
     */
    public static void toLogFile(String outputString) {
        String strFilePath = "LogFile.txt";
        try {
            // Open an output stream
            FileOutputStream fos = new FileOutputStream(strFilePath, true);
            PrintStream ps;
            // Print a line of text
            ps = new PrintStream(fos);
            ps.println(outputString);
            fos.close();
        } // Catches any error conditions
        catch (IOException e) {
            String output = "Unable to write file";
            toLogFile(output);
        }
        System.out.println(outputString);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TOUGH2ViewerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TOUGH2ViewerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TOUGH2ViewerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TOUGH2ViewerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SplashScreen ss = new SplashScreen();
        ss.showSplashAndExit();
        try {
            dataobj = new GlobalVariables();
        } catch (Exception e) {
            String error = "Java3D not Found";
            JOptionPane.showMessageDialog(null, error);
            JOptionPane.showMessageDialog(null, e.toString());
            System.exit(-1);
        }
        tough2viewerGUI = new TOUGH2ViewerGUI();
        tough2viewerGUI.setVisible(true);

    }
}
