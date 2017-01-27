package akka.supervision.policies.example2;

import akka.actor.ActorRef;
import akka.supervision.policies.example2.MyActorSystem2.Result;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.Option;

public class WorkerActor2 extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private int state = 0;

	@Override
	public void preStart() {
		log.info("Starting WorkerActor2 instance hashcode # {}",
				this.hashCode());
	}

	public void onReceive(Object o) throws Exception {
		if (o == null) {
			throw new NullPointerException("Null Value Passed");
		} else if (o instanceof Integer) {
			Integer value = (Integer) o;
			if (value <= 0) {
				throw new ArithmeticException("Number equal or less than zero");
			} else
				state = value;
		} else if (o instanceof Result) {
			getSender().tell(state, ActorRef.noSender());
		} else {
			throw new IllegalArgumentException("Wrong Argument");
		}
	}

	@Override
	public void preRestart(Throwable reason, Option<Object> message) throws Exception {
		log.info("in PreRestart Method"+self().path().name());
		super.preRestart(reason, message);
	}

	@Override
	public void postRestart(Throwable reason) throws Exception {
		log.info("in postRestart Method"+self().path().name());
		super.postRestart(reason);
	}

	@Override
	public void postStop() {
		log.info("Stopping WorkerActor2 instance hashcode # {}",
				this.hashCode());

	}
}
