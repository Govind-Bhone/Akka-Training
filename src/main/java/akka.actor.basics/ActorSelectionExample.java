package actor.basics;

import akka.actor.*;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

/**
 * Created by govind.bhone on 1/2/2017.
 */
import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Identify;
import akka.actor.Props;

class TestAskActor extends UntypedActor{

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            System.out.println("message received is "+message);
        }
    }
}

public class ActorSelectionExample {

    public static void main(String[] args) throws Exception {
        ActorSystem sys = ActorSystem.apply("test");
        ActorRef testActor=sys.actorOf(Props.create(TestAskActor.class), "mytest");

        ActorSelection sel = sys.actorSelection("/user/mytest");

        sel.tell("Hello",ActorRef.noSender());
        //testActor.tell(PoisonPill.getInstance(),ActorRef.noSender());
        Thread.sleep(3000);


        Timeout t = new Timeout(5, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(sel);
        Future<Object> fut = asker.ask(new Identify(2), t);
        ActorIdentity ident = (ActorIdentity) Await.result(fut, t.duration());
        ActorRef ref = ident.getRef();
        ref.tell("Hello",ActorRef.noSender());
        System.out.println(ref == null);

    }
}
