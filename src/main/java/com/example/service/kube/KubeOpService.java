package com.example.service.kube;

import com.example.exception.JobAlreadyExistsException;

import java.util.Collection;

public interface KubeOpService {
    void createPod(String jobNamePostfix, String filePath) throws JobAlreadyExistsException;

    void destroyPod();

    Collection<KPod> getPodStatus();
}
