package com.example.service.metadata;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MetadataEditorTest {
    @Inject
    MetadataEditorService metadataEditorService;

    @Test
    void giveYmlConfigService_whenReadingSamples_readNewSamples() {
        metadataEditorService.writeYmlConfigFile();
    }
}