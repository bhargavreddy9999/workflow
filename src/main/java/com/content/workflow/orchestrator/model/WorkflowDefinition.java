package com.content.workflow.orchestrator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "workflow_definitions")
@Getter
@Setter
public class WorkflowDefinition {
    @Id
    private UUID id;
    private String name;
    private int version;

    @Column(columnDefinition = "jsonb")
    private String definition;

    private Date createdAt;
}
