package com.mfpe;

import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.reactivex.Single;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@ClientWebSocket("/ws/simple/prices")
public abstract class SimpleWebSocketClient implements AutoCloseable {

    private final Collection<String> observedMessages = new ConcurrentLinkedQueue<>();

    public Collection<String> getObservedMessages() {
        return observedMessages;
    }

    private WebSocketSession session;

    public WebSocketSession getSession() {
        return session;
    }

    @OnOpen
    public void onOpen(WebSocketSession session){
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message){
        observedMessages.add(message);
    }

    public abstract void send(String message);

    public abstract Single<String> sendReactive(String message);

}
