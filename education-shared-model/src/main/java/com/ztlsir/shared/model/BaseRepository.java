package com.ztlsir.shared.model;

public abstract class BaseRepository<TAggregate> {

    public final void save(TAggregate aggregate) {
        doSave(aggregate);
    }

    protected abstract void doSave(TAggregate aggregate);
}
