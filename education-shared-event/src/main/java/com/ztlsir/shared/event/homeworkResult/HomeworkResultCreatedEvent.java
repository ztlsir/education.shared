package com.ztlsir.shared.event.homeworkResult;

import com.ztlsir.shared.event.DomainEvent;
import lombok.Getter;

@Getter
public class HomeworkResultCreatedEvent extends DomainEvent {
    private String homeworkResultId;

    public HomeworkResultCreatedEvent(){}
    public HomeworkResultCreatedEvent(String homeworkResultId) {
        this.homeworkResultId = homeworkResultId;
    }
}
