package com.example.service.kube;

import com.example.exception.JobAlreadyExistsException;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.quarkus.runtime.StartupEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@ApplicationScoped
@RequiredArgsConstructor
public class KubeOpServiceImpl implements KubeOpService {
    private final KubernetesClient client;
    private final IKJob job;


    @PostConstruct
    public void init(@Observes StartupEvent ev) {
        final List<Pod> podList = client.pods().list().getItems();
        log.info("Found: {} Pods.", podList.size());
        for (final Pod pod : podList) {
            log.info(" * {}", pod.getMetadata().getName());
        }

        try (var x = client.pods().watch(new Watcher<Pod>() {
            /**
             * If the Watcher can reconnect itself after an error
             *
             * @return
             */
            @Override
            public boolean reconnecting() {
                return Watcher.super.reconnecting();
            }


            @Override
            public void eventReceived(Action action, Pod pod) {
                log.info("Event-Action: {}", action.name());
                log.info("Pod: {}", pod.getStatus().getNominatedNodeName());
            }


            /**
             * Invoked when the watcher is gracefully closed.
             */
            @Override
            public void onClose() {
                Watcher.super.onClose();
            }

            /**
             * Invoked when the watcher closes due to an Exception.
             *
             * @param cause What caused the watcher to be closed.
             */
            @Override
            public void onClose(WatcherException cause) {
                log.error(cause);
            }
        })) {
            log.info(x);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    /**
     * Left this code for educational purposes. It's essential to spot why this wasn't an ideal approach, even though
     * it works.
     *
     * @throws FileNotFoundException if the sbp-job.yml isn't found.
     */
    @Deprecated
    public void createPod2() throws FileNotFoundException {
        final Job j = client.batch().v1().jobs().load(new FileInputStream(".job/sbp-job.yml")).get();
        final Job job = client.batch().v1().jobs().inNamespace("default").createOrReplace(j);
//       final Pod p = client.pods().load(new FileInputStream("")).get();
//       var pod = client.pods().createOrReplace(p);
        log.info("Job State: {}", job.getStatus().toString());
    }

    @Override
    public void createPod(final String jobNamePostfix, final String filePath) throws JobAlreadyExistsException {

        final Job j = job.buildNewJob(jobNamePostfix, filePath);
        try {
            final Job job = client.batch().v1().jobs().inNamespace("default").createOrReplace(j);
            log.info("Job State: {}", job.getStatus().toString());
        } catch (KubernetesClientException kex) {
            log.error(kex);
            if (kex.getMessage().contains("field is immutable")) {
                throw new JobAlreadyExistsException("Job Already with the same matadata.name or name already exists.", kex);
            } else throw kex;
        }
//       final Pod p = client.pods().load(new FileInputStream("")).get();
//       var pod = client.pods().createOrReplace(p);

    }

    @Override
    public void destroyPod(final Job j) {
        client.batch().v1().jobs().inNamespace("default").delete(j);
    }

    public Collection<KPod> getPodStatus() {
        return client.pods().list()
                .getItems().stream()
                .filter(p -> p.getStatus().getContainerStatuses().size() > 0 && p.getMetadata().getName().contains("sbp"))
                .map(p -> new KPod(p.getMetadata().getName(),
                        p.getStatus().getContainerStatuses().stream().findFirst().get().getState().toString()))
                .collect(Collectors.toList());
    }
}

//record KPod(String name, String status){};