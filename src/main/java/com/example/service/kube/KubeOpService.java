package com.example.service.kube;

import com.example.exception.JobAlreadyExistsException;
import io.fabric8.kubernetes.api.model.batch.v1.Job;

import java.util.Collection;

public interface KubeOpService {
    /**
     * Creates a Kubernetes Job/Pod
     *
     * @param jobNamePostfix
     * @param filePath
     * @throws JobAlreadyExistsException
     */
    void createPod(String jobNamePostfix, String filePath) throws JobAlreadyExistsException;

    /**
     * Destroys a Kubernetes Job currently in running state. Job defined as; any Pod instantiated as a Job.
     *
     * @param job in a running state
     */
    void destroyPod(final Job job);

    Collection<KPod> getPodStatus();
}
