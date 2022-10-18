/*
 * IconManager.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.icons;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Manages the icons for the tree.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class IconManager {

  /** the props file with the icon sets. */
  public final static String SETS = "nz/ac/waikato/cms/adams/simpledirectorychooser/icons/sets.props";

  /** the key for the available sets. */
  public final static String KEY_AVAILABLE_SETS = "available_sets";

  /** the key for the active sets. */
  public final static String KEY_ACTIVE_SET = "active_set";

  /** the key prefix for the location. */
  public final static String KEY_LOCATION_PREFIX = "location_";

  /** the key for the icon size. */
  public final static String KEY_ICON_SIZE = "icon_size";

  /** the key for the source. */
  public final static String KEY_SOURCE = "source";

  /** the key for the license. */
  public final static String KEY_LICENSE = "license";

  /** the key for the drive. */
  public final static String KEY_DRIVE = "drive";

  /** the key for the closed folder. */
  public final static String KEY_CLOSED = "closed";

  /** the key for the open folder. */
  public final static String KEY_OPEN = "open";

  /** the default icon size. */
  public final static int DEFAULT_ICON_SIZE = 16;

  /** the properties for the sets. */
  protected Properties m_Sets;

  /** the properties for the active set. */
  protected Properties m_ActiveSet;

  /** the icon cache. */
  protected Map<String, Icon> m_Cache;

  /** the filesystem view. */
  protected FileSystemView m_View;

  /**
   * Initializes the manager with the predefined icon sets.
   */
  public IconManager() {
    this(SETS);
  }

  /**
   * Initializes the manager with the specified props file defining the sets.
   *
   * @param sets	the resource path to the props file with the sets
   */
  public IconManager(String sets) {
    String	msg;
    Properties  props;

    initializeMembers();
    props = loadProperties(sets);
    msg   = checkSets(props);
    if (msg != null)
      throw new IllegalStateException("Icon sets definition invalid:\n" + msg);
    m_Sets = props;
    initializeActiveSet();
  }

  /**
   * Initializes the manager with the properties defining the sets.
   *
   * @param props	the properties with the sets
   */
  public IconManager(Properties props) {
    String	msg;

    initializeMembers();
    msg = checkSets(props);
    if (msg != null)
      throw new IllegalStateException("Icon sets definition invalid:\n" + msg);
    m_Sets = props;
    initializeActiveSet();
  }

  /**
   * Initializes members
   */
  protected void initializeMembers() {
    m_Cache = new HashMap<>();
  }

  /**
   * Loads the specified properties.
   *
   * @param props	the resource path to the props file with the sets
   * @return		the properties
   */
  protected Properties loadProperties(String props) {
    Properties 		result;
    InputStream		stream;

    result = new Properties();
    stream = null;
    try {
      stream = ClassLoader.getSystemResourceAsStream(props);
      result.load(stream);
    }
    catch (Exception e) {
      System.err.println("Failed to read icons sets: " + props);
    }
    finally {
      if (stream != null) {
	try {
	  stream.close();
	}
	catch (Exception e) {
	  // ignored
	}
      }
    }

    return result;
  }

  /**
   * Performs checks on the sets properties.
   *
   * @param props	the properties to check
   * @return		null if checks passed, otherwise error message
   */
  protected String checkSets(Properties props) {
    String[]	sets;

    if (props.getProperty(KEY_AVAILABLE_SETS) == null)
      return "Missing sets key: " + KEY_AVAILABLE_SETS;
    if (props.getProperty(KEY_ACTIVE_SET) == null)
      return "Missing sets key: " + KEY_ACTIVE_SET;

    sets = props.getProperty(KEY_AVAILABLE_SETS).split(",");
    for (String set: sets) {
      if (props.getProperty(KEY_LOCATION_PREFIX + set) == null)
	return "Missing sets location key: " + KEY_LOCATION_PREFIX + set;
    }

    return null;
  }

  /**
   * Performs checks on the icon set properties.
   *
   * @param props	the properties to check
   * @return		null if checks passed, otherwise error message
   */
  protected String checkIconSet(Properties props) {
    if (props.getProperty(KEY_SOURCE) == null)
      return "Missing icon set key: " + KEY_SOURCE;
    if (props.getProperty(KEY_LICENSE) == null)
      return "Missing icon set key: " + KEY_LICENSE;
    if (props.getProperty(KEY_DRIVE) == null)
      return "Missing icon set key: " + KEY_DRIVE;
    if (props.getProperty(KEY_OPEN) == null)
      return "Missing icon set key: " + KEY_OPEN;
    if (props.getProperty(KEY_CLOSED) == null)
      return "Missing icon set key: " + KEY_CLOSED;
    if (props.getProperty(KEY_ICON_SIZE) == null)
      return "Missing sets key: " + KEY_ICON_SIZE;

    return null;
  }

  /**
   * Returns the names of the available sets.
   *
   * @return		the available sets
   */
  public String[] getAvailableSets() {
    return m_Sets.getProperty(KEY_AVAILABLE_SETS).split(",");
  }

  /**
   * Returns the name of the active set.
   *
   * @return		the active set
   */
  public String getActiveSet() {
    return m_Sets.getProperty(KEY_ACTIVE_SET);
  }

  /**
   * Sets the active set.
   *
   * @param name	the name of set to activate
   */
  public void setActiveSet(String name) {
    Set<String>		available;

    available = new HashSet<>(Arrays.asList(getAvailableSets()));
    if (!available.contains(name))
      throw new IllegalStateException("Invalid icon set name (available: " + m_Sets.getProperty(KEY_AVAILABLE_SETS) + "): " + name);

    m_Sets.setProperty(KEY_ACTIVE_SET, name);
    initializeActiveSet();
  }

  /**
   * Initializes the icon set.
   */
  protected void initializeActiveSet() {
    String	filename;
    Properties	props;
    String	msg;

    filename = m_Sets.getProperty(KEY_LOCATION_PREFIX + getActiveSet()) + "/icons.props";
    props    = loadProperties(filename);
    msg      = checkIconSet(props);
    if (msg != null)
      throw new IllegalStateException("Failed to initialize icon set '" + getActiveSet() + "':\n" + msg);
    m_ActiveSet = props;
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
   * Scales the icon to the specified size.
   *
   * @param icon 	the icon to scale
   * @param size 	the size to scale to
   * @return		the scaled image
   */
  public static ImageIcon scale(ImageIcon icon, int size) {
    return new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
  }

  /**
   * Loads the icon.
   *
   * @param filename	the icon to load
   * @return		the icon, null if failed to load
   */
  protected Icon loadIcon(String filename) {
    Icon	result;
    int		size;

    result = null;

    if (m_Cache.containsKey(filename)) {
      result = m_Cache.get(filename);
    }
    else {
      size = getIconSize();
      try {
	result = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(filename));
	result = scale((ImageIcon) result, size);
	m_Cache.put(filename, result);
      }
      catch (Exception e) {
	System.err.println("Failed to load icon: " + filename);
	e.printStackTrace();
	m_Cache.put(filename, null);
      }
    }

    return result;
  }

  /**
   * Returns the icon for the specified key.
   *
   * @param key		the key for the icon
   * @return		the icon, null if not available
   */
  protected Icon getIcon(String key) {
    Icon	result;
    String	dir;
    String	name;
    String	filename;

    dir      = m_Sets.getProperty(KEY_LOCATION_PREFIX + getActiveSet());
    name     = m_ActiveSet.getProperty(key);
    if (name.trim().isEmpty()) {
      result = null;
    }
    else {
      filename = dir + "/" + name;
      result = loadIcon(filename);
    }

    return result;
  }

  /**
   * Returns the drive icon.
   *
   * @return		the icon, null if none available
   */
  public Icon getDriveIcon() {
    return getIcon(KEY_DRIVE);
  }

  /**
   * Returns the open folder icon.
   *
   * @return		the icon, null if none available
   */
  public Icon getOpenIcon() {
    return getIcon(KEY_OPEN);
  }

  /**
   * Returns the closed folder icon.
   *
   * @return		the icon, null if none available
   */
  public Icon getClosedIcon() {
    return getIcon(KEY_CLOSED);
  }

  /**
   * Returns the icon size.
   *
   * @return		the size
   */
  public int getIconSize() {
    try {
      return Integer.parseInt(m_ActiveSet.getProperty(KEY_ICON_SIZE));
    }
    catch (Exception e) {
      return DEFAULT_ICON_SIZE;
    }
  }

  /**
   * Returns the system icon (if any) for the directory.
   *
   * @param dir		the directory to get the icon for
   * @return		the icon, null if none available
   */
  public Icon getSystemIcon(File dir) {
    Icon	result;
    String	path;

    path = dir.getAbsolutePath();
    if (m_Cache.containsKey(path))
      return m_Cache.get(path);

    result = getView().getSystemIcon(dir);
    if (result instanceof ImageIcon)
      result = scale((ImageIcon) result, getIconSize());

    m_Cache.put(path, result);

    return result;
  }
}
