/*
 * DirectoryChangeEvent.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.events;

import nz.ac.waikato.cms.adams.simpledirectorychooser.tree.DirectoryTree;

import java.io.File;
import java.util.EventObject;

/**
 * Event that gets sent when the current directory changes in a {@link DirectoryTree}.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class DirectoryChangeEvent
  extends EventObject {

  /** the current directory. */
  protected File m_CurrentDirectory;

  /**
   * Initializes the event.
   *
   * @param source The object on which the Event initially occurred.
   * @throws IllegalArgumentException if source is null.
   */
  public DirectoryChangeEvent(DirectoryTree source, File currentDirectory) {
    super(source);
    m_CurrentDirectory = currentDirectory;
  }

  /**
   * Returns the directory tree that triggered the event.
   *
   * @return		the tree
   */
  public DirectoryTree getDirectoryTree() {
    return (DirectoryTree) getSource();
  }

  /**
   * Returns the current directory (at the time the event was created).
   *
   * @return		the directory
   */
  public File getCurrentDirectory() {
    return m_CurrentDirectory;
  }
}
