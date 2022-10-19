/*
 * GUIHelper.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.core;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * GUI related helper functions.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class GUIHelper {

  /**
   * Tries to determine the parent this panel is part of.
   *
   * @param cont	the container to get the parent for
   * @param parentClass	the class of the parent to obtain
   * @return		the parent if one exists or null if not
   */
  public static Object getParent(Container cont, Class parentClass) {
    Container	result;
    Container	parent;

    result = null;

    parent = cont;
    while (parent != null) {
      if (parentClass.isInstance(parent)) {
	result = parent;
	break;
      }
      else {
	parent = parent.getParent();
      }
    }

    return result;
  }

  /**
   * Tries to determine the frame the container is part of.
   *
   * @param cont	the container to get the frame for
   * @return		the parent frame if one exists or null if not
   */
  public static Frame getParentFrame(Container cont) {
    return (Frame) getParent(cont, Frame.class);
  }

  /**
   * Tries to determine the dialog this component is part of.
   *
   * @param comp	the component to get the dialog for
   * @return		the parent dialog if one exists or null if not
   */
  public static Dialog getParentDialog(Component comp) {
    if (comp instanceof Container)
      return (Dialog) getParent((Container) comp, Dialog.class);
    else
      return null;
  }

  /**
   * Tries to determine the window the container is part of.
   *
   * @param comp	the component to get the window for
   * @return		the parent window if one exists or null if not
   */
  public static Window getParentWindow(Component comp) {
    return (Window) getParent((Container) comp, Window.class);
  }

  /**
   * Writes the icon as PNG to the specified file.
   *
   * @param icon	the icon to output
   * @param filename	the filename to save it under
   * @throws IOException        if writing fails
   */
  public static void saveIconAsPng(Icon icon, String filename) throws IOException {
    BufferedImage image;
    Graphics2D g;

    image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    g     = image.createGraphics();
    icon.paintIcon(null, g, 0, 0);
    ImageIO.write(image, "png", new File(filename));
  }

  /**
   * Scales the icon to the specified size.
   *
   * @param icon 	the icon to scale
   * @param size 	the size to scale to
   * @return		the scaled image
   */
  public static ImageIcon scaleIcon(ImageIcon icon, int size) {
    return new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
  }

  /**
   * Loads the icon from a resource path.
   *
   * @param resource	the icon to load
   * @param size	the size to scale to
   * @return		the icon, null if failed to load
   */
  public static Icon loadIcon(String resource, int size) {
    Icon	result;

    result = null;

    try {
      result = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(resource));
      result = scaleIcon((ImageIcon) result, size);
    }
    catch (Exception e) {
      System.err.println("Failed to load icon: " + resource);
      e.printStackTrace();
    }

    return result;
  }

  /**
   * Checks whether the mouse event is a right-click event.
   * Alt+Left-Click is also interpreted as right-click.
   *
   * @param e		the event
   * @return		true if a right-click event
   */
  public static boolean isRightClick(MouseEvent e) {
    boolean	result;

    result = false;

    if ((e.getButton() == MouseEvent.BUTTON3) && (e.getClickCount() == 1))
      result = true;
    else if ((e.getButton() == MouseEvent.BUTTON1) && e.isAltDown() && !e.isShiftDown() && !e.isControlDown())
      result = true;

    return result;
  }
}
