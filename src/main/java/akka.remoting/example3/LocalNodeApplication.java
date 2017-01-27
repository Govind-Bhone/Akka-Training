package remote3.deployment;

import akka.actor.*;
import akka.remote.RemoteScope;
import akka.remote.routing.RemoteRouterConfig;
import akka.routing.FromConfig;
import akka.routing.RoundRobinPool;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import common.Ping;

/**
 * Hello world!
 */
public class LocalNodeApplication {
    public static void main(String[] args) throws Exception {
        ActorSystem _system = ActorSystem.create("LocalNodeApp", ConfigFactory
                .load().getConfig("LocalSys"));

        //ActorRef localActor = _system.actorOf(Props.create(LocalActor.class), "localActor");

        ActorRef remoteActor = _system.actorOf(Props.create(LocalActor.class), "remoteActor");

        Address addr = new Address("akka.tcp", "RemoteNodeApp", "192.168.56.1", 2553);

        ActorRef ref = _system.actorOf(Props.create(LocalActor.class).withDeploy(
                new Deploy(new RemoteScope(addr))));

        //Another way of remote deployment

//        Address[] addresses = {
//                new Address("akka.tcp", "remotesys", "otherhost", 1234),
//                AddressFromURIString.parse("akka.tcp://othersys@anotherhost:1234")};
//        ActorRef routerRemote = _system.actorOf(
//                new RemoteRouterConfig(new RoundRobinPool(5), addresses).props(
//                        Props.create(LocalActor.class)));


        Thread.sleep(5000);

        ref.tell("Hello World ", ActorRef.noSender());

        remoteActor.tell("Hello World ", ActorRef.noSender());

        ActorRef remotepool = _system.actorOf(Props.create(LocalActor.class).withRouter(new FromConfig()), "remotePool");
        for (int i = 0; i < 10; i++) {
            remotepool.tell("hello world for router", ActorRef.noSender());
        }
    }
}
