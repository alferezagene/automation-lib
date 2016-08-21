package nz.co.thebteam.AutomationLibrary.Utilities;

import com.jcraft.jsch.*;

import java.io.InputStream;

/**
 * @author World
 */
public class SSHHelper {

    String user;
    String password;
    String host;
    int port;
    Session session = null;

    public SSHHelper(String user, String password, String host, int port) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
    }


    private void createSession() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public InputStream getFile(String remoteFile) {
        try {
            createSession();
            System.out.println("Crating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            InputStream out = null;
            out = sftpChannel.get(remoteFile);
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void putFile(String localFile, String remotePath) {
        try {
            createSession();
            System.out.println("Crating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            InputStream file = FileHandler.readFileAsInputStream(localFile); //read in file here
            sftpChannel.put(file, remotePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void putFile(InputStream file, String remotePath) {
        try {
            createSession();
            System.out.println("Crating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            sftpChannel.put(file, remotePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectSession() {
        session.disconnect();
    }

}