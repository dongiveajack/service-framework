package org.trips.service_framework.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailData {
    private String sender;

    @JsonProperty("toAddresses")
    private List<String> toAddresses;

    private String body;

    private String subject;

    private String html;

    @JsonProperty("ccAddresses")
    private List<String> ccAddresses;

    @JsonProperty("bccAddresses")
    private List<String> bccAddresses;

    @Builder.Default
    private String charset = "UTF-8";
}
