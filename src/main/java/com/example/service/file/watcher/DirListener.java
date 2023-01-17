package com.example.service.file.watcher;

import lombok.extern.log4j.Log4j2;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@Log4j2
public class DirListener {
    private static final String dir = "src/test/resources/data/demo";


    void pollDir() {
        try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
            final Path path = Paths.get(dir);
            boolean poll = true;

            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

            while (poll) {
                final WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    log.info("Event Triggered from Directory: {}", dir);
                    log.info("Event kind: {} - File: {}", event.kind(), event.context());
                }
                poll = key.reset();
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
