/*
 * InputBoxRegular.java
 *
 * Created on 28 maggio 2009, 15.37
 */
package Tough2ViewerPRJ;

import java.awt.Dimension;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.String;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;

/**
 *
 * @author stebond
 */
public class InputBoxVoronoi extends javax.swing.JDialog {

    String FilePathString;

    /**
     * Creates new form InputBoxRegular
     */
    public InputBoxVoronoi() {
        super();
        initComponents();
        getRootPane().setDefaultButton(closeButton);
//        readnxynzINIFile();
        readINIPathFile();
        this.setSize(new Dimension(600, 300));
        jButton1.setEnabled(false);
        jButtonCreateEmptyModel.setEnabled(false);
        jButtonSegmt.setEnabled(false);

    }

    public void toINIFile() {
        String strFilePath = "iTough2Viewer.ini";
        try {
            // Open an output stream
            FileOutputStream fos = new FileOutputStream(strFilePath, true);
            PrintStream ps;
            // Print a line of text
            ps = new PrintStream(fos);
            ps.println(jTextField1.getText());
            ps.println(jTextField2.getText());
            ps.println(jTextField3.getText());
            fos.close();
        } // Catches any error conditions
        catch (IOException e) {
            String output = "Unable to write iTough2Viewer.ini file";
            Tough2Viewer.toLogFile(output);
            System.exit(-1);
        }

    }

    public void tonxynzINIFile() {
        String strFilePath = "nxynz.ini";
        try {
            // Open an output stream
            FileOutputStream fos = new FileOutputStream(strFilePath, true);
            // DataOutputStream dos = new DataOutputStream(fos);
            PrintStream ps;
            // Print a line of text
            ps = new PrintStream(fos);
            //            ps.println (jTextNxy.getText());
            //            ps.println (jTextNz.getText());

            fos.close();
        } // Catches any error conditions
        catch (IOException e) {
            String output = "Unable to write file";
            Tough2Viewer.toLogFile(output);
            System.exit(-1);
        }

    }

    public void toINIPathFile() {
        String strFilePath = "path.ini";
        try {
            // Open an output stream
            FileOutputStream fos = new FileOutputStream(strFilePath, true);
            //                      DataOutputStream dos = new DataOutputStream(fos);
            PrintStream ps;
            // Print a line of text
            ps = new PrintStream(fos);
            ps.println(FilePathString);
            fos.close();
        } // Catches any error conditions
        catch (IOException e) {
            String output = "Unable to write file";
            Tough2Viewer.toLogFile(output);
            System.exit(-1);
        }

    }

    public void readINIFile() {
        ///////////////////////////////////////
        ////lettura URL-primo giro guardo quante sono..///////////////////////
        ///////////////////////////////////////
        File file = new File("iTough2Viewer.ini");
        boolean exists = (file).exists();
        if (exists) {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            String linea;
            int num_url = 0;
            //mettere
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    linea = (String) dis.readLine();
                    jTextField1.setText(linea);
                    linea = (String) dis.readLine();
                    jTextField2.setText(linea);
                    Tough2Viewer.dataobj.set_SegmtFileName(linea);
                    linea = (String) dis.readLine();
                    jTextField3.setText(linea);
                    Tough2Viewer.dataobj.set_InFileName(linea);
                }
                fis.close();
                bis.close();
                dis.close();
            } catch (IOException e) {
                //e.printStackTrace();
                Tough2Viewer.toLogFile("Impossibile trovare il file ini");

            }

            if (Tough2Viewer.dataobj.read_IN_file()) {
                jButtonSegmt.setEnabled(true);
                Tough2Viewer.dataobj.ID_grid_type = 1;
                if (Tough2Viewer.dataobj.read_SEGMT_file()) {
                    if (jTextField1.getText().contentEquals("none")) {
                        CreateEmptyModel();
                    } else {
                        jButton1.setEnabled(true);
                        String file_path = jTextField1.getText();
                        File data_file = new File(jTextField1.getText());
                        String fileName = data_file.getName();
                        Tough2Viewer.dataobj.set_DataFileName(fileName);
                        ReadDataFileActivity readactivity = new ReadDataFileActivity(10, file_path, jProgressBar1);
                        readactivity.execute();
                        //iTough2Viewer.dataobj.readDataFile(data_file,jCheckBox1.isSelected(),this);
                        String FilePath = data_file.getAbsolutePath();
                        FilePathString = FilePath.substring(0, FilePath.length() - fileName.length());

                        Tough2Viewer.dataobj.set_DataFilePath(FilePathString);
                        Tough2Viewer.tough2viewerGUI.UpdateFileInformation();
                    }

                } else {
                    jTextField1.setText("");
                    jTextField2.setText("");
                    jTextField3.setText("");
                }
            } else {
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
            }

        } else {
            String error = "Warning: File Tough2Viewer.ini not found.";
            JOptionPane.showMessageDialog(null, error);

            Tough2Viewer.toLogFile("Impossibile trovare il file ini");
        }
    }

    public void readnxynzINIFile() {
        ///////////////////////////////////////
        ////lettura URL-primo giro guardo quante sono..///////////////////////
        ///////////////////////////////////////
        boolean exists = (new File("nxynz.ini")).exists();
        if (exists) {

            File file = new File("nxynz.ini");
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            String linea;
            int num_url = 0;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    linea = (String) dis.readLine();
//                jTextNxy.setText(linea);
                    linea = (String) dis.readLine();
//                jTextNz.setText(linea);

                }
                fis.close();
                bis.close();
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
                Tough2Viewer.toLogFile("Impossibile trovare il file nxynz.ini");

            }

        }
    }

    public void readINIPathFile() {

        boolean exists = (new File("path.ini")).exists();
        if (exists) {
            File file = new File("path.ini");
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            String linea;
            int num_url = 0;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    FilePathString = (String) dis.readLine();
                    //era linea=...
                }
                fis.close();
                bis.close();
                dis.close();
            } catch (IOException e) {
                //e.printStackTrace();
                Tough2Viewer.toLogFile("Impossibile trovare il file path.ini");
            }
        }
    }

    public void closeInputBoxVoronoi() {
        if (Tough2Viewer.dataobj.get_dataLoaded()) {
            toINIFile();
            //tonxynzINIFile ();
            toINIPathFile();
            Tough2Viewer.dataobj.set_WorkingPath(FilePathString);
            setVisible(false);

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    //@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButtonSegmt = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jButtonInFile = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButtonLoadLast = new javax.swing.JButton();
        jButtonCreateEmptyModel = new javax.swing.JButton();
        jMeshFile = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Open unstructured grid");

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        jTextField1.setText("FileNameOut");

        jTextField2.setText("FileNameSegmt");

        jButtonSegmt.setText("Segmt File...");
        jButtonSegmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSegmtActionPerformed(evt);
            }
        });

        jTextField3.setText("FileNameIn");

        jButtonInFile.setText("In File...");
        jButtonInFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInFileActionPerformed(evt);
            }
        });

        jProgressBar1.setStringPainted(true);

        jButtonLoadLast.setText("Load last");
        jButtonLoadLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadLastActionPerformed(evt);
            }
        });

        jButtonCreateEmptyModel.setText("CreateEmptyModel");
        jButtonCreateEmptyModel.setEnabled(false);
        jButtonCreateEmptyModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateEmptyModelActionPerformed(evt);
            }
        });

        jMeshFile.setText("Mesh File...");
        jMeshFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMeshFileActionPerformed(evt);
            }
        });

        jLabel4.setText("or");

        jButton1.setText("Simulated File...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(closeButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonCreateEmptyModel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(134, 134, 134)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(74, 74, 74))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(84, 84, 84))))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonSegmt, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                            .addComponent(jButtonLoadLast, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonInFile, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMeshFile, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonCreateEmptyModel, jButtonInFile, jButtonLoadLast, jButtonSegmt});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jProgressBar1, jTextField1, jTextField2, jTextField3});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonLoadLast)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButtonInFile)
                                .addComponent(jMeshFile)
                                .addComponent(jLabel4))
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSegmt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addGap(8, 8, 8)
                        .addComponent(jButtonCreateEmptyModel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonCreateEmptyModel, jButtonInFile, jButtonLoadLast, jButtonSegmt});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCreateEmptyModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateEmptyModelActionPerformed

        CreateEmptyModel();
    }//GEN-LAST:event_jButtonCreateEmptyModelActionPerformed

    private void jButtonLoadLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadLastActionPerformed
        readINIFile();
    }//GEN-LAST:event_jButtonLoadLastActionPerformed

    private void jButtonInFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInFileActionPerformed
        showFileInChooser();
    }//GEN-LAST:event_jButtonInFileActionPerformed

    private void jButtonSegmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSegmtActionPerformed
        showFileSegmtChooser();
    }//GEN-LAST:event_jButtonSegmtActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        closeInputBoxVoronoi();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void jMeshFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMeshFileActionPerformed
        final JFileChooser fc = new JFileChooser(FilePathString);
        fc.setDialogTitle("Open Mesh");
        int returnVal = fc.showOpenDialog(InputBoxVoronoi.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            FilePathString = fc.getCurrentDirectory().toString();
            File file = fc.getSelectedFile();
            String fileName = file.getName();
            String FilePath = file.getAbsolutePath();
            jTextField3.setText(FilePath);
            Tough2Viewer.dataobj.set_InFileName(FilePath);
            if (Tough2Viewer.dataobj.read_MESH_file(file, 1))//1:unstrucured
            {
                jButtonInFile.setEnabled(false);
                jButtonSegmt.setEnabled(true);
                Tough2Viewer.dataobj.ID_grid_type = 1;
            }
        }
    }//GEN-LAST:event_jMeshFileActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        showFileDataChooser1();
    }//GEN-LAST:event_jButton1ActionPerformed
    private void CreateEmptyModel() {
        String fileName = "none";
        Tough2Viewer.dataobj.set_DataFileName(fileName);
        jTextField1.setText(fileName);

        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        int timesteptotal = 1;
        int n_var = 1;
        Tough2Viewer.dataobj.set_number_of_variables(n_var);
        String[] pippo = {"Empty model"};
        Tough2Viewer.dataobj.set_VariableName(pippo);
        Tough2Viewer.dataobj.set_variables_UM(pippo);
        Tough2Viewer.dataobj.dataArrayCreate(nxyz, timesteptotal);
        Tough2Viewer.dataobj.set_dataLoaded(true);

        Tough2Viewer.dataobj.set_voronoi(true);
        Tough2Viewer.dataobj.ID_grid_type = 1;
        Tough2Viewer.tough2viewerGUI.UpdateFileInformation();
        Tough2Viewer.dataobj.set_INIT_ROI();
        //iTough2Viewer.dataobj.read_IN_file();

    }

    public void showFileDataChooser() {
        final JFileChooser fc = new JFileChooser(FilePathString);
        fc.setDialogTitle("Open out");
        int returnVal = fc.showOpenDialog(InputBoxVoronoi.this);
        final File file;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            FilePathString = fc.getCurrentDirectory().toString();
            final String FilePath = file.getAbsolutePath();
            jTextField1.setText(FilePath);

            ReadDataFileActivity readactivity = new ReadDataFileActivity(10, FilePath, jProgressBar1);
            readactivity.execute();

            String fileName = file.getName();
            Tough2Viewer.dataobj.set_DataFileName(fileName);
            FilePathString = FilePath.substring(0, FilePath.length() - fileName.length());
            jTextField1.setText(FilePath);
            Tough2Viewer.dataobj.set_DataFilePath(FilePathString);
            Tough2Viewer.dataobj.ID_grid_type = 1;
            Tough2Viewer.tough2viewerGUI.UpdateFileInformation();

        }
    }

    public void showFileDataChooser1() {
        final JFileChooser fc = new JFileChooser(FilePathString);
        fc.setDialogTitle("Open out");
        int returnVal = fc.showOpenDialog(InputBoxVoronoi.this);
        final File file;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            FilePathString = fc.getCurrentDirectory().toString();
            final String FilePath = file.getAbsolutePath();
            jTextField1.setText(FilePath);

            ReadDataFileActivity1 readactivity1 = new ReadDataFileActivity1(10, FilePath, jProgressBar1);
            readactivity1.execute();

            String fileName = file.getName();
            Tough2Viewer.dataobj.set_DataFileName(fileName);
            FilePathString = FilePath.substring(0, FilePath.length() - fileName.length());
            jTextField1.setText(FilePath);
            Tough2Viewer.dataobj.set_DataFilePath(FilePathString);
            Tough2Viewer.dataobj.ID_grid_type = 1;
            Tough2Viewer.tough2viewerGUI.UpdateFileInformation();

        }
    }

    public void showFileSegmtChooser() {
        final JFileChooser fc = new JFileChooser(FilePathString);
        fc.setDialogTitle("Open Segmt");
        int returnVal = fc.showOpenDialog(InputBoxVoronoi.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            FilePathString = fc.getCurrentDirectory().toString();
            File file = fc.getSelectedFile();
            String fileName = file.getName();
            String FilePath = file.getAbsolutePath();
            jTextField2.setText(FilePath);
            Tough2Viewer.dataobj.set_SegmtFileName(FilePath);
            //inizio to read file
            if (Tough2Viewer.dataobj.read_SEGMT_file()) {
                jButton1.setEnabled(true);
                jButtonCreateEmptyModel.setEnabled(true);

            }
        }
    }

    public void showFileInChooser() {
        final JFileChooser fc = new JFileChooser(FilePathString);
        fc.setDialogTitle("Open In");
        int returnVal = fc.showOpenDialog(InputBoxVoronoi.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            FilePathString = fc.getCurrentDirectory().toString();
            File file = fc.getSelectedFile();
            String fileName = file.getName();
            String FilePath = file.getAbsolutePath();
            jTextField3.setText(FilePath);
            Tough2Viewer.dataobj.set_InFileName(FilePath);
            if (Tough2Viewer.dataobj.read_IN_file()) {
                jButtonSegmt.setEnabled(true);
                Tough2Viewer.dataobj.ID_grid_type = 1;
            }
        }
    }

    public void GetDataFromUrl1() throws IOException {
//throws IOException
        String[] filesToDownload = {"example.out", "segmt", "in"};
        String urlfolder = "http:////137.204.97.169/dist/";
        for (int i = 0; i < 3; i = i + 1) {
            try {
                String filename = urlfolder + filesToDownload[i];

                URL url = new URL(filename);
                URLConnection urlC = url.openConnection();
                InputStream is = url.openStream();
                FileOutputStream fos = null;
                fos = new FileOutputStream(filesToDownload[i]);
                int oneChar, count = 0;
                while ((oneChar = is.read()) != -1) {
                    fos.write(oneChar);
                    count++;
                }
                is.close();
                fos.close();
            } catch (MalformedURLException e) {
                System.err.println(e.toString());
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
        jTextField1.setText(filesToDownload[0]);
        jTextField2.setText(filesToDownload[1]);
        jTextField3.setText(filesToDownload[2]);
        Tough2Viewer.dataobj.set_SegmtFileName(filesToDownload[1]);
        Tough2Viewer.dataobj.set_InFileName(filesToDownload[2]);
        File data_file = new File(jTextField1.getText());
//iTough2Viewer.dataobj.readDataFile(data_file,true,this);

    }

    public void GetDataFromUrl()/* throws IOException*/ {
//throws IOException
        int BUFF_SIZE = 100000;
        byte[] buffer = new byte[BUFF_SIZE];
        String[] filesToDownload = {"example.out", "segmt", "in"};
        String urlfolder = "http://137.204.97.169/dist/";
        InputStream in = null;
        OutputStream out = null;

        File[] outfile = new File[3];
        for (int i = 0; i < 3; i = i + 1) {

            try {
                String tempfolder = System.getProperty("java.io.tmpdir");
                String filename = urlfolder + filesToDownload[i];
                URL url = new URL(filename);
                URLConnection urlC = url.openConnection();
                in = url.openStream();
//      in = new FileInputStream(filename);

                outfile[i] = new File(tempfolder, filesToDownload[i]);

//      out = new FileOutputStream(filesToDownload[i]);
                out = new FileOutputStream(outfile[i]);
                while (true) {
                    synchronized (buffer) {
                        int amountRead = in.read(buffer);
                        if (amountRead == -1) {
                            break;
                        }
                        out.write(buffer, 0, amountRead);
                    }
                }
//   } /*finally*/ {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    if (i == 0) {
                        jTextField1.setText(outfile[i].getAbsolutePath());
                    }
                    if (i == 1) {
                        jTextField2.setText(outfile[i].getAbsolutePath());
                        Tough2Viewer.dataobj.set_SegmtFileName(outfile[i].getAbsolutePath());
                    }
                    if (i == 2) {
                        jTextField3.setText(outfile[i].getAbsolutePath());
                        Tough2Viewer.dataobj.set_InFileName(outfile[i].getAbsolutePath());
                    }
                    out.close();
                }//fine fynally
            }//fine try
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//fine ciclo
        File data_file = new File(jTextField1.getText());
//iTough2Viewer.dataobj.readDataFile(data_file,true,this);
    }
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonCreateEmptyModel;
    private javax.swing.JButton jButtonInFile;
    private javax.swing.JButton jButtonLoadLast;
    private javax.swing.JButton jButtonSegmt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton jMeshFile;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables

}
