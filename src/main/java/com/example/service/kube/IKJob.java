package com.example.service.kube;

import io.fabric8.kubernetes.api.model.batch.v1.Job;

public interface IKJob {
    /**
     * Job Builder for building a job required for creating a pod-instance for execution.
     *
     * @param jobNamePostfix unique characters appended to the original job name to uniquely identify a job.
     * @param abFileName     absolute fileName appended to the absolute file path.
     * @return A {@link Job} object ready for instantiation.
     */
    Job buildNewJob(final String jobNamePostfix, final String abFileName);
}
