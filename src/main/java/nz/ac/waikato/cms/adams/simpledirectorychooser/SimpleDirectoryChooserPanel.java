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
import nz.ac.waikato.cms.adams.simpledirectorychooser.tree.DirectoryTreePopupMenuCustomizer;

import javax.swing.filechooser.FileSystemView;
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
   * Refreshes the tree.
   */
  public void refresh() {
    m_Tree.refresh();
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

  /**
   * Sets the file system view to use.
   *
   * @param value	the view object
   */
  public void setView(FileSystemView value) {
    m_Tree.setView(value);
  }

  /**
   * Returns the file system view object in use.
   *
   * @return		the view object
   */
  public FileSystemView getView() {
    return m_Tree.getView();
  }

  /**
   * Requests the focus for the tree.
   */
  public boolean requestFocusInWindow() {
    return m_Tree.requestFocusInWindow();
  }

  /**
   * Sets the customizer for the popup menu.
   *
   * @param value	the customizer, null to remove
   */
  public void setPopupMenuCustomizer(DirectoryTreePopupMenuCustomizer value) {
    m_Tree.setPopupMenuCustomizer(value);
  }

  /**
   * Returns the customizer for the popup menu.
   *
   * @return		the customizer, null if none used
   */
  public DirectoryTreePopupMenuCustomizer getPopupMenuCustomizer() {
    return m_Tree.getPopupMenuCustomizer();
  }

  /**
   * Sets whether the popup menu is enabled.
   *
   * @param value	true to enable
   */
  public void setPopupMenuEnabled(boolean value) {
    m_Tree.setPopupMenuEnabled(value);
  }

  /**
   * Returns whether the popup menu is enabled.
   *
   * @return		true if enabled
   */
  public boolean isPopupMenuEnabled() {
    return m_Tree.isPopupMenuEnabled();
  }

  /**
   * Let's the user create a new folder.
   *
   * @param grabFocus 	whether to grab the focus
   */
  public void newFolder(boolean grabFocus) {
    m_Tree.newFolder(grabFocus);
  }
}
