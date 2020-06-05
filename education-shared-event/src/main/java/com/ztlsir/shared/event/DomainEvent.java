package com.ztlsir.shared.event;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Getter
public abstract class DomainEvent {
    private String _id = UUID.randomUUID().toString();
    private Instant _createdAt = now();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + _id + "]";
    }

}
