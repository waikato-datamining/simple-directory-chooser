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
 * SimpleDirectoryChooserPanel.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser;

import nz.ac.waikato.cms.adams.simpledirectorychooser.core.DirectoryTree;
import nz.ac.waikato.cms.adams.simpledirectorychooser.core.SimplePanel;
import nz.ac.waikato.cms.adams.simpledirectorychooser.core.SimpleScrollPane;

import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.io.File;

/**
 * Panel for selecting a directory.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class SimpleDirectoryChooserPanel
    extends SimplePanel {

  /** the tree with the directory structure. */
  protected DirectoryTree m_Tree;

  /**
   * Initializes the widget.
   */
  protected void initGUI() {
    setLayout(new BorderLayout());

    m_Tree = new DirectoryTree();
    add(new SimpleScrollPane(m_Tree), BorderLayout.CENTER);
  }

  /**
   * Sets the current directory.
   *
   * @param value	the directory
   */
  public void setCurrentDirectory(File value) {
    m_Tree.setCurrentDirectory(value);
  }

  /**
   * Returns the current directory.
   *
   * @return		the directory
   */
  public File getCurrentDirectory() {
    return m_Tree.getCurrentDirectory();
  }
}
