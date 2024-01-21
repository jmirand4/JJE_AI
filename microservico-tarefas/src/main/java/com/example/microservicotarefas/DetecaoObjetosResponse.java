package com.example.microservicotarefas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DetecaoObjetosResponse {

    private final String message;
    private final int count;
    private final List<Prediction> predictions;
    private final boolean success;

    @JsonCreator
    public DetecaoObjetosResponse(
            @JsonProperty("message") String message,
            @JsonProperty("count") int count,
            @JsonProperty("predictions") List<Prediction> predictions,
            @JsonProperty("success") boolean success
    ) {
        this.message = message;
        this.count = count;
        this.predictions = predictions;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public int getCount() {
        return count;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public boolean isSuccess() {
        return success;
    }

    public static class Prediction {
        private final double confidence;
        private final String label;

        @JsonCreator
        public Prediction(
                @JsonProperty("confidence") double confidence,
                @JsonProperty("label") String label
        ) {
            this.confidence = confidence;
            this.label = label;
        }

        public double getConfidence() {
            return confidence;
        }

        public String getLabel() {
            return label;
        }
    }
}
