package main.java.com.filesyncpro.core;

import java.util.List;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

import java.io.IOException;
import java.security.PublicKey;

public class SftpClient {

    private final SSHClient ssh;
    private final SFTPClient sftp;

    public SftpClient(String host, int port, String username, String password) throws IOException {
        ssh = new SSHClient();
        ssh.addHostKeyVerifier(new HostKeyVerifier() {
            @Override
            public boolean verify(String hostname, int port, PublicKey key) {
                return true;
            }

            @Override
            public List<String> findExistingAlgorithms(String hostname, int port) {
                return null;
            }
        });
        ssh.connect(host, port);
        ssh.authPassword(username, password);
        sftp = ssh.newSFTPClient();
    }

    public SFTPClient getSftp(){
        return sftp;
    }

    public void close() throws IOException {
        sftp.close();
        ssh.disconnect();
    }
}
