/*
 * SimpleDirectoryChooserMultiSelectionExample.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.examples;

import nz.ac.waikato.cms.adams.simpledirectorychooser.SimpleDirectoryChooser;

import javax.swing.JFileChooser;
import java.io.File;

/**
 * Choosing directories using the {@link SimpleDirectoryChooser} dialog.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class SimpleDirectoryChooserMultiSelectionExample {

  public static void main(String[] args) throws Exception {
    SimpleDirectoryChooser fileChooser = new SimpleDirectoryChooser();
    fileChooser.setPopupMenuEnabled(true);
    fileChooser.setMultiSelectionEnabled(true);
    fileChooser.setSortSelectedDirectories(true);
    File[] initial;
    if (args.length > 0) {
      initial = new File[args.length];
      for (int i = 0; i < args.length; i++)
        initial[i] = new File(args[i]);
    }
    else {
      initial = new File[]{
          new File(System.getProperty("user.home")),
          new File(System.getProperty("user.dir")),
          new File(System.getProperty("java.io.tmpdir")),
      };
    }
    fileChooser.setSelectedFiles(initial);
    int retVal = fileChooser.showOpenDialog(null);
    if (retVal == JFileChooser.APPROVE_OPTION) {
      for (File f: fileChooser.getSelectedFiles())
        System.out.println(f);
    }
    else {
      System.err.println("Cancelled");
    }
  }
}
