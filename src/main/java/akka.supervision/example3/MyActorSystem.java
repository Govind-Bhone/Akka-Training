package akka.supervision.policies.example3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class MyActorSystem {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ActorSystem system = ActorSystem.create("faultTolerance");

		ActorRef supervisor = system.actorOf(Props.create(SupervisorActor.class),
				"supervisor");

		ActorRef supervisor1 = system.actorOf(Props.create(SupervisorActor.class),
				"supervisor");

		supervisor.tell(Integer.valueOf(10),ActorRef.noSender());
		supervisor.tell("10",ActorRef.noSender());

		Thread.sleep(5000);

		supervisor.tell(Integer.valueOf(10),ActorRef.noSender());

		system.shutdown();
	}

}
