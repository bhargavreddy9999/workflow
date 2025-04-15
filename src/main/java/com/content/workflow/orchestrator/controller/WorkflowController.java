package com.content.workflow.orchestrator.controller;

import com.content.workflow.orchestrator.model.WorkflowInstance;
import com.content.workflow.orchestrator.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/start/{definitionId}")
    public WorkflowInstance startWorkflow(
            @PathVariable UUID definitionId,
            @RequestBody Map<String, Object> inputData
    ) {

            WorkflowInstance workflowInstance = workflowService.startWorkflow(definitionId, inputData);
            System.out.println(workflowInstance);
        return workflowInstance;
    }
}
