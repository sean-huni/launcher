package com.example.service.logic;

public class ExecutionSeq {

    public void executeFlow() {
        //Step 1. Poll/listen to the directory & Read (New File) the FileName. (50% Completed)
        //Step 2. Check the filename against the Batch-Params table. If filename already exist, skip to Step 4
        //Step 3. Launch Kubernetes Tasks. (Done)
        //Step 4. Move the files from source to archive/error (Done)
        //Step 5. Destroy completed pods
    }
}
