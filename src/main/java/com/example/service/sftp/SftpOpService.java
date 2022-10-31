package com.example.service.sftp;

import java.util.List;

public interface SftpOpService {

    List<String> listAllFilesFromDir(final String dirPath);

    void moveFile(final String source, final String destination);
}
