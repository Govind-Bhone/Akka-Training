package remote2.akka.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import common.Ping;

/**
 * Hello world!
 * 
 */
public class LocalNodeApplication {
	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem.create("LocalNodeApp",ConfigFactory
				.load().getConfig("LocalSys"));
		ActorRef localActor = _system.actorOf(Props.create(LocalActor.class));
		localActor.tell(new Ping(),ActorRef.noSender());

		//Thread.sleep(5000);
		//_system.shutdown();
	}
}
