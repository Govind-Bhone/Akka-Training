package examples.dispatcher.conf.CallingThreadDispatcher;

import examples.dispatcher.conf.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

import com.typesafe.config.ConfigFactory;

public class Example {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem _system = ActorSystem.create("callingThread-dispatcher",
				ConfigFactory.load().getConfig("MyDispatcherExample"));
		
		ActorRef actor = _system.actorOf(Props.create(MsgEchoActor.class)
				.withDispatcher("CallingThreadDispatcher").withRouter(
						new RoundRobinRouter(2)));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 10000; i++) {
			actor.tell(i,ActorRef.noSender());
		}

		_system.shutdown();

		}
		}
