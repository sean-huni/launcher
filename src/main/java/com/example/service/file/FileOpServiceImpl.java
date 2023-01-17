package com.example.service.file;

import com.example.exception.ClientNotProvidedException;
import com.example.exception.FileNotFoundException;
import com.example.exception.InstructionException;
import lombok.extern.log4j.Log4j2;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

@Log4j2
@ApplicationScoped
public class FileOpServiceImpl implements FileOpService {
    private static final String CLIENT_PLACEHOLDER = "$CLIENT$";
    private static final String YEAR_PLACEHOLDER = "yyyy";
    private static final String MONTH_PLACEHOLDER = "mm";
    private static final String DAY_PLACEHOLDER = "dd";
    private static final String HOUR_PLACEHOLDER = "hr";
    private final String inDir;
    private final String archDir;
    private final String errDir;

    public FileOpServiceImpl(@ConfigProperty(name = "batch.input") final String inDir, @ConfigProperty(name = "batch.archive") final String archDir, @ConfigProperty(name = "batch.error") final String errDir) {
        this.inDir = inDir;
        this.archDir = archDir;
        this.errDir = errDir;
    }

    @Override
    public List<String> listAllFilesFromDir(String dirPath) {
        return null;
    }

    @Override
    public void moveFile(final String clientAbbr, final Instruction instruction, final String fileName) throws FileNotFoundException, ClientNotProvidedException, InstructionException {
        if (isNull(clientAbbr)) {
            throw new ClientNotProvidedException("Client Abbreviation not provided.");
        }

        if (isNull(fileName)) {
            throw new FileNotFoundException("FileName not provided.");
        }

        if (isNull(instruction)) {
            throw new InstructionException("Instruction not provided.");
        }

        final String input = inDir.replace(CLIENT_PLACEHOLDER, clientAbbr);
        checkFileExistence(fileName, input);
        var ldt = extractFileDateTimeCreated(fileName, input);

        final String error = dateReplacement(errDir.replace(CLIENT_PLACEHOLDER, clientAbbr), ldt);
        final String archive = dateReplacement(archDir.replace(CLIENT_PLACEHOLDER, clientAbbr), ldt);

        log.info("Client: {}", clientAbbr);
        log.info("Op.Instruction: {}", instruction);
        log.info("FileName: {}", fileName);

        switch (instruction) {
            case INPUT_TO_ARCHIVE -> moveEachFile(fileName, input, archive);
            case INPUT_TO_ERROR -> moveEachFile(fileName, input, error);
            default -> log.warn("Only INPUT_TO_ARCHIVE & INPUT_TO_ERROR operations have been defined...");
        }
    }

    private LocalDateTime extractFileDateTimeCreated(final String fileName, final String input) {
        var fileLocation = "%s%s%s".formatted(input, File.separator, fileName);
        try {
            return LocalDateTime.parse(Files.getFileAttributeView(Paths.get(fileLocation), BasicFileAttributeView.class).readAttributes().creationTime().toString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
        } catch (IOException ex) {
            log.warn("Cannot read file attributes: {}", fileLocation);
            log.warn(ex);
            return LocalDateTime.now();
        }
    }

    private void moveEachFile(final String fileName, final String input, final String destination) throws FileNotFoundException {
        checkFileExistence(fileName, input);
        final String inputFilePath = "%s%s%s".formatted(input, File.separator, fileName);
        final String destinationFilePath = "%s%s%s".formatted(destination, File.separator, fileName);

        createDirIfNotExists("%s%s".formatted(destination, File.separator));

        log.info("====================START - FILE-MOVE-OPERATION - START=====================");
        log.info("From: {}", inputFilePath);
        log.info("To: {}", destinationFilePath);

        final File inputFile = new File(inputFilePath);
        inputFile.renameTo(new File(destinationFilePath));
        log.info("====================END - FILE-MOVE-OPERATION - END=====================");
    }

    private void createDirIfNotExists(final String destinationFilePath) {
        final File folder = new File(destinationFilePath);
        if (folder.mkdirs()) {
            log.info("Directory Created...\n{}", destinationFilePath);
        } else log.info("Directory already existed...\n{}", destinationFilePath);

    }

    private void checkFileExistence(final String fileName, final String input) throws FileNotFoundException {
        log.info("------Checking File Existence---------\nFile: {}\nDirectory: {}", fileName, input);
        final File folder = new File(input);
        final File[] listOfFiles = folder.listFiles();

        if (isNull(listOfFiles) || !Arrays.stream(listOfFiles).map(String::valueOf).anyMatch(f -> f.contains(fileName))) {
            log.error("file %s not found from: %s".formatted(fileName, input));
            throw new FileNotFoundException("file %s not found from: %s".formatted(fileName, input));
        }
    }

    private String dateReplacement(final String dirPath, final LocalDateTime ldt) {
        return dirPath.replaceAll(YEAR_PLACEHOLDER, String.valueOf(ldt.getYear())).replaceAll(MONTH_PLACEHOLDER, String.valueOf(ldt.getMonthValue())).replaceAll(DAY_PLACEHOLDER, String.valueOf(ldt.getDayOfMonth())).replaceAll(HOUR_PLACEHOLDER, String.valueOf(ldt.getHour()));
    }
}
