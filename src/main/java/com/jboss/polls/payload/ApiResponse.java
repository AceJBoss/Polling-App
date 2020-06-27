package com.jboss.polls.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({
        "error",
        "message"
})
public class ApiResponse {
    @JsonProperty
    private boolean error;
    @JsonProperty
    private String message;
    private Object data;
  
  }