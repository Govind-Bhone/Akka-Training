package remote3.deployment;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import common.Ping;
import common.Pong;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class LocalActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void preStart() {
        //Get a reference to the remote actor
        log.info("Remote deployement Actor started ");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("Message from remote node received " + message);
    }
}
