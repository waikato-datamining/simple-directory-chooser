/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * StandardJFileChooser.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.examples;

import javax.swing.JFileChooser;
import java.io.File;

/**
 * Choosing directories using the standard {@link JFileChooser} dialog.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class StandardJFileChooser {

  public static void main(String[] args) throws Exception {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (args.length > 0)
      fileChooser.setCurrentDirectory(new File(args[0]));
    else
      fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    int retVal = fileChooser.showOpenDialog(null);
    if (retVal == JFileChooser.APPROVE_OPTION)
      System.out.println(fileChooser.getSelectedFile());
    else
      System.err.println("Cancelled");
  }
}
