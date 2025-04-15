package com.content.workflow.orchestrator.repository;

import com.content.workflow.orchestrator.model.WorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, UUID> {}
