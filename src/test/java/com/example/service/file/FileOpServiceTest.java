package com.example.service.file;

import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.log4j.Log4j2;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;

@Log4j2
@QuarkusTest
class FileOpServiceTest {
    private static final String CLIENT_PLACEHOLDER = "$CLIENT$";
    private static final String YEAR_PLACEHOLDER = "yyyy";
    private static final String MONTH_PLACEHOLDER = "mm";
    private static final String DAY_PLACEHOLDER = "dd";
    private static final String HOUR_PLACEHOLDER = "hr";
    private final FileOpService fileOpService;

    @ConfigProperty(name = "batch.input")
    String inDir;
    @ConfigProperty(name = "batch.archive")
    String archDir;
    @ConfigProperty(name = "batch.error")
    String errDir;
    final String client1 = "client-1";


    @Inject
    public FileOpServiceTest(FileOpService fileOpService) {
        this.fileOpService = fileOpService;
    }

    @BeforeEach
    void setUp() {
        try {
            final File fos = new File("src/test/resources/data/demo/client-1/arc/2022/11/6/18/demo.csv");
        } catch (Exception ex) {
            throw ex;
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        var path = "src/test/resources/data/demo/client-1/arc/2022/11/6/16/demo.csv";
        var destPath = inDir.replace("$CLIENT$", client1) + File.separator + "demo.csv";
        log.info("Moving from: {}", path);

        log.info("Move file to Input...");
        log.info(destPath);
        final File inputFile = new File(path);
        inputFile.renameTo(new File(destPath));
        log.info("File Moved to Input...");
        if ((new File("src/test/resources/data/demo/client-1/arc/2022")).delete()) {
            log.info("deleted");
        }
    }

    @DisplayName("Move File - Positive")
    @Test
    void givenFileOpService_whenMovingFileFromInToArchiveDir_thenCheckFile() throws Exception {
        var fileName = "demo.csv";
        fileOpService.moveFile(client1, Instruction.INPUT_TO_ARCHIVE, fileName);
    }
}