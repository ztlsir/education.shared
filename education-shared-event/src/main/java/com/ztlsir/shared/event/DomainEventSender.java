package com.ztlsir.shared.event;

public interface DomainEventSender {
    void send(DomainEvent event);
}

