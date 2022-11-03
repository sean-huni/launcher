package com.example.service.kube;

public class KPod<N, S> {
    private N name;
    private S status;


    public KPod(N name, S status) {
        this.name = name;
        this.status = status;
    }

    @Override
    public String toString() {
        return name.toString() + "\t" + status.toString();
    }
}
