package remote.akka.example;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class RemoteNodeApplication /*implements Bootable*/ {
	public static void main(String args[]){
		final ActorSystem system = ActorSystem.create("RemoteNodeApp", ConfigFactory
				.load().getConfig("RemoteSys"));
		system.actorOf(Props.create(RemoteActor.class), "remoteActor");
	}

}
