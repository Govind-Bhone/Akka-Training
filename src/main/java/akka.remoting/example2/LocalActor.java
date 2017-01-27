package remote2.akka.example;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import common.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class LocalActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	Timeout timeout = new Timeout(FiniteDuration.apply(5, TimeUnit.SECONDS));

	ActorRef remoteActor;

	@Override
	public void preStart() {
		//Get a reference to the remote actor
		remoteActor = getContext().actorFor(
				"akka.tcp://RemoteNodeApp@127.0.0.1:2553/user/remoteActor");
	}

	@Override
	public void onReceive(Object message) throws Exception {
	 if( message instanceof Ping){
		 log.info("Message received  -> {}", message);
		 Pong pong = new Pong();
		 remoteActor.tell(pong,self());
		 log.info("Message Sending  -> {}", pong);
	 }else if (message instanceof Pong){
		 log.info("Message received  -> {}", message);
		 Ping ping = new Ping();
		 remoteActor.tell(ping,self());
		 log.info("Message Sending  -> {}", ping);

	 }

	}
}
