package ask.pattern.example;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import akka.dispatch.OnComplete;
import ask.pattern.example.messages.Address;
import ask.pattern.example.messages.Order;
import ask.pattern.example.messages.OrderHistory;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.util.Timeout;

public class ProcessOrderActor extends UntypedActor {

    final Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    ActorRef orderActor = getContext().actorOf(Props.create(OrderActor.class));
    ActorRef addressActor = getContext().actorOf(Props.create(AddressActor.class));
    ActorRef orderAggregateActor = getContext().actorOf(
            Props.create(OrderAggregateActor.class));

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Integer) {
            Integer userId = (Integer) message;
            final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();

            final ActorRef senderState=sender();
            // make concurrent calls to actors
            futures.add(ask(orderActor, userId, t));
            futures.add(ask(addressActor, userId, t));

            // set the sequence in which the reply are expected
            final Future<Iterable<Object>> aggregate = Futures.sequence(
                    futures, getContext().system().dispatcher());

            // once the replies comes back, we loop through the Iterable to
            // get the replies in same order
            // final Future<OrderHistory> aggResult =

            final Future<OrderHistory> aggResult = aggregate.map(
                    new Mapper<Iterable<Object>, OrderHistory>() {
                        public OrderHistory apply(Iterable<Object> coll) {
                            final Iterator<Object> it = coll.iterator();
                            final Order order = (Order) it.next();
                            final Address address = (Address) it.next();
                            return new OrderHistory(order, address);
                        }
                    }, getContext().system().dispatcher());
            // aggregated result is piped to another actor


            final ExecutionContext ec = getContext().dispatcher();


    /*        aggResult.onComplete(new OnComplete<OrderHistory>() {

                public void onComplete(Throwable failure, OrderHistory result) {
                    if (failure == null) {
                        System.out.println(sender().path());

                       // sender().tell(result, self());
                        senderState.tell(result, self());
                    } else {
                        try {
                            throw new TimeoutException("timeout");
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, ec);
*/
            /**
             * Register an onComplete callback on this [[scala.concurrent.Future]] to send
             * the result to the given [[akka.actor.ActorRef]] or [[akka.actor.ActorSelection]].
             * Returns the original Future to allow method chaining.
             * If the future was completed with failure it is sent as a [[akka.actor.Status.Failure]]
             * to the recipient.
             *
             * <b>Recommended usage example:</b>
             *
             * {{{
             *   final Future<Object> f = Patterns.ask(worker, request, timeout);
             *   // apply some transformation (i.e. enrich with request info)
             *   final Future<Object> transformed = f.map(new akka.japi.Function<Object, Object>() { ... });
             *   // send it on to the next stage
             *   Patterns.pipe(transformed).to(nextActor);
             * }}}
             */

            System.out.println("sneder is " + sender().path());
          pipe(aggResult, getContext().system().dispatcher()).to(
                 sender());
        }
    }
}
