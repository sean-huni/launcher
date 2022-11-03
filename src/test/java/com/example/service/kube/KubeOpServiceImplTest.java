package com.example.service.kube;

import com.example.exception.JobAlreadyExistsException;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@QuarkusTest
class KubeOpServiceImplTest {

    @Inject
     KubeOpService kubeOpService;

    @Test
    void givenKubernetesClient_whenProbingPodStatus_thenReturnStatus() throws JobAlreadyExistsException {
       var resp = kubeOpService.getPodStatus();

        resp.forEach(Assertions::assertNotNull);
        resp.forEach(p -> log.info(p.toString()));

        kubeOpService.createPod("100014538-48BD-9DB0",
                "/Users/sean/env/eq/proj/spring/ms/launcher/src/test/resources/data/samples/" +
                        "100014538_2022-09-17_18.45.52.923651_48BD-9DB0.csv");
    }
}