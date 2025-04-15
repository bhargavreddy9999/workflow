package com.content.workflow.orchestrator.repository;

import com.content.workflow.orchestrator.model.WorkflowDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkflowDefinitionRepository extends JpaRepository<WorkflowDefinition, UUID> {}
