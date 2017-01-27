package routers.examples.simplerouter;

import akka.actor.*;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by govind.bhone on 1/5/2017.
 */

final class Work implements Serializable {
    private static final long serialVersionUID = 1L;
    public final int payload;

    public Work(int payload) {
        this.payload = payload;
    }
}

class Worker extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("Received message from Master to worker " + self().path().name());
    }
}

class Master extends UntypedActor {

    Router router;

    {
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < 5; i++) {
            ActorRef r = getContext().actorOf(Props.create(Worker.class));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    public void onReceive(Object msg) {
        if (msg instanceof Work) {
            router.route(msg, getSender());
        } else if (msg instanceof Terminated) {
            System.out.println("One of the worker get terminated ,removing from routee list" + self().path().name());
            router = router.removeRoutee(((Terminated) msg).actor());
            ActorRef r = getContext().actorOf(Props.create(Worker.class));
            getContext().watch(r);
            router = router.addRoutee(new ActorRefRoutee(r));
        }
    }
}


public class SimpleRouterExample {
    public static void main(String args[]) {
        ActorSystem system =ActorSystem.create("round-robin-router-system");
        ActorRef actor =system.actorOf(Props.create(Master.class),"master");

        for(int i=0;i<=10000;i++){
            actor.tell(new Work(i),ActorRef.noSender());
        }


    }
}
