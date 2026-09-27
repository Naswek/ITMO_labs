package org.example.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.example.utils.cdi.CollectionChanged;

@ApplicationScoped
public class ChangeStream {

    private final Set<Client> clients = ConcurrentHashMap.newKeySet();

    public void subscribe(SseEventSink sink, Sse sse) {
        Client client = new Client(sink, sse);
        clients.add(client);
        sink.send(sse.newEventBuilder().name("ready").data("connected").build())
                .whenComplete((ignored, error) -> {
                    if (error != null) {
                        clients.remove(client);
                        sink.close();
                    }
                });
    }

    public void changed(@Observes(during = TransactionPhase.AFTER_SUCCESS) CollectionChanged event) {
        for (Client client : clients) {
            if (client.sink().isClosed()) {
                clients.remove(client);
                continue;
            }
            client.sink().send(client.sse().newEventBuilder().name("changed").data("refresh").build())
                    .whenComplete((ignored, error) -> {
                        if (error != null) {
                            clients.remove(client);
                            client.sink().close();
                        }
                    });
        }
    }

    private record Client(SseEventSink sink, Sse sse) {
    }
}
