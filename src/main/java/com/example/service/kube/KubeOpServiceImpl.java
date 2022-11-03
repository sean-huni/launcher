package com.example.service.kube;

import com.example.exception.JobAlreadyExistsException;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
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
    }

    //    @Override
    @Deprecated
    public void createPod2() throws FileNotFoundException {
        final Job j = client.batch().v1().jobs().load(new FileInputStream("/Users/sean/env/eq/proj/spring/ms/launcher/.job/sbp-job.yml")).get();
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

    public void destroyPod() {

    }

    public Collection<KPod> getPodStatus() {

//        return  client.pods().list().getItems().stream()
//                .filter(p -> p.getMetadata().getName().contains("sbp"))
//                .map(pod -> new KPod(pod.getMetadata().getName(), pod.getStatus().getMessage()))
//                .collect(Collectors.toCollection(ArrayList::new));

        return client.pods().list().getItems().stream().filter(p -> p.getStatus().getContainerStatuses().size() > 0 && p.getMetadata().getName().contains("sbp")).map(p -> new KPod(p.getMetadata().getName(), p.getStatus().getContainerStatuses().stream().findFirst().get().getState().toString())).collect(Collectors.toList());
    }
}

/*client.pods().list().getItems().get(0).getStatus().getContainerStatuses().get(0).getName() +"\t" +
 */

//record KPod(String name, String status){};