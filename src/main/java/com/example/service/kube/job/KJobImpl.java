package com.example.service.kube.job;

import com.example.service.kube.IKJob;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarSource;
import io.fabric8.kubernetes.api.model.SecretKeySelector;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;

@ApplicationScoped
public class KJobImpl implements IKJob {
    private final String dbHost;
    private final String dbPort;
    private final String dbName;
    private final String sbpVer;//sbp-app version

    public KJobImpl(@ConfigProperty(name = "db.host") String dbHost, @ConfigProperty(name = "db.port") String dbPort,
                    @ConfigProperty(name = "db.name") String dbName, @ConfigProperty(name = "app.sbp.ver") String sbpVer) {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.sbpVer = sbpVer;
    }

    /**
     * Builds a Java {@link Job} Object representation of a Kubernetes Job Yml File.
     * An Example located in: src/test/resources/config/job/sbp-job-template.yml
     *
     * @param jobNamePostfix Must be unique or else the application will fail an Error Containing
     *                       'Invalid value... field is immutable'
     * @param abFileName     Batch-file to be processed.
     * @return A {@link Job} that is ready to be created/launched.
     */
    public Job buildNewJob(final String jobNamePostfix, final String abFileName) {
        final var envList = new ArrayList<EnvVar>();
        envList.add(new EnvVar("DB_HOST", dbHost, null));
        envList.add(new EnvVar("DB_PORT", dbPort, null));
        envList.add(new EnvVar("DB_NAME", dbName, null));
        envList.add(new EnvVar("DB_USER", null, new EnvVarSource(null, null, null, new SecretKeySelector("db.username", "db-secret", false))));
        envList.add(new EnvVar("DB_PASS", null, new EnvVarSource(null, null, null, new SecretKeySelector("db.password", "db-secret", false))));

        final var cList = new ArrayList<Container>();
        cList.add(new ContainerBuilder()
                .withName("sbp-job")
                .withImage("s34n/sbp:%s".formatted(sbpVer))
                .withArgs("fileName=%s".formatted(abFileName))
                .withEnv(envList)
                .build());


        return new JobBuilder().withApiVersion("batch/v1")
                .withKind("Job")
                .withNewMetadata()
                .withName("sbp-%s".formatted(jobNamePostfix))
                .endMetadata()
                .withNewSpec()
                .withNewTemplate()
                .withNewSpec()
                .withRestartPolicy("OnFailure")
                .addAllToContainers(cList)
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();
    }
}
