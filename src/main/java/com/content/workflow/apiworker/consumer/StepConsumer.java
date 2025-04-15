package com.content.workflow.apiworker.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class StepConsumer {

    private static final Logger logger = LoggerFactory.getLogger(StepConsumer.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public StepConsumer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "workflow-step", groupId = "workflow-group")
    public void consumeStep(Map<String, Object> payload, Acknowledgment ack) {
        logger.info("Received step execution payload: {}", payload);
        ack.acknowledge();
        try {
            String stepName = (String) payload.get("stepName");
            String instanceId = (String) payload.get("workflowInstanceId");
            Map<String, Object> input = (Map<String, Object>) payload.get("input");

            // Hardcoded logic to simulate API step execution (could be dynamic later)
            if ("validate-invoice".equals(stepName)) {
                String endpoint = "http://localhost:8081/validate";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, Object>> request = new HttpEntity<>(input, headers);

                Map response = restTemplate.postForObject(endpoint, request, Map.class);

                logger.info("Step '{}' executed successfully for instance {}", stepName, instanceId);

                // Emit to step-complete topic
                kafkaTemplate.send("workflow-step-complete", Map.of(
                        "workflowInstanceId", instanceId,
                        "stepName", stepName,
                        "status", "COMPLETED",
                        "output", response
                ));
            }
        } catch (Exception e) {
            logger.error("Error executing step: {}", e.getMessage(), e);
        }
    }
}
