package ask.pattern.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import ask.pattern.example.messages.OrderHistory;
import scala.Function1;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.util.Try;

import java.util.concurrent.TimeUnit;

public class TestActorSystem {

	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem.create("FutureUsageExample");
		ActorRef processOrder = _system.actorOf(Props.create(
				ProcessOrderActor.class),"processor");
		final Timeout t = new Timeout(Duration.create(8, TimeUnit.SECONDS));

		OrderHistory result = (OrderHistory) Await.result(Patterns.ask(processOrder, Integer.valueOf(456), t), new FiniteDuration(8,TimeUnit.SECONDS));

		System.out.println("Back Result is "+result);
		Thread.sleep(50000);

		_system.shutdown();
	}

}
