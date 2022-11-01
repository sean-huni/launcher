package com.example.service.sftp;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SftpOpServiceImpl implements SftpOpService {
    @Override
    public List<String> listAllFilesFromDir(String dirPath) {
        return null;
    }

    @Override
    public void moveFile(String source, String destination) {

    }
}
