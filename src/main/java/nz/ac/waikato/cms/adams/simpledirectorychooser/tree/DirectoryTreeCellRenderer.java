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
 * DirectoryTreeCellRenderer.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.tree;

import nz.ac.waikato.cms.adams.simpledirectorychooser.icons.IconManager;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;

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
    ImageIcon 	icon;

    super.getTreeCellRendererComponent(tree, value, sel, expanded, false, row, hasFocus);

    if (expanded)
      icon = m_IconManager.getOpenIcon();
    else
      icon = m_IconManager.getClosedIcon();

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
