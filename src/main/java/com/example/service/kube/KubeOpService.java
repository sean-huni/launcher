package com.example.service.kube;

public interface KubeOpService {

    void createPod();

    void destroyPod();

    String getPodStatus();
}
