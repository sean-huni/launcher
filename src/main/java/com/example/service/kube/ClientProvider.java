package com.example.service.kube;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@Log4j2
public class ClientProvider {
    private final String kubeConfig;


    public ClientProvider(@ConfigProperty(name = "kube.config") String kubeConfig) {
        this.kubeConfig = kubeConfig;
    }


    @Produces
    @Singleton
    @Named("namespace")
    private String findNamespace() {
        return "default";
    }

    @Produces
    @Singleton
    KubernetesClient newClient(@Named("namespace") String namespace) {
        log.info("Loading Kube-Config from: {}", kubeConfig);
        System.setProperty("kubeconfig", kubeConfig);
        try (KubernetesClient k8 = new DefaultKubernetesClient().inNamespace(namespace)) {
            return k8;
        } catch (Exception ex) {
            log.error(ex);
        }
        return null;
    }
}
