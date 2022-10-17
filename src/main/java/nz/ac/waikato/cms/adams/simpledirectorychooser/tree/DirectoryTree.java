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
 * DirectoryTree.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.tree;

import nz.ac.waikato.cms.adams.simpledirectorychooser.icons.IconManager;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays the directory structure.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class DirectoryTree
    extends JTree
    implements TreeWillExpandListener, TreeSelectionListener {

  /** the current directory. */
  protected File m_CurrentDir;

  /** whether to show hidden dirs. */
  protected boolean m_ShowHidden;

  /**
   * Initializes the tree. Does not show hidden dirs.
   */
  public DirectoryTree() {
    this(false);
  }

  /**
   * Initializes the tree.
   *
   * @param showHidden 	whether to show hidden dirs
   */
  public DirectoryTree(boolean showHidden) {
    super();
    setIconManager(new IconManager());
    m_ShowHidden = showHidden;
    buildTree();
  }

  /**
   * Builds the tree.
   */
  protected void buildTree() {
    DefaultTreeModel  	model;
    File[]		roots;

    roots = FileSystemView.getFileSystemView().getRoots();
    if (roots.length == 1)
      model = new DefaultTreeModel(new DirectoryNode(roots[0], m_ShowHidden));
    else
      model = new DefaultTreeModel(new MultiRootNode(roots, m_ShowHidden));
    ((ExpandableNode) model.getRoot()).expandIfNecessary();

    setRootVisible(roots.length == 1);
    setShowsRootHandles(true);
    setModel(model);

    addTreeWillExpandListener(this);
    addTreeSelectionListener(this);
  }

  /**
   * Gets called when the tree is about to be expanded.
   *
   * @param event	the event
   * @throws ExpandVetoException
   */
  @Override
  public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
    DirectoryNode   node;

    if ((event.getPath() != null) && (event.getPath().getLastPathComponent() instanceof DirectoryNode)) {
      node = (DirectoryNode) event.getPath().getLastPathComponent();
      node.expandIfNecessary();
    }
  }

  /**
   * Gets called when the tree is about to be collapsed.
   *
   * @param event	the event
   * @throws ExpandVetoException
   */
  @Override
  public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
  }

  /**
   * Called whenever the value of the selection changes.
   *
   * @param e the event that characterizes the change.
   */
  @Override
  public void valueChanged(TreeSelectionEvent e) {
    DirectoryNode   node;

    if ((e.getPath() != null) && (e.getPath().getLastPathComponent() instanceof DirectoryNode)) {
      node = (DirectoryNode) e.getPath().getLastPathComponent();
      m_CurrentDir = node.getDirectory();
      // TODO change event
    }
  }

  /**
   * Refreshes the tree.
   */
  public void refresh() {
    File  curDir;

    curDir = getCurrentDirectory();

    buildTree();

    if (curDir != null)
      setCurrentDirectory(curDir);
  }

  /**
   * Turns the directory into its path elements.
   *
   * @param value	the directory to split
   * @return		the path elements
   */
  protected String[] toPathElements(File value) {
    List<String> 	result;

    // file provided?
    if (!value.isDirectory())
      value = value.getParentFile();

    result = new ArrayList<>();
    while (value != null) {
      if (!value.getName().isEmpty())
	result.add(0, value.getName());
      value = value.getParentFile();
    }

    return result.toArray(new String[0]);
  }

  /**
   * Expands the specified directory (if possible), but does not select it.
   *
   * @param value	the directory to expand
   * @return		the child node, if successfully expanded
   */
  public DirectoryNode expandDirectory(File value) {
    DirectoryNode	result;
    String[] 		parts;
    int			i;
    ExpandableNode	node;
    ExpandableNode 	root;

    result = null;
    root   = (ExpandableNode) getModel().getRoot();
    parts  = toPathElements(value);
    node   = root;
    for (i = 0; i < parts.length; i++) {
      node = node.expand(parts[i]);
      if (node == null)
	break;
      if (i == parts.length - 1)
	result = (DirectoryNode) node;
    }

    return result;
  }

  /**
   * Sets the current directory.
   *
   * @param value	the directory
   */
  public void setCurrentDirectory(File value) {
    DirectoryNode	node;
    TreePath		path;

    node = expandDirectory(value);
    if (node != null) {
      m_CurrentDir = node.getDirectory();
      path = new TreePath(node.getPath());
      setSelectionPath(path);
      scrollPathToVisible(path);
    }
  }

  /**
   * Returns the current directory.
   *
   * @return		the directory
   */
  public File getCurrentDirectory() {
    return m_CurrentDir;
  }

  /**
   * Returns whether hidden directories are shown.
   *
   * @return		true if shown
   */
  public boolean getShowHidden() {
    return m_ShowHidden;
  }

  /**
   * Sets whether hidden dirs are shown.
   *
   * @param value	true if to show hidden dirs
   */
  public void setShowHidden(boolean value) {
    m_ShowHidden = value;
    refresh();
  }

  /**
   * Sets the icon manager to use.
   *
   * @param value	the manager
   */
  public void setIconManager(IconManager value) {
    DirectoryTreeCellRenderer renderer;

    renderer = new DirectoryTreeCellRenderer(value);
    setCellRenderer(renderer);
  }

  /**
   * Returns the icon manager in use.
   *
   * @return		the manager
   */
  public IconManager getIconManager() {
    return ((DirectoryTreeCellRenderer) getCellRenderer()).getIconManager();
  }
}
