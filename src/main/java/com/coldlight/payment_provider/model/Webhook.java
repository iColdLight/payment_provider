package com.coldlight.payment_provider.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "webhook")
public class Webhook extends BaseEntity{

    @Column(name = "webhook_event_type")
    @Enumerated(EnumType.STRING)
    private WebhookEventType webhookEventType;

    @Column(name = "url")
    private String url;

    @Column(name = "retry_counter")
    private int retryCounter;

    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "response_status")
    private int responseStatus;

    @Column(name = "response_body")
    private String responseBody;
}
