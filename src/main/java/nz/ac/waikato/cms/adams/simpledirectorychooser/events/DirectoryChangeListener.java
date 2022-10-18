/*
 * DirectoryChangeListener.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.events;

/**
 * Interface for listeners to directory changes.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public interface DirectoryChangeListener {

  /**
   * Gets called when the current directory changes.
   *
   * @param e		the event
   */
  public void directoryChanged(DirectoryChangeEvent e);
}
