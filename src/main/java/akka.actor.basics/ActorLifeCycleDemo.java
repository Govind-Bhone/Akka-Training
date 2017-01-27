package actor.basics; /**
 * Created by govind.bhone on 1/2/2017.
 */

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.Option;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.io.Serializable;

class ActorEvent implements Serializable {
    public final String who;

    public ActorEvent(String who) {
        this.who = who;
    }
}

class StopChild implements Serializable {
    public StopChild() {
    }
}

class ChildActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ActorEvent) {
            log.info("Got Message {}", ((ActorEvent) message).who);
        }
    }
}

class ActorLifeCycle extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    final ActorRef child = this.getContext().actorOf(Props.create(ChildActor.class), "child");

    {
        this.getContext().watch(child); // <-- the only call needed for registration
    }

    ActorRef lastSender = getContext().system().deadLetters();

    public ActorLifeCycle() {
        super();
    }

    @Override
    public ActorContext context() {
        return super.context();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ActorEvent) {
            log.info("Got Message {}", ((ActorEvent) message).who);
        } else if (message instanceof StopChild) {
            child.tell(PoisonPill.getInstance(), self());
        } else if (message instanceof Terminated) {
            final Terminated t = (Terminated) message;
            log.info("child is Terminated " + t.getActor().path());
            if (t.getActor() == child) {
                lastSender.tell("finished", getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    @Override
    public UntypedActorContext getContext() {
        return super.getContext();
    }

    @Override
    public ActorRef getSelf() {
        return super.getSelf();
    }

    @Override
    public ActorRef getSender() {
        return super.getSender();
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return super.supervisorStrategy();
    }

    @Override
    public void preStart() throws Exception {
        log.info("in Prestart Method");
        super.preStart();
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.info("in PreRestart Method");
        super.preRestart(reason, message);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log.info("in postRestart Method");
        super.postRestart(reason);
    }

    @Override
    public void unhandled(Object message) {
        super.unhandled(message);
        log.info("UnHandled Message" + message);

    }

    @Override
    public void aroundReceive(PartialFunction<Object, BoxedUnit> receive, Object msg) {
        super.aroundReceive(receive, msg);
    }

    @Override
    public void aroundPreStart() {
        log.info("in aroundPreStart Method");
        super.aroundPreStart();
    }

    @Override
    public void aroundPostStop() {
        log.info("in aroundPostStop Method");
        super.aroundPostStop();
    }

    @Override
    public void postStop() throws Exception {
        log.info("in PostStop Method");
        super.postStop();
    }
}

public class ActorLifeCycleDemo {
    public static void main(String args[]) throws InterruptedException {
        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef cycleRef = system.actorOf(Props.create(ActorLifeCycle.class), "actor-life-cycle");

        cycleRef.tell(new ActorEvent("Hello 1 "), cycleRef);
        cycleRef.tell(" String message", cycleRef);
        cycleRef.tell(new StopChild(), cycleRef);
        cycleRef.tell(new ActorEvent("Hello 2"), cycleRef);
        Thread.sleep(3000);
        cycleRef.tell(PoisonPill.getInstance(), null);
        Thread.sleep(3000);
        system.shutdown();
    }
}