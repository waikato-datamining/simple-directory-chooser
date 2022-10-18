/*
 * PanelExample.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.examples;

import nz.ac.waikato.cms.adams.simpledirectorychooser.SimpleDirectoryChooserPanel;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeEvent;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeListener;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

/**
 * Shows how to use {@link SimpleDirectoryChooserPanel}.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class PanelExample {

  public static void main(String[] args) throws Exception {
    SimpleDirectoryChooserPanel panel = new SimpleDirectoryChooserPanel();
    panel.addChangeListener(new DirectoryChangeListener() {
      @Override
      public void directoryChanged(DirectoryChangeEvent e) {
        System.out.println("Dir changed: " + e.getCurrentDirectory());
      }
    });
    if (args.length > 0)
      panel.setCurrentDirectory(new File(args[0]));
    else
      panel.setCurrentDirectory(new File(System.getProperty("user.dir")));
    JFrame frame = new JFrame("Panel example");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.setSize(new Dimension(600, 480));
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
