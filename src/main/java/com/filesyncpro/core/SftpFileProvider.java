package main.java.com.filesyncpro.core;

import main.java.com.filesyncpro.model.FileMetadata;
import main.java.com.filesyncpro.util.HashUtil;
import main.java.com.filesyncpro.util.StreamUtil;
import net.schmizz.sshj.sftp.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SftpFileProvider implements FileProvider {

    private final SFTPClient sftp;

    public SftpFileProvider(SFTPClient sftp) {
        this.sftp = sftp;
    }

    @Override
    public Map<String, FileMetadata> scan(String root) throws Exception {
        Map<String, FileMetadata> files = new HashMap<>();
        scanRecursive(root, files);
        return files;
    }

    private void scanRecursive(String dir, Map<String, FileMetadata> out) throws Exception {
        List<RemoteResourceInfo> list = sftp.ls(dir);
        for (RemoteResourceInfo info : list) {
            String path = info.getParent();
            boolean isDir = info.isDirectory();
            long modified = info.getAttributes().getMtime() * 1000L;
            String hash = null;

            if (!isDir) {
                byte[] data = readFile(path);
                hash = HashUtil.hashBytes(data, "SHA-256");
            }

            out.put(path, new FileMetadata(Paths.get(path), modified, hash, isDir));

            if (isDir) {
                scanRecursive(path, out);
            }
        }

    }

    public byte[] readFile(String path) throws Exception {
        try (RemoteFile rf = sftp.open(path);
                InputStream is = rf.new RemoteFileInputStream()) {
            return StreamUtil.toByteArray(is);
        }
    }

    @Override
    public boolean exists(String path) {
        try {
            sftp.stat(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public InputStream read(String path) throws Exception {
        RemoteFile rf = sftp.open(path);
        return rf.new RemoteFileInputStream();
    }

    @Override
    public void write(String path, InputStream data) throws Exception {
        Path temp = Files.createTempFile("sftp-upload-", ".tmp");
        try {
            byte[] bytes = StreamUtil.toByteArray(data);
            Files.write(temp, bytes);
            sftp.put(temp.toString(), path);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    @Override
    public void createDirectory(String path) throws Exception {
        sftp.mkdirs(path);
    }

    @Override
    public void delete(String path) throws Exception {
        sftp.rm(path);
    }

    @Override
    public long getLastModified(String path) throws Exception {
        return sftp.stat(path).getMtime() * 1000L;
    }

    @Override
    public boolean isDirectory(String path) throws Exception {
        try {            
            FileAttributes attrs = sftp.stat(path);
            return attrs.getType() == net.schmizz.sshj.sftp.FileMode.Type.DIRECTORY;
        } catch (SFTPException e) {
            if (e.getStatusCode() == Response.StatusCode.NO_SUCH_FILE) {
                return false;
            }
            throw e;
        }

    }
}
