package mailboxes.examples;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by govind.bhone on 1/5/2017.
 */
class PriorityBasedActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    {
        for (Object msg : new Object[] { new HighPriority(), new LowPriority(),
                new HighPriority(), new LowPriority(), "pigdog2", "pigdog3", new HighPriority(),
                PoisonPill.getInstance() }) {
            getSelf().tell(msg, getSelf());
        }
    }

    public void onReceive(Object message) {
        log.info(message.toString());
    }
}


public class PriorityBasedMessageProcessor {
    public static void main(String args[]) throws InterruptedException {
        ActorSystem system =ActorSystem.create("prioritySystem");
        ActorRef actor =system.actorOf(Props.create(PriorityBasedActor.class).withDispatcher("prio-dispatcher"),"priorityActor");

        ActorRef myActor =
                system.actorOf(Props.create(PriorityBasedActor.class)
                        .withMailbox("prio-mailbox"));


        Thread.sleep(1000);
        actor.tell(new HighPriority(),ActorRef.noSender());
    }
}
