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
 * DirectoryNode.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.tree;

import javax.swing.filechooser.FileSystemView;
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

  /** the filesystem view. */
  protected FileSystemView m_View;

  /** whether to show hidden dirs. */
  protected boolean m_ShowHidden;

  /** whether we need to look for sub-dirs. */
  protected boolean m_Initialized;

  /**
   * Initializes the node with the specified dir.
   *
   * @param dir		the directory to encapsulate
   * @param showHidden  whether to show hidden dirs
   */
  public DirectoryNode(File dir, boolean showHidden) {
    super();

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
    if (m_View == null)
      m_View = FileSystemView.getFileSystemView();
    return m_View;
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
      add(new DirectoryNode(dir, getShowHidden()));
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
