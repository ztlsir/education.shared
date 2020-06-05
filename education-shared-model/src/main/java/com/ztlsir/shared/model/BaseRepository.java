package com.ztlsir.shared.model;

import com.ztlsir.shared.event.DomainEventSender;

public abstract class BaseRepository<TAggregate extends BaseAggregate> {
    private DomainEventSender sender;

    protected BaseRepository(DomainEventSender sender) {
        this.sender = sender;
    }

    public final void save(TAggregate aggregate) {
        doSave(aggregate);
        aggregate.get_events().forEach(event -> {
            sender.send(event);
        });
        aggregate.clearEvents();
    }

    protected abstract void doSave(TAggregate aggregate);
}
