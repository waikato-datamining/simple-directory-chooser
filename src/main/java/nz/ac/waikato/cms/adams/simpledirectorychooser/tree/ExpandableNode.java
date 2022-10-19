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

  /** the owner. */
  protected DirectoryTree m_Owner;

  /**
   * Initializes the node.
   *
   * @param owner	the tree this node belongs to
   */
  protected ExpandableNode(DirectoryTree owner) {
    m_Owner = owner;
  }

  public DirectoryTree getOwner() {
    return m_Owner;
  }

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
