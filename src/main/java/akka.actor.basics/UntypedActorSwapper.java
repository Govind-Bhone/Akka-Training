package actor.basics;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

/**
 * Created by govind.bhone on 1/2/2017.
 */

public class UntypedActorSwapper {

    public static class Swap {
        public static Swap SWAP = new Swap();

        private Swap() {
        }
    }

    public static class UnSwap {
        public static UnSwap UNSWAP = new UnSwap();

        private UnSwap() {
        }
    }

    public static class Swapper extends UntypedActor {
        LoggingAdapter log = Logging.getLogger(getContext().system(), this);

        public void onReceive(Object message) {
            if (message == Swap.SWAP) {
                log.info("Hi");
                getContext().become(new Procedure<Object>() {
                    @Override
                    public void apply(Object message) {
                        log.info("Ho");
                        getContext().unbecome(); // resets the latest 'become'
                    }
                }, false); // this signals stacking of the new behavior
            } else if (message == UnSwap.UNSWAP) {
                log.info("Hi");
                getContext().become(new Procedure<Object>() {
                    @Override
                    public void apply(Object message) {
                        log.info("Ho");
                         // resets the latest 'become'
                    }
                }, false); // this signals stacking of the new behavior
            } else {
                unhandled(message);
            }
        }
    }

    public static void main(String... args) {
        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef swap = system.actorOf(Props.create(Swapper.class));
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Hi
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Ho
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Hi
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Ho
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Hi
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Ho

        swap.tell(UnSwap.UNSWAP, ActorRef.noSender()); // logs Hi
        swap.tell(UnSwap.UNSWAP, ActorRef.noSender()); // logs Ho
        swap.tell(UnSwap.UNSWAP, ActorRef.noSender()); // logs Hi
        swap.tell(UnSwap.UNSWAP, ActorRef.noSender()); // logs Ho

    }

}