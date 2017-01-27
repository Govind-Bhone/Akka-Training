package actor.basics;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.FiniteDuration;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by govind.bhone on 1/2/2017.
 */

class OneWayGreeting implements Serializable {
    public final String who;
    public OneWayGreeting(String who) {
        this.who = who;
    }
}

class GreetingAndResponse implements Serializable {
    public final String who;
    public GreetingAndResponse(String who) {
        this.who = who;
    }
}

class GreetingActor2 extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public void onReceive(Object message) throws Exception {
        if (message instanceof OneWayGreeting) {
            log.info("Hello {}", ((OneWayGreeting) message).who);
        }else if(message instanceof GreetingAndResponse){
            sender().tell("Thanks for wishing me ",ActorRef.noSender());
        }
    }
}
public class InboxExample {
    public static void main(String args[]) {
        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef greeter = system.actorOf(Props.create(GreetingActor2.class), "greeter");
        greeter.tell(new OneWayGreeting("Java Guys !"), greeter);

        Inbox inbox=Inbox.create(system);
        inbox.send(greeter, new GreetingAndResponse("Hello"));
        Object result = inbox.receive(FiniteDuration.apply(5, TimeUnit.SECONDS));
        System.out.println("Greeting Response :"+result.toString());

    }
}
