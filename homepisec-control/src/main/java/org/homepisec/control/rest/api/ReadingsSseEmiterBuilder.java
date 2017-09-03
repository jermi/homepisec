package org.homepisec.control.rest.api;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class ReadingsSseEmiterBuilder {

    private final PublishSubject<DeviceEvent> eventsSubject;

    public ReadingsSseEmiterBuilder(PublishSubject<DeviceEvent> eventsSubject) {
        this.eventsSubject = eventsSubject;
    }

    public SseEmitter create() {
        final SseEmitter sseEmitter = new SseEmitter(-1L);
        final Disposable disposable = eventsSubject.subscribe(e -> sseEmitter.send(e, MediaType.APPLICATION_JSON_UTF8));
        sseEmitter.onCompletion(disposable::dispose);
        sseEmitter.onTimeout(disposable::dispose);
        return sseEmitter;
    }

}
