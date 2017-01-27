package actor.basics;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.Serializable;

/**
 * Created by govind.bhone on 12/29/2016.
 */
class Greeting1 implements Serializable {
    public final String who;

    public Greeting1(String who) {
        this.who = who;
    }
}

class GreetingActor1 extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public void onReceive(Object message) throws Exception {
        if (message instanceof Greeting1) {
            log.info("Hello {}", ((Greeting1) message).who);
        }
    }
}


public class GreetingDemo1 {
    public static void main(String args[]) {
        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef greeter = system.actorOf(Props.create(GreetingActor1.class), "greeter");
        greeter.tell(new Greeting1("Java Guys !"), greeter);

    }
}