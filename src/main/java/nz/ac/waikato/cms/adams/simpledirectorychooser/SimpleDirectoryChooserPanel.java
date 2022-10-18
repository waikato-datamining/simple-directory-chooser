/*
 * SimpleDirectoryChooserPanel.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser;

import nz.ac.waikato.cms.adams.simpledirectorychooser.core.SimplePanel;
import nz.ac.waikato.cms.adams.simpledirectorychooser.core.SimpleScrollPane;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeListener;
import nz.ac.waikato.cms.adams.simpledirectorychooser.icons.IconManager;
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
   * Returns whether hidden directories are shown.
   *
   * @return		true if shown
   */
  public boolean getShowHidden() {
    return m_Tree.getShowHidden();
  }

  /**
   * Sets whether hidden dirs are shown.
   *
   * @param value	true if to show hidden dirs
   */
  public void setShowHidden(boolean value) {
    m_Tree.setShowHidden(value);
  }

  /**
   * Sets the icon manager to use.
   *
   * @param value	the manager
   */
  public void setIconManager(IconManager value) {
    m_Tree.setIconManager(value);
  }

  /**
   * Returns the icon manager in use.
   *
   * @return		the manager
   */
  public IconManager getIconManager() {
    return m_Tree.getIconManager();
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

  /**
   * Makes sure that the specified file is viewable, and
   * not hidden.
   *
   * @param f  a File object
   */
  public void ensureFileIsVisible(File f) {
    m_Tree.ensureFileIsVisible(f);
  }

  /**
   * Tells the UI to rescan its files list from the current directory.
   */
  public void rescanCurrentDirectory() {
    m_Tree.rescanCurrentDirectory();
  }
}
