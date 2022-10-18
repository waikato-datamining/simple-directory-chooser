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
 * ExpandableNode.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Ancestor for nodes that can be expanded.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class ExpandableNode
  extends DefaultMutableTreeNode {

  /**
   * Expands the node if necessary
   */
  public abstract void expandIfNecessary();

  /**
   * Expands specified directory (if available).
   *
   * @param dir 	the directory to expand (just the directory name)
   * @return		the directory node if successfully expanded
   */
  public DirectoryNode expand(String dir) {
    DirectoryNode	result;
    DirectoryNode	child;
    int			i;

    result = null;

    for (i = 0; i < getChildCount(); i++) {
      if (getChildAt(i) instanceof DirectoryNode) {
        child = (DirectoryNode) getChildAt(i);
        if (child.getName().equals(dir)) {
          result = child;
          break;
        }
      }
    }

    if (result != null)
      result.expandIfNecessary();

    return result;
  }
}
