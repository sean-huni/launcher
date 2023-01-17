package com.example.service.file;

import com.example.exception.ClientNotProvidedException;
import com.example.exception.FileNotFoundException;
import com.example.exception.InstructionException;

import java.util.List;

public interface FileOpService {

    List<String> listAllFilesFromDir(final String dirPath);

    void moveFile(final String clientAbbr, final Instruction instruction, final String fileName) throws FileNotFoundException, ClientNotProvidedException, InstructionException;
}
