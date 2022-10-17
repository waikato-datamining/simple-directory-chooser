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
 * MultiRootNode.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.tree;

import java.io.File;

/**
 * For managing multiple roots.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class MultiRootNode
  extends ExpandableNode {

  /** the default title. */
  public final static String DEFAULT_TITLE = "Computer";

  /**
   * Sets the root dirs to manage. Uses {@link #DEFAULT_TITLE} as title.
   *
   * @param roots	the roots
   * @param showHidden  whether to show hidden dirs
   */
  public MultiRootNode(File[] roots, boolean showHidden) {
    this(roots, DEFAULT_TITLE, showHidden);
  }

  /**
   * Sets the root dirs to manage.
   *
   * @param roots	the roots
   * @param title 	the title to use for the node
   * @param showHidden  whether to show hidden dirs
   */
  public MultiRootNode(File[] roots, String title, boolean showHidden) {
    super();
    setUserObject(title);
    for (File root: roots)
      add(new DirectoryNode(root, showHidden));
  }

  /**
   * Expands the node if necessary
   */
  @Override
  public void expandIfNecessary() {
    int		i;

    for (i = 0; i < getChildCount(); i++)
      ((DirectoryNode) getChildAt(i)).expandIfNecessary();
  }
}
