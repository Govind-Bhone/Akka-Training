package remote2.example;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import common.*;

public class RemoteActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
   /*     if (message instanceof Ping) {*/
            // Get reference to the message sender and reply back
            log.info("Message Received {}", message);
            getSender().tell(new Pong(), self());
     /*   }*/
    }
}
