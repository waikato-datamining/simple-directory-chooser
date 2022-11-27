/*
 * DirectoryNode.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.tree;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Encapsulates a single directory in the directory tree.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class DirectoryNode
    extends ExpandableNode {

  /** whether to show hidden dirs. */
  protected boolean m_ShowHidden;

  /** whether we need to look for sub-dirs. */
  protected boolean m_Initialized;

  /**
   * Initializes the node with the specified dir.
   *
   * @param owner	the tree this node belongs to
   * @param dir		the directory to encapsulate
   * @param showHidden  whether to show hidden dirs
   */
  public DirectoryNode(DirectoryTree owner, File dir, boolean showHidden) {
    super(owner);

    m_Initialized = false;
    m_ShowHidden  = showHidden;
    setUserObject(dir);
    add(new DummyNode());
  }

  /**
   * Returns whether to show hidden dirs.
   *
   * @return		true if to show
   */
  public boolean getShowHidden() {
    return m_ShowHidden;
  }

  /**
   * Returns the file system view object in use.
   *
   * @return		the view object
   */
  protected FileSystemView getView() {
    return m_Owner.getView();
  }

  /**
   * Returns the encapsulated directory.
   *
   * @return		the directory
   */
  public File getDirectory() {
    return (File) getUserObject();
  }

  /**
   * Whether the directory represents the home directory.
   *
   * @return		true if home directory
   */
  public boolean isHomeDirectory() {
    return new File(System.getProperty("user.home")).equals(getDirectory());
  }

  /**
   * Whether the directory represents a file system root.
   *
   * @return		true if file system root
   */
  public boolean isFileSystemRoot() {
    return getView().isFileSystemRoot(getDirectory());
  }

  /**
   * Returns the directory/drive name, not the full path.
   *
   * @return		the directory/drive name
   */
  public String getName() {
    File	dir;

    dir = getDirectory();
    if (getView().isDrive(dir))
      return dir.getAbsolutePath();
    else
      return dir.getName();
  }

  /**
   * Resets the initialized state.
   */
  public void reset() {
    m_Initialized = false;
  }

  /**
   * Returns whether the node has been initialized.
   *
   * @return		true if initialized
   */
  public boolean isInitialized() {
    return m_Initialized;
  }

  /**
   * Expands the node if necessary
   */
  @Override
  public void expandIfNecessary() {
    File[] 	files;
    List<File> 	dirs;

    if (m_Initialized)
      return;

    m_Initialized = true;

    // find dirs
    dirs  = new ArrayList<>();
    files = getDirectory().listFiles();
    if (files != null) {
      for (File f : files) {
	if (f.isDirectory()) {
	  if (!m_ShowHidden && f.isHidden())
	    continue;
	  dirs.add(f);
	}
      }
      dirs.sort(new Comparator<File>() {
	@Override
	public int compare(File o1, File o2) {
	  return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
	}
      });
    }

    // remove dummy node
    removeAllChildren();

    // add children
    for (File dir: dirs)
      add(new DirectoryNode(getOwner(), dir, getShowHidden()));

    // flag as changed to trigger redraw
    if (getOwner().getModel() instanceof DefaultTreeModel)
      ((DefaultTreeModel) getOwner().getModel()).nodeStructureChanged(this);

    // expand path
    getOwner().expandPath(new TreePath(getPath()));
  }

  /**
   * Returns the directory name.
   *
   * @return		the dir name
   */
  public String toString() {
    return getView().getSystemDisplayName(getDirectory());
  }
}
