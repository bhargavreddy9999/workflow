package com.content.workflow.orchestrator.service;

import com.content.workflow.orchestrator.model.WorkflowDefinition;
import com.content.workflow.orchestrator.model.WorkflowInstance;
import com.content.workflow.orchestrator.repository.WorkflowDefinitionRepository;
import com.content.workflow.orchestrator.repository.WorkflowInstanceRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WorkflowService {

    private final WorkflowDefinitionRepository definitionRepository;
    private final WorkflowInstanceRepository instanceRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public WorkflowService(
            WorkflowDefinitionRepository definitionRepository,
            WorkflowInstanceRepository instanceRepository,
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.definitionRepository = definitionRepository;
        this.instanceRepository = instanceRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public WorkflowInstance startWorkflow(UUID definitionId, Map<String, Object> inputData) {
        WorkflowDefinition def = definitionRepository.findById(definitionId)
                .orElseThrow(() -> new RuntimeException("Definition not found"));

        WorkflowInstance instance = new WorkflowInstance();
        instance.setId(UUID.randomUUID());
        instance.setWorkflowDefinition(def);
        instance.setStatus("RUNNING");
        instance.setStartedAt(new Date());
        instance.setInputData(inputData);
        instance.setCurrentStep("init");

        WorkflowInstance savedInstance = instanceRepository.save(instance);

        Map<String, Object> kafkaPayload = new HashMap<>();
        kafkaPayload.put("workflowInstanceId", instance.getId().toString());
        kafkaPayload.put("definitionId", definitionId.toString());
        kafkaPayload.put("stepName", "validate-invoice");
        kafkaPayload.put("input", inputData);

        kafkaTemplate.send("workflow-step", kafkaPayload);

        return savedInstance;
    }
}
