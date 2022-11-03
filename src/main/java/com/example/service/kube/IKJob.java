package com.example.service.kube;

import io.fabric8.kubernetes.api.model.batch.v1.Job;

public interface IKJob {
    Job buildNewJob(final String jobNamePostfix, final String abFileName);
}
