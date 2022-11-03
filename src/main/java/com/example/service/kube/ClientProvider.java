package com.example.service.kube;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.log4j.Log4j2;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

@Log4j2
public class ClientProvider {

    @ConfigProperty(name = "kube.config")
    private String kubeConfig;

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