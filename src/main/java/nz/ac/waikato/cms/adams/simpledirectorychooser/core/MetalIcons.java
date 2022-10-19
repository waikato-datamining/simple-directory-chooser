/*
 * MetalIcons.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.core;

import javax.swing.plaf.metal.MetalIconFactory;

/**
 * Helper class for saving Metal Look'n'Feel icons as PNGs.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class MetalIcons {

  /**
   * Saves the images in the specified output director (1st argument).
   *
   * @param args	the output dir
   * @throws Exception	if writing fails
   */
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("Missing argument: <output dir>");
      return;
    }
    String outDir = args[0];
    GUIHelper.saveIconAsPng(MetalIconFactory.getFileChooserHomeFolderIcon(), outDir + "/home.png");
    GUIHelper.saveIconAsPng(MetalIconFactory.getFileChooserNewFolderIcon(), outDir + "/new_folder.png");
  }
}
