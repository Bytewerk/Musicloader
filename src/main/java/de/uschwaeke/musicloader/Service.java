/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uschwaeke.musicloader;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 *
 * @author Uwe
 */
public class Service {

    private final JSch jsch;
    private static final String MUSIC_IMPORTME = "music_importme";
    private static final String USER = "beets";
    private static final String HOST = "stern.bingo";
    private static final int PORT = 22;
    private static final String PASSWORD = "beets";
    private static final String BEETS_IMPORT = "beet import -A ";
    private java.util.logging.Logger logger;

    public Service() {
        jsch = new JSch();
        logger = java.util.logging.Logger.getLogger(getClass().getName());
    }

    public void uploadToDirectory(File src, String dst) throws JSchException, SftpException, FileNotFoundException {
        Session session = jsch.getSession(USER, HOST, PORT);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(PASSWORD);
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();

        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.cd(MUSIC_IMPORTME);
        List<?> dirs = sftpChannel.ls(".");
        boolean pathAvailible = false;
        for (Object object : dirs) {
            if ((object.toString()).contains(dst)) {
                pathAvailible = true;
                break;
            }
        }
        if (!pathAvailible) {
            sftpChannel.mkdir(dst);
        }
        sftpChannel.cd(dst);
        recUploadToDirectory(src, dst, sftpChannel);
        sftpChannel.disconnect();
        session.disconnect();
    }

    private void recUploadToDirectory(File src, String dst,
                                      ChannelSftp sftpChannel) throws JSchException, SftpException, FileNotFoundException {
        if (!src.isDirectory()) {
            logger.log(Level.INFO, "uploading File " + src);
            sftpChannel.put(new FileInputStream(src), src.getName());
            return;
        }
        for (File object : src.listFiles()) {
            recUploadToDirectory(object, dst, sftpChannel);
        }
    }

    public void startBeets(String dst) throws JSchException, IOException {
        Session session = jsch.getSession(USER, HOST, PORT);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(PASSWORD);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        BufferedReader in = new BufferedReader(new InputStreamReader(channel.
            getInputStream()));
        channel.setCommand(BEETS_IMPORT + MUSIC_IMPORTME + "/" + dst + " &;");
        channel.connect();

        String msg = null;
        while ((msg = in.readLine()) != null) {
            logger.log(Level.INFO, msg);
        }

        channel.disconnect();
        session.disconnect();
    }
}
