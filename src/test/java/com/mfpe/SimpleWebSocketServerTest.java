package com.mfpe;

import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.websocket.RxWebSocketClient;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class SimpleWebSocketServerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleWebSocketClient.class);
    SimpleWebSocketClient webSocketClient;

    @Inject
    @Client("http://localhost:8180")
    RxWebSocketClient client;

    @BeforeEach
    void connect(){
        webSocketClient = client
                .connect(SimpleWebSocketClient.class, "/ws/simple/prices")
                .blockingFirst();

        LOG.info("Client session: {}", webSocketClient.getSession());
    }

    @Test
    void canRecieveMessagesWithClient(){
        webSocketClient.send("Hello");

        Awaitility.await().timeout(Duration.ofSeconds(10)).untilAsserted(() -> {
            Object[] observedMessages = webSocketClient.getObservedMessages().toArray();

            LOG.info("Observed messages {} - {}",
                    webSocketClient.getObservedMessages().size(),
                    observedMessages);

            assertEquals("Connected!", observedMessages[0]);
            assertEquals("Not supported => (Hello)", observedMessages[1]);
        });
    }

    @Test
    void canRecieveReactiveMessagesWithClient(){
        webSocketClient.sendReactive("Hello").blockingGet();

        Awaitility.await().timeout(Duration.ofSeconds(10)).untilAsserted(() -> {
            Object[] observedMessages = webSocketClient.getObservedMessages().toArray();

            LOG.info("Observed messages {} - {}",
                    webSocketClient.getObservedMessages().size(),
                    observedMessages);

            assertEquals("Connected!", observedMessages[0]);
            assertEquals("Not supported => (Hello)", observedMessages[1]);
        });
    }

}
