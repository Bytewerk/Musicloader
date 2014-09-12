/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uschwaeke.musicloader;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Uwe
 */
public class Controller implements ActionListener, KeyListener, ListDataListener {

    private Service service;
    private Logger logger;
    private List<File> files;
    private MainFrame gui;
    private DefaultListModel<File> listModel;

    public Controller() {
        service = new Service();
        logger = Logger.getLogger(getClass().getName());
        files = new ArrayList<File>();
        listModel = new DefaultListModel<File>();
        listModel.addListDataListener(this);
    }

    public void start(String[] args) {
        if (args != null && args.length > 1) {
            String dst = args[0];
            File tmp;
            for (int i = 1; i < args.length; i++) {
                tmp = new File(args[i]);
                if (tmp.exists()) {
                    files.add(tmp);
                }
            }
            headless(dst);
        } else {
            headfull();
        }
        files = new ArrayList<File>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(MainFrame.SYNC_CMD)) {
            gui.getSync().setEnabled(false);
            headless(gui.getRemotedir().getText());
            gui.getSync().setEnabled(true);
        } else if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
            File[] fis = gui.getChooser().getSelectedFiles();
            for (File f : fis) {
                files.add(f);
                listModel.addElement(f);
            }
        } else if (e.getActionCommand().equals(MainFrame.REMOVE_FILE_CMD)) {
            List<File> sel = gui.getFiles().getSelectedValuesList();
            files.removeAll(sel);
            listModel.removeRange(gui.getFiles().getMinSelectionIndex(),
                                  gui.getFiles().getMaxSelectionIndex());
        } else if (e.getActionCommand().equals(MainFrame.CLEAR_FILES_CMD)) {
            files.clear();
            listModel.removeAllElements();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        gui.getSync().setEnabled(!gui.getRemotedir().getText().isEmpty());
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        gui.getClearFiles().setEnabled(!listModel.isEmpty());
        gui.getRemoveFiles().setEnabled(!listModel.isEmpty());
        gui.getSync().setEnabled(!listModel.isEmpty());
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        gui.getClearFiles().setEnabled(!listModel.isEmpty());
        gui.getRemoveFiles().setEnabled(!listModel.isEmpty());
        gui.getSync().setEnabled(!listModel.isEmpty());
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        gui.getClearFiles().setEnabled(!listModel.isEmpty());
        gui.getRemoveFiles().setEnabled(!listModel.isEmpty());
        gui.getSync().setEnabled(!listModel.isEmpty());
    }

    private void headless(String dst) {
        try {
            logger.log(Level.INFO, "starting upload");
            for (File f : files) {
                service.uploadToDirectory(f, dst);
            }
            logger.log(Level.INFO, "importing uploaded music");
            service.startBeets(dst);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (JSchException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SftpException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private void headfull() {
        gui = new MainFrame();
        gui.getChooser().addActionListener(this);
        gui.getRemotedir().addKeyListener(this);
        gui.getSync().addActionListener(this);
        gui.getClearFiles().addActionListener(this);
        gui.getRemoveFiles().addActionListener(this);
        gui.getFiles().setModel(listModel);
        gui.setVisible(true);
    }
}
