/*
 * $RCSfile: ImageDisplayer.java,v $
 *
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 * $Revision: 1.2 $
 * $Date: 2007/02/09 17:21:50 $
 * $State: Exp $
 */
package Tough2ViewerPRJ;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

class ImageDisplayer extends JFrame implements ActionListener {

    BufferedImage bImage;
    boolean printprogressbar = false;
    int timestep_1;

    private class ImagePanel extends JPanel {

        public void paint(Graphics g) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getSize().width, getSize().height);
            g.drawImage(bImage, 0, 0, this);
            int progressbarwidth = 200;
            int progressbarheight = 20;
            if (printprogressbar) {
                g.setColor(new Color(0.5f, 0.5f, 0.5f));
                g.drawRect((int) 60, (int) 60, (int) progressbarwidth, (int) 20);
                Font myfont = new Font("SansSerif", Font.PLAIN, 20);
                g.setFont(myfont);
                int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
                double initial = Tough2Viewer.dataobj.get_Times(0);
                double current = Tough2Viewer.dataobj.get_Times(timestep_1);
                double finaltime = Tough2Viewer.dataobj.get_Times(timesteps - 1);
                g.drawString(Double.toString(initial), 20, (int) 60);
                g.drawString(Double.toString(finaltime), progressbarwidth + 20, (int) 60);
                g.drawString(Double.toString(current), (progressbarwidth + 20) / 2, (int) 100);
                double progress_1 = (current - initial) / finaltime;
                g.fillRect(60, 60, (int) progress_1, 20);

            }
        }

        private ImagePanel() {
            setPreferredSize(new Dimension(bImage.getWidth(),
                    bImage.getHeight()));
        }
    }

    private JMenuItem printItem;
    private JMenuItem closeItem;
    private JMenuItem saveAsItem;

    private void freeResources() {
        this.removeAll();
        this.setVisible(false);
        bImage = null;
    }

    private void savefile() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
        Date data = new Date();
        String stringa = formatter.format(data);
        int actualVariableToPlot = Tough2Viewer.dataobj.get_actualVariableToPlot();
        String Variable_Name = Tough2Viewer.dataobj.get_variables_name(actualVariableToPlot);
        String fileName = "SnapShot" + stringa + "_" + Variable_Name + ".png";
        String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
        String SuggestedFileName = FilePathString + "\\" + fileName;
        File suggestedFile = new File(SuggestedFileName);
        final JFileChooser fc = new JFileChooser(FilePathString);
        fc.setSelectedFile(suggestedFile);
        int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String strFilePath = fc.getSelectedFile().toString();
            try {
                File file = new File(strFilePath);
                ImageIO.write(bImage, "png", file);
            } catch (IOException e) {
                String error = "File I/O error";
                JOptionPane.showMessageDialog(null, error);
                JOptionPane.showMessageDialog(null, e.toString());
            }
        }

    }

    public void actionPerformed(ActionEvent event) {
        Object target = event.getSource();

        if (target == printItem) {
            new ImagePrinter(bImage).print();
        } else if (target == closeItem) {
            freeResources();
        } else if (target == saveAsItem) {
            savefile();
        }
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        printItem = new JMenuItem("Print..");
        printItem.addActionListener(this);
        saveAsItem = new JMenuItem("Save Image as..");
        saveAsItem.addActionListener(this);
        closeItem = new JMenuItem("Close");
        closeItem.addActionListener(this);
        fileMenu.add(saveAsItem);
        fileMenu.add(printItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(closeItem);

        menuBar.add(fileMenu);
        return menuBar;
    }

    ImageDisplayer(BufferedImage bImage, boolean progressbar, int timestep) {
        this.printprogressbar = progressbar;
        this.timestep_1 = timestep;
        this.bImage = bImage;
        this.setTitle("SnapShot Preview");

        // Create and initialize menu bar
        this.setJMenuBar(createMenuBar());

        // Create scroll pane, and embedded image panel
        ImagePanel imagePanel = new ImagePanel();
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.getViewport().setPreferredSize(new Dimension(700, 700));

        // Handle the close event
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent winEvent) {
                freeResources();
            }
        });

        // Add scroll pane to the frame and make it visible
        this.getContentPane().add(scrollPane);
        this.pack();
        this.setVisible(true);
    }
}
