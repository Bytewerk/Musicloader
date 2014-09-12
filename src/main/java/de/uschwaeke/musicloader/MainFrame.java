/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uschwaeke.musicloader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Uwe
 */
public class MainFrame extends JFrame {

    private JFileChooser chooser;
    private JList<File> files;
    private JButton addFiles;
    private JButton sync;
    private JTextField remotedir;
    private JButton removeFiles;
    private JButton clearFiles;

    public static final String SYNC_CMD = "sync_cmd";
    public static final String REMOVE_FILE_CMD = "remove_cmd";
    public static final String CLEAR_FILES_CMD = "clear_cmd";

    public MainFrame() {
        setTitle("Musicloader for Beets");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogTitle("Select files to upload");
        chooser.setCurrentDirectory(new File(
            "/Users/Uwe/Music/iTunes/iTunes Media/Music"));
        files = new JList<>();
        JPanel tmp = new JPanel();
        tmp.setLayout(new BorderLayout());
        tmp.setSize(450, 100);
        tmp.add(files, BorderLayout.CENTER);
        addFiles = new JButton("open");
        addFiles.addActionListener((ActionListener) e -> {
            chooser.showDialog(null, "add");
        });
        removeFiles = new JButton("remove selected Files");
        removeFiles.setActionCommand(REMOVE_FILE_CMD);
        removeFiles.setEnabled(false);
        clearFiles = new JButton("clear all");
        clearFiles.setActionCommand(CLEAR_FILES_CMD);
        clearFiles.setEnabled(false);
        JPanel tmp2 = new JPanel(new GridLayout(3, 1));
        tmp2.add(addFiles);
        tmp2.add(removeFiles);
        tmp2.add(clearFiles);

        tmp.add(tmp2, BorderLayout.EAST);
        add(tmp);
        tmp = new JPanel(new FlowLayout());
        sync = new JButton("start sync");
        sync.setActionCommand(SYNC_CMD);
        sync.setEnabled(false);
        tmp.add(new JLabel("remote tmp Directory"));
        remotedir = new JTextField(10);
        remotedir.setText("tmp");
        tmp.add(remotedir);
        tmp.add(sync);
        add(tmp, BorderLayout.SOUTH);
        pack();
    }

    public JFileChooser getChooser() {
        return chooser;
    }

    public void setChooser(JFileChooser chooser) {
        this.chooser = chooser;
    }

    public JList<File> getFiles() {
        return files;
    }

    public void setFiles(JList<File> files) {
        this.files = files;
    }

    public JButton getAddFiles() {
        return addFiles;
    }

    public void setAddFiles(JButton addFiles) {
        this.addFiles = addFiles;
    }

    public JButton getSync() {
        return sync;
    }

    public void setSync(JButton sync) {
        this.sync = sync;
    }

    public JTextField getRemotedir() {
        return remotedir;
    }

    public void setRemotedir(JTextField remotedir) {
        this.remotedir = remotedir;
    }

    public JButton getRemoveFiles() {
        return removeFiles;
    }

    public void setRemoveFiles(JButton removeFiles) {
        this.removeFiles = removeFiles;
    }

    public JButton getClearFiles() {
        return clearFiles;
    }

    public void setClearFiles(JButton clearFiles) {
        this.clearFiles = clearFiles;
    }

}
