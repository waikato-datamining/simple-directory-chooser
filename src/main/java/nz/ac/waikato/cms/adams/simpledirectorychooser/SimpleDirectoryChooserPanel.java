/*
 * SimpleDirectoryChooserPanel.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser;

import nz.ac.waikato.cms.adams.simpledirectorychooser.core.SimplePanel;
import nz.ac.waikato.cms.adams.simpledirectorychooser.core.SimpleScrollPane;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeListener;
import nz.ac.waikato.cms.adams.simpledirectorychooser.tree.DirectoryTree;

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

  /**
   * Adds the listener for changes in the selected directory.
   *
   * @param l		the listener to add
   */
  public void addChangeListener(DirectoryChangeListener l) {
    m_Tree.addChangeListener(l);
  }

  /**
   * Removes the listener for changes in the selected directory.
   *
   * @param l		the listener to remove
   */
  public void removeChangeListener(DirectoryChangeListener l) {
    m_Tree.removeChangeListener(l);
  }
}
