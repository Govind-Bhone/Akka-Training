package examples.dispatcher.conf.BalancingDispatcher;

import examples.dispatcher.conf.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

import com.typesafe.config.ConfigFactory;

public class Example2 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem _system = ActorSystem.create("balancing-dispatcher",
				ConfigFactory.load().getConfig("MyDispatcherExample"));
		
		ActorRef actor = _system.actorOf(Props.create(MsgEchoActor.class)
				.withDispatcher("balancingDispatcher1").withRouter(
						new RoundRobinRouter(5)));

		for (int i = 0; i < 25; i++) {
			actor.tell(i,ActorRef.noSender());
		}

		_system.shutdown();

	}
}
