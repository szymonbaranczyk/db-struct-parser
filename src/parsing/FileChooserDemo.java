package parsing;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileFilter;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;


public class FileChooserDemo extends JPanel
        implements ActionListener {
    static private final String newline = "\n";
    java.util.List<Table> tables = null;
    JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fc;

    public FileChooserDemo() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5, 20);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();


        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);


        FileNameExtensionFilter filter = new FileNameExtensionFilter("sql", "sql");
        fc.addChoosableFileFilter(filter);


        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Open a File...");
        openButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save a File...");
        saveButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);

        saveButton.setVisible(false);

        JPanel imagePanel=new JPanel();
        ImageIcon ii=new ImageIcon("./samples/giphy.gif");
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(ii);
        imagePanel.add(imageLabel);

        add(imagePanel, BorderLayout.AFTER_LAST_LINE);

    }

    public void actionPerformed(ActionEvent e) {
        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(FileChooserDemo.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    DBParser dbp = new DBParser();
                    tables = dbp.parse(file);
                } catch (FileNotFoundException e1) {
                    log.append("File was not found." + newline);
                    e1.printStackTrace();
                }
                log.append("Opened: " + file.getName() + "." + newline);
                saveButton.setVisible(true);
            } else {
                log.append("Open command cancelled by user." + newline);
                saveButton.setVisible(false);
            }
            log.setCaretPosition(log.getDocument().getLength());


            //here maybe hello parser


            //Handle save button action.
        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(FileChooserDemo.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                JavaSaver js = new JavaSaver();
                try {
                    js.save(file, tables);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    log.append("File could not be found." + newline);
                }
                log.append("Saved: " + file.getName() + "." + newline);
            } else {
                log.append("Save command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
    }

//    /** Returns an ImageIcon, or null if the path was invalid. */
    /*protected static ImageIcon createImageIcon() {
        String path="./samples/giphy.gif";
        java.net.URL imgURL = FileChooserDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }

        ImageIcon ii=new ImageIcon("./samples/giphy.gif");
        return ii;
    }*/


    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new FileChooserDemo());



        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
