/*
 * DirectoryTree.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.tree;

import nz.ac.waikato.cms.adams.simpledirectorychooser.core.OS;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeEvent;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeListener;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  /** the change listeners. */
  protected Set<DirectoryChangeListener> m_ChangeListeners;

  /** the filesystem view. */
  protected FileSystemView m_View;

  /** the icon manager. */
  protected IconManager m_IconManager;

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
    this(showHidden, new IconManager(), FileSystemView.getFileSystemView());
  }

  /**
   * Initializes the tree.
   *
   * @param showHidden 	whether to show hidden dirs
   * @param iconManager the icon manager to use
   * @param view	the view to use
   */
  public DirectoryTree(boolean showHidden, IconManager iconManager, FileSystemView view) {
    super();
    initializeMembers();
    m_ShowHidden = showHidden;
    m_View       = view;
    setIconManager(iconManager);
    buildTree();
  }

  /**
   * Initializes the members.
   */
  protected void initializeMembers() {
    m_CurrentDir      = null;
    m_ChangeListeners = new HashSet<>();
    m_ShowHidden      = false;
    m_View            = null;
    m_IconManager     = new IconManager();
    setCellRenderer(new DirectoryTreeCellRenderer());
  }

  /**
   * Builds the tree.
   */
  protected void buildTree() {
    DefaultTreeModel  	model;
    File[]		roots;
    List<File>		allRoots;

    allRoots = new ArrayList<>(Arrays.asList(FileSystemView.getFileSystemView().getRoots()));
    if (OS.isWindows())
      allRoots.addAll(Arrays.asList(File.listRoots()));

    roots = allRoots.toArray(new File[0]);
    if (roots.length == 1)
      model = new DefaultTreeModel(new DirectoryNode(this, roots[0], m_ShowHidden));
    else
      model = new DefaultTreeModel(new MultiRootNode(this, roots, m_ShowHidden));
    ((ExpandableNode) model.getRoot()).expandIfNecessary();

    setRootVisible(roots.length == 1);
    setShowsRootHandles(true);
    setModel(model);

    addTreeWillExpandListener(this);
    addTreeSelectionListener(this);
  }

  /**
   * Sets the file system view to use.
   *
   * @param value	the view object
   */
  public void setView(FileSystemView value) {
    m_View = value;
  }

  /**
   * Returns the file system view object in use.
   *
   * @return		the view object
   */
  public FileSystemView getView() {
    if (m_View == null)
      m_View = FileSystemView.getFileSystemView();
    return m_View;
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
    }
    else {
      m_CurrentDir = null;
    }

    notifyChangeListeners();
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
    FileSystemView	view;

    view = FileSystemView.getFileSystemView();

    // file provided?
    if (!value.isDirectory())
      value = value.getParentFile();

    result = new ArrayList<>();
    while (value != null) {
      if (!value.getName().isEmpty())
	result.add(0, value.getName());
      else if (view.isDrive(value))
	result.add(0, value.getAbsolutePath());
      value = value.getParentFile();
    }

    // "." at the end?
    if ((result.size() > 0) && result.get(result.size() - 1).equals("."))
      result.remove(result.size() - 1);

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
    parts  = toPathElements(value.getAbsoluteFile());
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
   * Makes sure that the specified file is viewable, and
   * not hidden.
   *
   * @param f  a File object
   */
  public void ensureFileIsVisible(File f) {
    DirectoryNode	node;
    TreePath		path;

    node = expandDirectory(f);
    if (node != null) {
      path = new TreePath(node.getPath());
      scrollPathToVisible(path);
    }
  }

  /**
   * Tells the UI to rescan its files list from the current directory.
   */
  public void rescanCurrentDirectory() {
    DirectoryNode 	node;

    node = expandDirectory(getCurrentDirectory());
    node.reset();
    node.expandIfNecessary();
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
   * Sets the icon manager to use. Also updates its file system view.
   *
   * @param value	the manager
   */
  public void setIconManager(IconManager value) {
    DirectoryTreeCellRenderer renderer;

    m_IconManager = value;
    m_IconManager.setView(getView());
    refresh();
  }

  /**
   * Returns the icon manager in use.
   *
   * @return		the manager
   */
  public IconManager getIconManager() {
    return m_IconManager;
  }

  /**
   * Adds the listener for changes in the selected directory.
   *
   * @param l		the listener to add
   */
  public void addChangeListener(DirectoryChangeListener l) {
    m_ChangeListeners.add(l);
  }

  /**
   * Removes the listener for changes in the selected directory.
   *
   * @param l		the listener to remove
   */
  public void removeChangeListener(DirectoryChangeListener l) {
    m_ChangeListeners.remove(l);
  }

  /**
   * Notifies all change listeners that the current directory has changed.
   */
  protected void notifyChangeListeners() {
    DirectoryChangeEvent	e;

    e = new DirectoryChangeEvent(this, getCurrentDirectory());
    for (DirectoryChangeListener l: m_ChangeListeners.toArray(new DirectoryChangeListener[0]))
      l.directoryChanged(e);
  }
}
