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
 * PanelExample.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.adams.simpledirectorychooser.examples;

import nz.ac.waikato.cms.adams.simpledirectorychooser.SimpleDirectoryChooserPanel;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeEvent;
import nz.ac.waikato.cms.adams.simpledirectorychooser.events.DirectoryChangeListener;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

/**
 * Shows how to use {@link SimpleDirectoryChooserPanel}.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class PanelExample {

  public static void main(String[] args) throws Exception {
    SimpleDirectoryChooserPanel panel = new SimpleDirectoryChooserPanel();
    if (args.length > 0)
      panel.setCurrentDirectory(new File(args[0]));
    else
      panel.setCurrentDirectory(new File(System.getProperty("user.dir")));
    panel.addChangeListener(new DirectoryChangeListener() {
      @Override
      public void directoryChanged(DirectoryChangeEvent e) {
        System.out.println("Dir changed: " + e.getCurrentDirectory());
      }
    });
    JFrame frame = new JFrame("Panel example");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.setSize(new Dimension(600, 480));
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
