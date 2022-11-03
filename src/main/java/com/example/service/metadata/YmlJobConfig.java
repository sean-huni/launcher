package com.example.service.metadata;

public interface YmlJobConfig {

    String data = """
           apiVersion: batch/v1
           kind: Job
           metadata:
             name: sbp-$JOB_NAME
           spec:
             template:
               spec:
                 restartPolicy: OnFailure
                 containers:
                   - name: sbp-job
                     image: s34n/sbp:2.1.1
                     imagePullPolicy: Always
                     args: [ "fileName=$FILE_NAME" ]
                     env:
                       - name: DB_HOST
                         value: "78.141.201.212"
                       - name: DB_PORT
                         value: "3306"
                       - name: DB_NAME
                         value: "sbp"
                       - name: DB_USER
                         valueFrom:
                           secretKeyRef:
                             name: db-secret
                             key: db.username
                       - name: DB_PASS
                         valueFrom:
                           secretKeyRef:
                             name: db-secret
                             key: db.password
             """;
}
