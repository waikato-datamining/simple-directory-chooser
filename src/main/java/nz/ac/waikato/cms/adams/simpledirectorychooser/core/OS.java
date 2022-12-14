/*
 * OS.java
 * Copyright (C) 2011-2015 University of Waikato, Hamilton, New Zealand
 */
package nz.ac.waikato.cms.adams.simpledirectorychooser.core;

import java.io.File;

/**
 * Helper class for operating system related stuff.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class OS {

  /**
   * Enumeration of OS.
   */
  public enum OperatingSystems {
    LINUX,
    ANDROID,
    MAC,
    WINDOWS
  }

  /** whether the OS is Windows. */
  protected static Boolean m_IsWindows;

  /** whether the OS is Mac. */
  protected static Boolean m_IsMac;

  /** whether the OS is Linux. */
  protected static Boolean m_IsLinux;

  /** whether the OS is Android. */
  protected static Boolean m_IsAndroid;

  /**
   * Checks whether the operating system is Windows.
   *
   * @return		true if the OS is Windows flavor
   */
  public static synchronized boolean isWindows() {
    if (m_IsWindows == null)
      m_IsWindows = System.getProperty("os.name").toLowerCase().contains("windows");

    return m_IsWindows;
  }

  /**
   * Checks whether the operating system is Mac.
   *
   * @return		true if the OS is Mac flavor
   */
  public synchronized static boolean isMac() {
    if (m_IsMac == null)
      m_IsMac = System.getProperty("os.name").toLowerCase().startsWith("mac os");

    return m_IsMac;
  }

  /**
   * Checks whether the operating system is Linux (but not Android).
   *
   * @return		true if the OS is Linux flavor (but not Android)
   */
  public synchronized static boolean isLinux() {
    String os;

    if (m_IsLinux == null)
      m_IsLinux = System.getProperty("os.name").toLowerCase().startsWith("linux") && !isAndroid();

    return m_IsLinux;
  }

  /**
   * Checks whether the operating system is Android.
   *
   * @return		true if the OS is Android flavor
   */
  public synchronized static boolean isAndroid() {
    if (m_IsAndroid == null) {
      m_IsAndroid = System.getProperty("java.vm.vendor").toLowerCase().contains("android")
	  || System.getProperty("java.vendor").toLowerCase().contains("android")
	  || System.getProperty("java.vendor.url").toLowerCase().contains("android");
    }

    return m_IsAndroid;
  }

  /**
   * Tests whether the current OS is the same as the provided parameter.
   *
   * @param os		the OS to test
   * @return		true if it is the OS
   */
  public synchronized static boolean isOS(OperatingSystems os) {
    switch (os) {
      case LINUX:
	return isLinux();
      case MAC:
	return isMac();
      case ANDROID:
	return isAndroid();
      case WINDOWS:
	return isWindows();
      default:
	throw new IllegalStateException("Unhandled OS: " + os);
    }
  }

  /**
   * Returns the "bitness", ie 32 or 64 bit of the underlying OS.
   *
   * @return		the number of bits
   */
  public synchronized static int getBitness() {
    String arch;

    arch = System.getProperty("os.arch");
    if (arch.endsWith("86"))
      return 32;
    else if (arch.endsWith("64"))
      return 64;
    else
      throw new IllegalStateException("Cannot interpret 'os.arch' for bitness: " + arch);
  }

  /**
   * Returns a string representation of the file. null if the file is null.
   *
   * @param f		the file to turn into a string
   * @return		the string
   */
  public static String fileToString(File f) {
    if (f == null)
      return null;
    else
      return f.getAbsolutePath();
  }
}
