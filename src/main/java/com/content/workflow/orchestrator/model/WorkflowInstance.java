package com.content.workflow.orchestrator.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.postgresql.util.PGobject;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "workflow_instances")
@Getter
@Setter
public class WorkflowInstance {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "workflow_definition_id")
    private WorkflowDefinition workflowDefinition;

    private String status;
    private String currentStep;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> inputData;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private PGobject outputData;

    private Date startedAt;
    private Date completedAt;
}
