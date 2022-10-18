/*
 * DirectoryTreeCellRenderer.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.tree;

import nz.ac.waikato.cms.adams.simpledirectorychooser.icons.IconManager;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Renderer for the directory tree.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class DirectoryTreeCellRenderer
    extends DefaultTreeCellRenderer {

  /** the icon manager. */
  protected IconManager m_IconManager;

  /**
   * Initializes the renderer.
   *
   * @param manager	the icon manager to use
   */
  public DirectoryTreeCellRenderer(IconManager manager) {
    super();
    m_IconManager = manager;
  }

  /**
   * Returns the component to use.
   *
   * @param tree	the tree
   * @param value	the node value
   * @param sel		whether the node is selected
   * @param expanded	whether the node is selected
   * @param leaf	whether the node is a lead
   * @param row		the row of the node in the tree
   * @param hasFocus	whether the node is focused
   * @return		the component for rendering
   */
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    Icon 		icon;
    DirectoryNode 	node;
    int			height;
    int			width;
    Dimension 		minimum;
    Dimension		preferred;

    super.getTreeCellRendererComponent(tree, value, sel, expanded, false, row, hasFocus);

    if (expanded)
      icon = m_IconManager.getOpenIcon();
    else
      icon = m_IconManager.getClosedIcon();

    if ((icon == null) && (value instanceof DirectoryNode)) {
      node = (DirectoryNode) value;
      icon = ((DirectoryTree) tree).getIconManager().getSystemIcon(node.getDirectory());
    }

    if (icon != null)
      setIcon(icon);

    return this;
  }

  /**
   * Returns the underlying icon manager.
   *
   * @return		the manager
   */
  public IconManager getIconManager() {
    return m_IconManager;
  }
}
