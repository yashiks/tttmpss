package nl.avisi.camel;

import org.apache.camel.builder.RouteBuilder;


public final class StreamingRoute extends RouteBuilder {

    @Override
    public final void configure() throws Exception {
        from("activemq:my-queue1")
                .to("websocket://bubble?sendToAll=true");
    }
}
