package com.coldlight.payment_provider.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "webhook")
public class Webhook extends BaseEntity{

    @Column(name = "webhook_event_type")
    @Enumerated(EnumType.STRING)
    private WebhookEventType webhookEventType;

    @Column(name = "url")
    private String url;
}
