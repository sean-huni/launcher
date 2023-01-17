package com.example.service.metadata;

import lombok.extern.log4j.Log4j2;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Log4j2
@ApplicationScoped
public class MetadataEditorServiceImpl implements MetadataEditorService {
    private final String batchFilesDir;
    private final String batchConfigDir;

    public MetadataEditorServiceImpl(@ConfigProperty(name = "batch.dir") String batchFilesDir,
                                     @ConfigProperty(name = "batch.job") String batchConfigDir) {
        this.batchFilesDir = batchFilesDir;
        this.batchConfigDir = batchConfigDir;
    }

    @Deprecated(since = "03-Nov-2022")
    public void writeYmlConfigFile() {

        final ClassLoader classLoader = getClass().getClassLoader();
        try {
            var items = classLoader.getResources(batchFilesDir);
            items.asIterator().forEachRemaining(i -> log.info("File: {}", i.getPath()));
        } catch (IOException e) {
            log.error(e);
        }

        final File folder = new File(batchFilesDir);
        final File[] listOfFiles = folder.listFiles();

        final var abFile = listOfFiles[0].getAbsoluteFile().getAbsoluteFile();
        final var abFileName = abFile.getAbsoluteFile().getAbsolutePath();
        final var fileName = abFile.getAbsoluteFile().getName();

        log.info("Absolute File Path: {}", abFileName);
        log.info("File Name: {}", fileName);

        var jobYml = YmlJobConfig.data.replace("$JOB_NAME", "%s-%s".formatted(fileName.split("_")[0], //account-no
                fileName.split("_")[3].replace(".csv", "")).toLowerCase() //Random-No
        ).replace("$FILE_NAME", abFileName);

//        final var exeCmd1 = "echo " + jobYml + " > ";
        final var path = batchConfigDir.replace("/", File.separator); //On Windows it's a backslash.
        createJobConfigDirIfNotExist();

        saveJobConfig(jobYml, path);
    }

    /**
     * Use Immutable
     * @param jobYml
     * @param path
     */
    @Deprecated(since = "03-Nov-2022")
    private void saveJobConfig(final String jobYml, final String path) {
        log.info("......writing sbp-job.yml config......");
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(jobYml);
            writer.close();
            log.info("......writer completed......");
        } catch (IOException e) {
            log.error(e);
        }
    }

    private void createJobConfigDirIfNotExist() {
        var dir = batchConfigDir.split(File.separator)[0];
        log.info("Probe Dir: {}", dir);
        final File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
