package com.ztlsir.shared.event.rabbit;

import com.ztlsir.shared.event.DomainEvent;
import com.ztlsir.shared.event.DomainEventSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

public class RabbitDomainEventSender implements DomainEventSender {
    private final RabbitTemplate rabbitTemplate;
    private final EducationRabbitProperties educationRabbitProperties;


    public RabbitDomainEventSender(MessageConverter messageConverter,
                                   EducationRabbitProperties educationRabbitProperties,
                                   RabbitTemplate rabbitTemplate) {
        this.educationRabbitProperties = educationRabbitProperties;
        rabbitTemplate.setMessageConverter(messageConverter);
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(DomainEvent event) {
        String exchange = educationRabbitProperties.getPublishX();
        String routingKey = event.getClass().getName();
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
