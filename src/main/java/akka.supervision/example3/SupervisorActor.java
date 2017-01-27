package akka.supervision.policies.example3;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;


public class SupervisorActor extends UntypedActor {
	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	ActorRef workerActor = getContext().actorOf(Props.create(WorkerActor.class),
			"workerActor");

	ActorRef monitor = getContext().system().actorOf(
			Props.create(MonitorActor.class), "monitorActor");

	@Override
	public void preStart() {
		monitor.tell(new RegisterWorker(workerActor, self()),ActorRef.noSender());
	}

	private static SupervisorStrategy strategy = new OneForOneStrategy(10,
			Duration.create("10 second"), new Function<Throwable, Directive>() {
				public Directive apply(Throwable t) {
					if (t instanceof ArithmeticException) {
						return resume();
					} else if (t instanceof NullPointerException) {
						return restart();
					} else if (t instanceof IllegalArgumentException) {
						return stop();
					} else {
						return escalate();
					}
				}
			});

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	public void onReceive(Object o) throws Exception {
		if (o instanceof Result) {
			workerActor.tell(o, getSender());
		} else if (o instanceof DeadWorker) {
			log.info("Got a DeadWorker message, restarting the worker");
			workerActor = getContext().actorOf(Props.create(WorkerActor.class),
					"workerActor");
		} else
			workerActor.tell(o,ActorRef.noSender());
	}

	public ActorRef getWorker() {
		return workerActor;
	}
}
