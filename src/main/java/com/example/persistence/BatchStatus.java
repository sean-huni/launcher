package com.example.persistence;

public enum BatchStatus {
    ABANDONED,
    COMPLETED, //The order of the status values is significant because it can be used to aggregate a set of status values - the result should be the maximum value.
    FAILED,
    STARTED,
    STARTING,
    STOPPED,
    STOPPING,
    UNKNOWN
}
