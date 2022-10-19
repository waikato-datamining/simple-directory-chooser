/*
 * SimpleDirectoryChooser.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser;

import nz.ac.waikato.cms.adams.simpledirectorychooser.core.GUIHelper;
import nz.ac.waikato.cms.adams.simpledirectorychooser.core.OS;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeEvent;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeListener;
import nz.ac.waikato.cms.adams.simpledirectorychooser.icons.IconManager;
import nz.ac.waikato.cms.adams.simpledirectorychooser.tree.DirectoryTreePopupMenuCustomizer;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Directory chooser widget with an interface compatible with JFileChooser,
 * to make it easy to swap in/out.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class SimpleDirectoryChooser
    extends JComponent
    implements DirectoryChangeListener {

  /** open or save. */
  protected int m_DialogType;

  /** eg accepted. */
  protected int m_ReturnValue;

  /** the title of the dialog. */
  protected String m_DialogTitle;

  /** the file system view. */
  protected FileSystemView m_View;

  /** the panel with the directories. */
  protected SimpleDirectoryChooserPanel m_PanelDirs;

  /** the dialog. */
  protected JDialog m_Dialog;

  /** the approve button. */
  protected JButton m_ButtonApprove;

  /** the cancel button. */
  protected JButton m_ButtonCancel;

  /** the accessory. */
  protected JComponent m_Accessory;

  /** the panel with the widgets. */
  protected JPanel m_PanelWidgets;

  /** the panel with dirs and accessory. */
  protected JPanel m_PanelContent;

  /** the label for the text field with current directory. */
  protected JLabel m_LabelDirectory;

  /** the text field with the current directory. */
  protected JTextField m_TextDirectory;

  /** the panel with the buttons. */
  protected JPanel m_PanelButtons;

  /** the toolbar. */
  protected JToolBar m_Toolbar;

  /** the home button. */
  protected JButton m_ButtonHome;

  /** the new folder button. */
  protected JButton m_ButtonNewFolder;

  /** the refresh button. */
  protected JButton m_ButtonRefresh;

  /**
   * Constructs a <code>BaseFileChooser</code> pointing to the user's
   * default directory. This default depends on the operating system.
   * It is typically the "My Documents" folder on Windows, and the
   * user's home directory on Unix.
   */
  public SimpleDirectoryChooser() {
    this(System.getProperty("user.dir"));
  }

  /**
   * Constructs a <code>BaseFileChooser</code> using the given path.
   * Passing in a <code>null</code>
   * string causes the file chooser to point to the user's default directory.
   * This default depends on the operating system. It is
   * typically the "My Documents" folder on Windows, and the user's
   * home directory on Unix.
   *
   * @param currentDirectoryPath  a <code>String</code> giving the path
   *				to a file or directory
   */
  public SimpleDirectoryChooser(String currentDirectoryPath) {
    this(new File(currentDirectoryPath).getAbsoluteFile());
  }

  /**
   * Constructs a <code>BaseFileChooser</code> using the given <code>File</code>
   * as the path. Passing in a <code>null</code> file
   * causes the file chooser to point to the user's default directory.
   * This default depends on the operating system. It is
   * typically the "My Documents" folder on Windows, and the user's
   * home directory on Unix.
   *
   * @param currentDirectory  a <code>File</code> object specifying
   *				the path to a file or directory
   */
  public SimpleDirectoryChooser(File currentDirectory) {
    super();
    initMembers();
    initWidgets();
    m_PanelDirs.setCurrentDirectory(currentDirectory);
  }

  /**
   * Initializes the members.
   */
  protected void initMembers() {
    m_DialogType  = JFileChooser.OPEN_DIALOG;
    m_ReturnValue = JFileChooser.ERROR_OPTION;
    m_DialogTitle = "Select directory";
    m_View        = FileSystemView.getFileSystemView();
  }

  /**
   * Initializes the widgets.
   */
  protected void initWidgets() {
    JPanel 	panelDir;

    setLayout(new BorderLayout());

    m_PanelContent = new JPanel(new BorderLayout());
    m_PanelContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

    m_PanelDirs     = new SimpleDirectoryChooserPanel();
    m_ButtonApprove = new JButton("Open");
    m_ButtonApprove.addActionListener((ActionEvent e) -> approveSelection());
    m_ButtonCancel  = new JButton("Cancel");
    m_ButtonCancel.addActionListener((ActionEvent e) -> cancelSelection());
    m_Accessory     = null;
    m_PanelContent.add(m_PanelDirs, BorderLayout.CENTER);

    panelDir = new JPanel(new BorderLayout(5, 0));
    panelDir.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    m_PanelContent.add(panelDir, BorderLayout.SOUTH);
    m_TextDirectory = new JTextField(20);
    m_TextDirectory.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
	super.keyPressed(e);
	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	  File dir = new File(m_TextDirectory.getText());
	  if (dir.exists() && dir.isDirectory())
	    setCurrentDirectory(dir);
	}
      }
    });
    panelDir.add(m_TextDirectory, BorderLayout.CENTER);
    m_LabelDirectory = new JLabel("Directory");
    m_LabelDirectory.setDisplayedMnemonic('D');
    m_LabelDirectory.setLabelFor(m_TextDirectory);
    panelDir.add(m_LabelDirectory, BorderLayout.WEST);

    m_PanelWidgets  = new JPanel(new BorderLayout());
    m_PanelWidgets.add(m_PanelContent, BorderLayout.CENTER);
    add(m_PanelWidgets, BorderLayout.CENTER);

    m_PanelButtons  = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    m_PanelButtons.add(m_ButtonApprove);
    m_PanelButtons.add(m_ButtonCancel);
    m_PanelWidgets.add(m_PanelButtons, BorderLayout.SOUTH);

    m_Toolbar         = new JToolBar();
    m_Toolbar.setFloatable(false);
    m_ButtonHome      = new JButton(m_PanelDirs.getIconManager().getHomeIcon());
    m_ButtonHome.addActionListener((ActionEvent e) -> goHome());
    m_Toolbar.add(m_ButtonHome);
    m_ButtonNewFolder = new JButton(m_PanelDirs.getIconManager().getNewFolderIcon());
    m_ButtonNewFolder.addActionListener((ActionEvent e) -> newFolder());
    m_Toolbar.add(m_ButtonNewFolder);
    m_ButtonRefresh = new JButton(m_PanelDirs.getIconManager().getRefreshIcon());
    m_ButtonRefresh.addActionListener((ActionEvent e) -> refresh());
    m_Toolbar.add(m_ButtonRefresh);
    m_PanelWidgets.add(m_Toolbar, BorderLayout.NORTH);

    addChangeListener(this);
  }

  /**
   * Update the state of the buttons.
   */
  protected void updateButtons() {
    m_ButtonApprove.setEnabled((getCurrentDirectory() != null));
  }

  /**
   * Sets the current directory to the user's home directory.
   */
  protected void goHome() {
    setCurrentDirectory(new File(System.getProperty("user.home")));
    m_PanelDirs.requestFocusInWindow();
  }

  /**
   * Let's the user create a new folder.
   */
  protected void newFolder() {
    m_PanelDirs.newFolder(true);
  }

  /**
   * Refreshes the view.
   */
  protected void refresh() {
    m_PanelDirs.refresh();
    m_PanelDirs.requestFocusInWindow();
  }

  /**
   * Sets the <code>dragEnabled</code> property,
   * which must be <code>true</code> to enable
   * automatic drag handling (the first part of drag and drop)
   * on this component.
   * The <code>transferHandler</code> property needs to be set
   * to a non-<code>null</code> value for the drag to do
   * anything.  The default value of the <code>dragEnabled</code>
   * property
   * is <code>false</code>.
   *
   * <p>
   *
   * When automatic drag handling is enabled,
   * most look and feels begin a drag-and-drop operation
   * whenever the user presses the mouse button over an item
   * and then moves the mouse a few pixels.
   * Setting this property to <code>true</code>
   * can therefore have a subtle effect on
   * how selections behave.
   *
   * <p>
   *
   * Some look and feels might not support automatic drag and drop;
   * they will ignore this property.  You can work around such
   * look and feels by modifying the component
   * to directly call the <code>exportAsDrag</code> method of a
   * <code>TransferHandler</code>.
   *
   * @param b the value to set the <code>dragEnabled</code> property to
   * @exception HeadlessException if
   *            <code>b</code> is <code>true</code> and
   *            <code>GraphicsEnvironment.isHeadless()</code>
   *            returns <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   * @see #getDragEnabled
   * @see #setTransferHandler
   * @see TransferHandler
   * @since 1.4
   */
  public void setDragEnabled(boolean b) {
  }

  /**
   * Gets the value of the <code>dragEnabled</code> property.
   *
   * @return  the value of the <code>dragEnabled</code> property
   * @see #setDragEnabled
   * @since 1.4
   */
  public boolean getDragEnabled() {
    return false;
  }

  // *****************************
  // ****** File Operations ******
  // *****************************

  /**
   * Returns the selected file. This can be set either by the
   * programmer via <code>setSelectedFile</code> or by a user action, such as
   * either typing the filename into the UI or selecting the
   * file from a list in the UI.
   *
   * @see #setSelectedFile
   * @return the selected file
   */
  public File getSelectedFile() {
    return getCurrentDirectory();
  }

  /**
   * Sets the selected file. If the file's parent directory is
   * not the current directory, changes the current directory
   * to be the file's parent directory.
   *
   * @see #getSelectedFile
   *
   * @param file the selected file
   */
  public void setSelectedFile(File file) {
    m_PanelDirs.setCurrentDirectory(file);
  }

  /**
   * Returns a list of selected files if the file chooser is
   * set to allow multiple selection.
   *
   * @return an array of selected {@code File}s
   */
  public File[] getSelectedFiles() {
    if (getCurrentDirectory() == null)
      return new File[0];
    else
      return new File[]{getCurrentDirectory()};
  }

  /**
   * Sets the list of selected files if the file chooser is
   * set to allow multiple selection.
   *
   * @param selectedFiles an array {@code File}s to be selected
   */
  public void setSelectedFiles(File[] selectedFiles) {
    if (selectedFiles.length == 1)
      setSelectedFile(selectedFiles[0]);
  }

  /**
   * Returns the current directory.
   *
   * @return the current directory
   * @see #setCurrentDirectory
   */
  public File getCurrentDirectory() {
    return m_PanelDirs.getCurrentDirectory();
  }

  /**
   * Sets the current directory. Passing in <code>null</code> sets the
   * file chooser to point to the user's default directory.
   * This default depends on the operating system. It is
   * typically the "My Documents" folder on Windows, and the user's
   * home directory on Unix.
   *
   * If the file passed in as <code>currentDirectory</code> is not a
   * directory, the parent of the file will be used as the currentDirectory.
   * If the parent is not traversable, then it will walk up the parent tree
   * until it finds a traversable directory, or hits the root of the
   * file system.
   *
   * @param dir the current directory to point to
   * @see #getCurrentDirectory
   */
  public void setCurrentDirectory(File dir) {
    m_PanelDirs.setCurrentDirectory(dir);
  }

  /**
   * Changes the directory to be set to the parent of the
   * current directory.
   *
   * @see #getCurrentDirectory
   */
  public void changeToParentDirectory() {
    File	parent;

    parent = getCurrentDirectory().getParentFile();
    if (parent != null)
      setCurrentDirectory(parent);
  }

  /**
   * Tells the UI to rescan its files list from the current directory.
   */
  public void rescanCurrentDirectory() {
    m_PanelDirs.rescanCurrentDirectory();
  }

  /**
   * Makes sure that the specified file is viewable, and
   * not hidden.
   *
   * @param f  a File object
   */
  public void ensureFileIsVisible(File f) {
    m_PanelDirs.ensureFileIsVisible(f);
  }

  // **************************************
  // ***** JFileChooser Dialog methods *****
  // **************************************

  /**
   * Pops up an "Open File" file chooser dialog. Note that the
   * text that appears in the approve button is determined by
   * the L&amp;F.
   *
   * @param    parent  the parent component of the dialog,
   *                  can be <code>null</code>;
   *                  see <code>showDialog</code> for details
   * @return   the return state of the file chooser on popdown:
   * <ul>
   * <li>JFileChooser.CANCEL_OPTION
   * <li>JFileChooser.APPROVE_OPTION
   * <li>JFileChooser.ERROR_OPTION if an error occurs or the
   *                  dialog is dismissed
   * </ul>
   * @exception HeadlessException if GraphicsEnvironment.isHeadless()
   * returns true.
   * @see java.awt.GraphicsEnvironment#isHeadless
   * @see #showDialog
   */
  public int showOpenDialog(Component parent) throws HeadlessException {
    setDialogType(JFileChooser.OPEN_DIALOG);
    return showDialog(parent, null);
  }

  /**
   * Pops up a "Save File" file chooser dialog. Note that the
   * text that appears in the approve button is determined by
   * the L&amp;F.
   *
   * @param    parent  the parent component of the dialog,
   *                  can be <code>null</code>;
   *                  see <code>showDialog</code> for details
   * @return   the return state of the file chooser on popdown:
   * <ul>
   * <li>JFileChooser.CANCEL_OPTION
   * <li>JFileChooser.APPROVE_OPTION
   * <li>JFileChooser.ERROR_OPTION if an error occurs or the
   *                  dialog is dismissed
   * </ul>
   * @exception HeadlessException if GraphicsEnvironment.isHeadless()
   * returns true.
   * @see java.awt.GraphicsEnvironment#isHeadless
   * @see #showDialog
   */
  public int showSaveDialog(Component parent) throws HeadlessException {
    setDialogType(JFileChooser.SAVE_DIALOG);
    return showDialog(parent, null);
  }

  /**
   * Creates the dialog.
   *
   * @param parent	the parent to use
   */
  protected JDialog createDialog(Component parent) {
    JDialog	result;

    Window window = GUIHelper.getParentWindow(parent);
    if (window instanceof Frame)
      result = new JDialog((Frame) window, m_DialogTitle, true);
    else
      result = new JDialog((Dialog) window, m_DialogTitle, true);

    result.getContentPane().setLayout(new BorderLayout());
    result.getContentPane().add(m_PanelWidgets);
    result.pack();
    result.setLocationRelativeTo(parent);
    m_PanelDirs.requestFocusInWindow();

    return result;
  }

  /**
   * Pops a custom file chooser dialog with a custom approve button.
   * For example, the following code
   * pops up a file chooser with a "Run Application" button
   * (instead of the normal "Save" or "Open" button):
   * <pre>
   * filechooser.showDialog(parentFrame, "Run Application");
   * </pre>
   *
   * Alternatively, the following code does the same thing:
   * <pre>
   *    JFileChooser chooser = new JFileChooser(null);
   *    chooser.setApproveButtonText("Run Application");
   *    chooser.showDialog(parentFrame, null);
   * </pre>
   *
   * <p>
   *
   * The <code>parent</code> argument determines two things:
   * the frame on which the open dialog depends and
   * the component whose position the look and feel
   * should consider when placing the dialog.  If the parent
   * is a <code>Frame</code> object (such as a <code>JFrame</code>)
   * then the dialog depends on the frame and
   * the look and feel positions the dialog
   * relative to the frame (for example, centered over the frame).
   * If the parent is a component, then the dialog
   * depends on the frame containing the component,
   * and is positioned relative to the component
   * (for example, centered over the component).
   * If the parent is <code>null</code>, then the dialog depends on
   * no visible window, and it's placed in a
   * look-and-feel-dependent position
   * such as the center of the screen.
   *
   * @param   parent  the parent component of the dialog;
   *                  can be <code>null</code>
   * @param   approveButtonText the text of the <code>ApproveButton</code>
   * @return  the return state of the file chooser on popdown:
   * <ul>
   * <li>JFileChooser.CANCEL_OPTION
   * <li>JFileChooser.APPROVE_OPTION
   * <li>JFileChooser.ERROR_OPTION if an error occurs or the
   *                  dialog is dismissed
   * </ul>
   * @exception HeadlessException if GraphicsEnvironment.isHeadless()
   * returns true.
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  @SuppressWarnings("deprecation")
  public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
    // Prevent to show second instance of dialog if the previous one still exists
    if (m_Dialog != null)
      return JFileChooser.ERROR_OPTION;

    if(approveButtonText != null) {
      setApproveButtonText(approveButtonText);
      setDialogType(JFileChooser.CUSTOM_DIALOG);
    }
    m_Dialog = createDialog(parent);
    m_Dialog.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
	m_ReturnValue = JFileChooser.CANCEL_OPTION;
      }
    });
    m_ReturnValue = JFileChooser.ERROR_OPTION;
    rescanCurrentDirectory();

    m_Dialog.show();
    firePropertyChange("JFileChooserDialogIsClosingProperty", m_Dialog, null);

    // Remove all components from dialog. The MetalFileChooserUI.installUI() method (and other LAFs)
    // registers AWT listener for dialogs and produces memory leaks. It happens when
    // installUI invoked after the showDialog method.
    m_Dialog.getContentPane().removeAll();
    m_Dialog.dispose();
    m_Dialog = null;
    return m_ReturnValue;
  }

  // **************************
  // ***** Dialog Options *****
  // **************************

  /**
   * Returns the value of the <code>controlButtonsAreShown</code>
   * property.
   *
   * @return   the value of the <code>controlButtonsAreShown</code>
   *     property
   *
   * @see #setControlButtonsAreShown
   * @since 1.3
   */
  public boolean getControlButtonsAreShown() {
    return m_Toolbar.isVisible();
  }


  /**
   * Sets the property
   * that indicates whether the <i>approve</i> and <i>cancel</i>
   * buttons are shown in the file chooser.  This property
   * is <code>true</code> by default.  Look and feels
   * that always show these buttons will ignore the value
   * of this property.
   * This method fires a property-changed event,
   * using the string value of
   * <code>CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY</code>
   * as the name of the property.
   *
   * @param b <code>false</code> if control buttons should not be
   *    shown; otherwise, <code>true</code>
   *
   * @see #getControlButtonsAreShown
   * @see JFileChooser#CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY
   * @since 1.3
   */
  public void setControlButtonsAreShown(boolean b) {
    m_Toolbar.setVisible(b);
  }

  /**
   * Returns the type of this dialog.  The default is
   * <code>JFileChooser.OPEN_DIALOG</code>.
   *
   * @return   the type of dialog to be displayed:
   * <ul>
   * <li>JFileChooser.OPEN_DIALOG
   * <li>JFileChooser.SAVE_DIALOG
   * <li>JFileChooser.CUSTOM_DIALOG
   * </ul>
   *
   * @see #setDialogType
   */
  public int getDialogType() {
    return m_DialogType;
  }

  /**
   * Sets the type of this dialog. Use <code>OPEN_DIALOG</code> when you
   * want to bring up a file chooser that the user can use to open a file.
   * Likewise, use <code>SAVE_DIALOG</code> for letting the user choose
   * a file for saving.
   * Use <code>CUSTOM_DIALOG</code> when you want to use the file
   * chooser in a context other than "Open" or "Save".
   * For instance, you might want to bring up a file chooser that allows
   * the user to choose a file to execute. Note that you normally would not
   * need to set the <code>JFileChooser</code> to use
   * <code>CUSTOM_DIALOG</code>
   * since a call to <code>setApproveButtonText</code> does this for you.
   * The default dialog type is <code>JFileChooser.OPEN_DIALOG</code>.
   *
   * @param dialogType the type of dialog to be displayed:
   * <ul>
   * <li>JFileChooser.OPEN_DIALOG
   * <li>JFileChooser.SAVE_DIALOG
   * <li>JFileChooser.CUSTOM_DIALOG
   * </ul>
   *
   * @exception IllegalArgumentException if <code>dialogType</code> is
   *                          not legal
   *
   * @see #getDialogType
   * @see #setApproveButtonText
   */
  public void setDialogType(int dialogType) {
    m_DialogType = dialogType;
  }

  /**
   * Sets the string that goes in the <code>JFileChooser</code> window's
   * title bar.
   *
   * @param dialogTitle the new <code>String</code> for the title bar
   *
   * @see #getDialogTitle
   *
   */
  public void setDialogTitle(String dialogTitle) {
    m_DialogTitle = dialogTitle;
  }

  /**
   * Gets the string that goes in the <code>JFileChooser</code>'s titlebar.
   *
   * @return the string from the {@code JFileChooser} window's title bar
   * @see #setDialogTitle
   */
  public String getDialogTitle() {
    return m_DialogTitle;
  }

  // ************************************
  // ***** JFileChooser View Options *****
  // ************************************



  /**
   * Sets the tooltip text used in the <code>ApproveButton</code>.
   * If <code>null</code>, the UI object will determine the button's text.
   *
   * @param toolTipText the tooltip text for the approve button
   * @see #setApproveButtonText
   * @see #setDialogType
   * @see #showDialog
   */
  public void setApproveButtonToolTipText(String toolTipText) {
    m_ButtonApprove.setToolTipText(toolTipText);
  }


  /**
   * Returns the tooltip text used in the <code>ApproveButton</code>.
   * If <code>null</code>, the UI object will determine the button's text.
   *
   * @return the tooltip text used for the approve button
   *
   * @see #setApproveButtonText
   * @see #setDialogType
   * @see #showDialog
   */
  public String getApproveButtonToolTipText() {
    return m_ButtonApprove.getToolTipText();
  }

  /**
   * Returns the approve button's mnemonic.
   * @return an integer value for the mnemonic key
   *
   * @see #setApproveButtonMnemonic
   */
  public int getApproveButtonMnemonic() {
    return m_ButtonApprove.getMnemonic();
  }

  /**
   * Sets the approve button's mnemonic using a numeric keycode.
   *
   * @param mnemonic  an integer value for the mnemonic key
   *
   * @see #getApproveButtonMnemonic
   */
  public void setApproveButtonMnemonic(int mnemonic) {
    m_ButtonApprove.setMnemonic(mnemonic);
  }

  /**
   * Sets the approve button's mnemonic using a character.
   * @param mnemonic  a character value for the mnemonic key
   *
   * @see #getApproveButtonMnemonic
   */
  public void setApproveButtonMnemonic(char mnemonic) {
    m_ButtonApprove.setMnemonic(mnemonic);
  }


  /**
   * Sets the text used in the <code>ApproveButton</code> in the
   * <code>FileChooserUI</code>.
   *
   * @param approveButtonText the text used in the <code>ApproveButton</code>
   *
   * @see #getApproveButtonText
   * @see #setDialogType
   * @see #showDialog
   */
  public void setApproveButtonText(String approveButtonText) {
    m_ButtonApprove.setText(approveButtonText);
  }

  /**
   * Returns the text used in the <code>ApproveButton</code> in the
   * <code>FileChooserUI</code>.
   * If <code>null</code>, the UI object will determine the button's text.
   *
   * Typically, this would be "Open" or "Save".
   *
   * @return the text used in the <code>ApproveButton</code>
   *
   * @see #setApproveButtonText
   * @see #setDialogType
   * @see #showDialog
   */
  public String getApproveButtonText() {
    return m_ButtonApprove.getText();
  }

  /**
   * Gets the list of user choosable file filters.
   *
   * @return a <code>FileFilter</code> array containing all the choosable
   *         file filters
   *
   * @see #addChoosableFileFilter
   * @see #removeChoosableFileFilter
   * @see #resetChoosableFileFilters
   */
  public FileFilter[] getChoosableFileFilters() {
    return new FileFilter[0];
  }

  /**
   * Adds a filter to the list of user choosable file filters.
   * For information on setting the file selection mode, see
   * {@link #setFileSelectionMode setFileSelectionMode}.
   *
   * @param filter the <code>FileFilter</code> to add to the choosable file
   *               filter list
   *
   * @see #getChoosableFileFilters
   * @see #removeChoosableFileFilter
   * @see #resetChoosableFileFilters
   * @see #setFileSelectionMode
   */
  public void addChoosableFileFilter(FileFilter filter) {
    // ignored
  }

  /**
   * Removes a filter from the list of user choosable file filters. Returns
   * true if the file filter was removed.
   *
   * @param f the file filter to be removed
   * @return true if the file filter was removed, false otherwise
   * @see #addChoosableFileFilter
   * @see #getChoosableFileFilters
   * @see #resetChoosableFileFilters
   */
  public boolean removeChoosableFileFilter(FileFilter f) {
    // ignored
    return true;
  }

  /**
   * Resets the choosable file filter list to its starting state. Normally,
   * this removes all added file filters while leaving the
   * <code>AcceptAll</code> file filter.
   *
   * @see #addChoosableFileFilter
   * @see #getChoosableFileFilters
   * @see #removeChoosableFileFilter
   */
  public void resetChoosableFileFilters() {
  }

  /**
   * Returns the <code>AcceptAll</code> file filter.
   * For example, on Microsoft Windows this would be All Files (*.*).
   *
   * @return the {@code AcceptAll} file filter
   */
  public FileFilter getAcceptAllFileFilter() {
    return null;
  }

  /**
   * Returns whether the <code>AcceptAll FileFilter</code> is used.
   * @return true if the <code>AcceptAll FileFilter</code> is used
   * @see #setAcceptAllFileFilterUsed
   * @since 1.3
   */
  public boolean isAcceptAllFileFilterUsed() {
    return false;
  }

  /**
   * Determines whether the <code>AcceptAll FileFilter</code> is used
   * as an available choice in the choosable filter list.
   * If false, the <code>AcceptAll</code> file filter is removed from
   * the list of available file filters.
   * If true, the <code>AcceptAll</code> file filter will become the
   * actively used file filter.
   *
   * @param b a {@code boolean} which determines whether the {@code AcceptAll}
   *          file filter is an available choice in the choosable filter list
   *
   * @see #isAcceptAllFileFilterUsed
   * @see #getAcceptAllFileFilter
   * @see #setFileFilter
   * @since 1.3
   */
  public void setAcceptAllFileFilterUsed(boolean b) {
    // ignored
  }

  /**
   * Returns the accessory component.
   *
   * @return this JFileChooser's accessory component, or null
   * @see #setAccessory
   */
  public JComponent getAccessory() {
    return m_Accessory;
  }

  /**
   * Sets the accessory component. An accessory is often used to show a
   * preview image of the selected file; however, it can be used for anything
   * that the programmer wishes, such as extra custom file chooser controls.
   *
   * <p>
   * Note: if there was a previous accessory, you should unregister
   * any listeners that the accessory might have registered with the
   * file chooser.
   *
   * @param newAccessory the accessory component to be set
   */
  public void setAccessory(JComponent newAccessory) {
    if (m_Accessory != null)
      m_PanelContent.remove(m_Accessory);
    m_Accessory = newAccessory;
    m_PanelContent.add(m_Accessory, BorderLayout.EAST);
  }

  /**
   * Sets the <code>JFileChooser</code> to allow the user to just
   * select files, just select
   * directories, or select both files and directories.  The default is
   * <code>JFilesChooser.FILES_ONLY</code>.
   *
   * @param mode the type of files to be displayed:
   * <ul>
   * <li>JFileChooser.FILES_ONLY
   * <li>JFileChooser.DIRECTORIES_ONLY
   * <li>JFileChooser.FILES_AND_DIRECTORIES
   * </ul>
   *
   * @exception IllegalArgumentException  if <code>mode</code> is an
   *                          illegal file selection mode
   *
   * @see #getFileSelectionMode
   */
  public void setFileSelectionMode(int mode) {
    // ignored
  }

  /**
   * Returns the current file-selection mode.  The default is
   * <code>JFilesChooser.FILES_ONLY</code>.
   *
   * @return the type of files to be displayed, one of the following:
   * <ul>
   * <li>JFileChooser.FILES_ONLY
   * <li>JFileChooser.DIRECTORIES_ONLY
   * <li>JFileChooser.FILES_AND_DIRECTORIES
   * </ul>
   * @see #setFileSelectionMode
   */
  public int getFileSelectionMode() {
    return JFileChooser.DIRECTORIES_ONLY;
  }

  /**
   * Convenience call that determines if files are selectable based on the
   * current file selection mode.
   *
   * @return true if files are selectable, false otherwise
   * @see #setFileSelectionMode
   * @see #getFileSelectionMode
   */
  public boolean isFileSelectionEnabled() {
    return false;
  }

  /**
   * Convenience call that determines if directories are selectable based
   * on the current file selection mode.
   *
   * @return true if directories are selectable, false otherwise
   * @see #setFileSelectionMode
   * @see #getFileSelectionMode
   */
  public boolean isDirectorySelectionEnabled() {
    return true;
  }

  /**
   * Sets the file chooser to allow multiple file selections.
   *
   * @param b true if multiple files may be selected
   *
   * @see #isMultiSelectionEnabled
   */
  public void setMultiSelectionEnabled(boolean b) {
    // ignored
  }

  /**
   * Returns true if multiple files can be selected.
   * @return true if multiple files can be selected
   * @see #setMultiSelectionEnabled
   */
  public boolean isMultiSelectionEnabled() {
    return false;
  }


  /**
   * Returns true if hidden files are not shown in the file chooser;
   * otherwise, returns false.
   *
   * @return the status of the file hiding property
   * @see #setFileHidingEnabled
   */
  public boolean isFileHidingEnabled() {
    return !m_PanelDirs.getShowHidden();
  }

  /**
   * Sets file hiding on or off. If true, hidden files are not shown
   * in the file chooser. The job of determining which files are
   * shown is done by the <code>FileView</code>.
   *
   * @param b the boolean value that determines whether file hiding is
   *          turned on
   * @see #isFileHidingEnabled
   */
  public void setFileHidingEnabled(boolean b) {
    m_PanelDirs.setShowHidden(!b);
  }

  /**
   * Sets the current file filter. The file filter is used by the
   * file chooser to filter out files from the user's view.
   *
   * @param filter the new current file filter to use
   * @see #getFileFilter
   */
  public void setFileFilter(FileFilter filter) {
  }


  /**
   * Returns the currently selected file filter.
   *
   * @return the current file filter
   * @see #setFileFilter
   * @see #addChoosableFileFilter
   */
  public FileFilter getFileFilter() {
    return null;
  }

  /**
   * Sets the file view to be used to retrieve UI information, such as
   * the icon that represents a file or the type description of a file.
   *
   * @param fileView a {@code FileView} to be used to retrieve UI information
   *
   * @see #getFileView
   */
  public void setFileView(FileView fileView) {
    // ignored
  }

  /**
   * Returns the current file view.
   *
   * @return the current file view
   * @see #setFileView
   */
  public FileView getFileView() {
    return null;
  }

  // ******************************
  // *****FileView delegation *****
  // ******************************

  // NOTE: all of the following methods attempt to delegate
  // first to the client set fileView, and if <code>null</code> is returned
  // (or there is now client defined fileView) then calls the
  // UI's default fileView.

  /**
   * Returns the filename.
   * @param f the <code>File</code>
   * @return the <code>String</code> containing the filename for
   *          <code>f</code>
   * @see FileView#getName
   */
  public String getName(File f) {
    return m_View.getSystemDisplayName(f);
  }

  /**
   * Returns the file description.
   * @param f the <code>File</code>
   * @return the <code>String</code> containing the file description for
   *          <code>f</code>
   * @see FileView#getDescription
   */
  public String getDescription(File f) {
    return m_View.getSystemTypeDescription(f);
  }

  /**
   * Returns the file type.
   * @param f the <code>File</code>
   * @return the <code>String</code> containing the file type description for
   *          <code>f</code>
   * @see FileView#getTypeDescription
   */
  public String getTypeDescription(File f) {
    return m_View.getSystemTypeDescription(f);
  }

  /**
   * Returns the icon for this file or type of file, depending
   * on the system.
   * @param f the <code>File</code>
   * @return the <code>Icon</code> for this file, or type of file
   * @see FileView#getIcon
   */
  public Icon getIcon(File f) {
    return m_View.getSystemIcon(f);
  }

  /**
   * Returns true if the file (directory) can be visited.
   * Returns false if the directory cannot be traversed.
   * @param f the <code>File</code>
   * @return true if the file/directory can be traversed, otherwise false
   * @see FileView#isTraversable
   */
  public boolean isTraversable(File f) {
    return m_View.isTraversable(f);
  }

  /**
   * Returns true if the file should be displayed.
   * @param f the <code>File</code>
   * @return true if the file should be displayed, otherwise false
   * @see FileFilter#accept
   */
  public boolean accept(File f) {
    boolean	result;

    result = f.isDirectory();
    if (f.isHidden() && !m_PanelDirs.getShowHidden())
      result = false;

    return result;
  }

  /**
   * Sets the file system view that the <code>JFileChooser</code> uses for
   * accessing and creating file system resources, such as finding
   * the floppy drive and getting a list of root drives.
   * @param fsv  the new <code>FileSystemView</code>
   *
   * @see FileSystemView
   */
  public void setFileSystemView(FileSystemView fsv) {
    m_View = fsv;
    m_PanelDirs.setView(fsv);
  }

  /**
   * Returns the file system view.
   * @return the <code>FileSystemView</code> object
   * @see #setFileSystemView
   */
  public FileSystemView getFileSystemView() {
    return m_View;
  }

  // **************************
  // ***** Event Handling *****
  // **************************

  /**
   * Called by the UI when the user hits the Approve button
   * (labeled "Open" or "Save", by default). This can also be
   * called by the programmer.
   * This method causes an action event to fire
   * with the command string equal to
   * <code>APPROVE_SELECTION</code>.
   *
   * @see JFileChooser#APPROVE_SELECTION
   */
  public void approveSelection() {
    m_ReturnValue = JFileChooser.APPROVE_OPTION;
    if (m_Dialog != null)
      m_Dialog.setVisible(false);
    fireActionPerformed(JFileChooser.APPROVE_SELECTION);
  }

  /**
   * Called by the UI when the user chooses the Cancel button.
   * This can also be called by the programmer.
   * This method causes an action event to fire
   * with the command string equal to
   * <code>CANCEL_SELECTION</code>.
   *
   * @see JFileChooser#CANCEL_SELECTION
   */
  public void cancelSelection() {
    m_ReturnValue = JFileChooser.CANCEL_OPTION;
    if (m_Dialog != null)
      m_Dialog.setVisible(false);
    fireActionPerformed(JFileChooser.CANCEL_SELECTION);
  }

  /**
   * Adds an <code>ActionListener</code> to the file chooser.
   *
   * @param l  the listener to be added
   *
   * @see #approveSelection
   * @see #cancelSelection
   */
  public void addActionListener(ActionListener l) {
    listenerList.add(ActionListener.class, l);
  }

  /**
   * Removes an <code>ActionListener</code> from the file chooser.
   *
   * @param l  the listener to be removed
   *
   * @see #addActionListener
   */
  public void removeActionListener(ActionListener l) {
    listenerList.remove(ActionListener.class, l);
  }

  /**
   * Returns an array of all the action listeners
   * registered on this file chooser.
   *
   * @return all of this file chooser's <code>ActionListener</code>s
   *         or an empty
   *         array if no action listeners are currently registered
   *
   * @see #addActionListener
   * @see #removeActionListener
   *
   * @since 1.4
   */
  public ActionListener[] getActionListeners() {
    return listenerList.getListeners(ActionListener.class);
  }

  /**
   * Notifies all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the <code>command</code> parameter.
   *
   * @see EventListenerList
   */
  protected void fireActionPerformed(String command) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    long mostRecentEventTime = EventQueue.getMostRecentEventTime();
    int modifiers = 0;
    AWTEvent currentEvent = EventQueue.getCurrentEvent();
    if (currentEvent instanceof InputEvent) {
      modifiers = ((InputEvent)currentEvent).getModifiers();
    } else if (currentEvent instanceof ActionEvent) {
      modifiers = ((ActionEvent)currentEvent).getModifiers();
    }
    ActionEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==ActionListener.class) {
	// Lazily create the event:
	if (e == null) {
	  e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
	      command, mostRecentEventTime,
	      modifiers);
	}
	((ActionListener)listeners[i+1]).actionPerformed(e);
      }
    }
  }

  /**
   * Returns whether hidden directories are shown.
   *
   * @return		true if shown
   */
  public boolean getShowHidden() {
    return m_PanelDirs.getShowHidden();
  }

  /**
   * Sets whether hidden dirs are shown.
   *
   * @param value	true if to show hidden dirs
   */
  public void setShowHidden(boolean value) {
    m_PanelDirs.setShowHidden(value);
  }

  /**
   * Sets the icon manager to use.
   *
   * @param value	the manager
   */
  public void setIconManager(IconManager value) {
    m_ButtonHome.setIcon(value.getHomeIcon());
    m_ButtonNewFolder.setIcon(value.getNewFolderIcon());
    m_PanelDirs.setIconManager(value);
  }

  /**
   * Returns the icon manager in use.
   *
   * @return		the manager
   */
  public IconManager getIconManager() {
    return m_PanelDirs.getIconManager();
  }

  /**
   * Adds the listener for changes in the selected directory.
   *
   * @param l		the listener to add
   */
  public void addChangeListener(DirectoryChangeListener l) {
    m_PanelDirs.addChangeListener(l);
  }

  /**
   * Removes the listener for changes in the selected directory.
   *
   * @param l		the listener to remove
   */
  public void removeChangeListener(DirectoryChangeListener l) {
    m_PanelDirs.removeChangeListener(l);
  }

  /**
   * Gets called when the current directory changes.
   *
   * @param e		the event
   */
  public void directoryChanged(DirectoryChangeEvent e) {
    firePropertyChange(JFileChooser.DIRECTORY_CHANGED_PROPERTY, OS.fileToString(m_PanelDirs.getLastDirectory()), OS.fileToString(m_PanelDirs.getCurrentDirectory()));
    if (getCurrentDirectory() == null)
      m_TextDirectory.setText("");
    else
      m_TextDirectory.setText(getCurrentDirectory().getAbsolutePath());
    updateButtons();
  }

  /**
   * Sets the customizer for the popup menu.
   *
   * @param value	the customizer, null to remove
   */
  public void setPopupMenuCustomizer(DirectoryTreePopupMenuCustomizer value) {
    m_PanelDirs.setPopupMenuCustomizer(value);
  }

  /**
   * Returns the customizer for the popup menu.
   *
   * @return		the customizer, null if none used
   */
  public DirectoryTreePopupMenuCustomizer getPopupMenuCustomizer() {
    return m_PanelDirs.getPopupMenuCustomizer();
  }

  /**
   * Sets whether the popup menu is enabled.
   *
   * @param value	true to enable
   */
  public void setPopupMenuEnabled(boolean value) {
    m_PanelDirs.setPopupMenuEnabled(value);
  }

  /**
   * Returns whether the popup menu is enabled.
   *
   * @return		true if enabled
   */
  public boolean isPopupMenuEnabled() {
    return m_PanelDirs.isPopupMenuEnabled();
  }
}
