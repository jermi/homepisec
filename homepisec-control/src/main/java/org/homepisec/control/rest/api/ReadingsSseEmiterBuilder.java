package org.homepisec.control.rest.api;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.core.ReadingsService;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Component
public class ReadingsSseEmiterBuilder {

    private final PublishSubject<DeviceEvent> eventsSubject;

    public ReadingsSseEmiterBuilder(
            PublishSubject<DeviceEvent> eventsSubject
    ) {
        this.eventsSubject = eventsSubject;
    }

    public SseEmitter create() {
        final SseEmitter sseEmitter = new SseEmitter(-1L);
        final Observable<DeviceEvent> observableDeltas = eventsSubject
                .filter(new DeviceEventsDeltaFilter())
                .buffer(1, TimeUnit.SECONDS)
                .flatMapIterable(e -> e);
        final Disposable disposable = observableDeltas.subscribe(e -> sseEmitter.send(e, MediaType.APPLICATION_JSON_UTF8));
        sseEmitter.onTimeout(disposable::dispose);
        sseEmitter.onCompletion(disposable::dispose);
        return sseEmitter;
    }

}
