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
