/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

import java.io.File;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author stebond
 */
class ReadDataFileActivity1 extends SwingWorker<Void, Integer> {

    /**
     * Constructs the simulated activity that increments a counter from 0 to a
     * given target.
     *
     * @param t the target value of the counter.
     */
    public ReadDataFileActivity1(int t, String filename, JProgressBar progressBar) {
        myfilename = filename.substring(0);
        myprogressBar = progressBar;
        myprogressBar.setMaximum(100);
        myprogressBar.setMinimum(0);
        myprogressBar.setStringPainted(true);

    }

    protected Void doInBackground() throws Exception {
        try {

            Thread.sleep(100);
            File mydata = new File(myfilename);
//               File mydata2=new File(filename);
            Tough2Viewer.dataobj.readDataFile4(mydata, myprogressBar);

        } catch (InterruptedException e) {
        }
        return null;
    }

    protected void process(List<Integer> chunks) {
        for (Integer chunk : chunks) {
            //textArea.append(chunk + "\n");
            myprogressBar.setValue(chunk);
        }
    }

    protected void done() {

        myprogressBar.setValue(100);
    }
    private String myfilename;
    private JProgressBar myprogressBar;
}
