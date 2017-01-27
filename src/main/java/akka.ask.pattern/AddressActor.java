package ask.pattern.example;

import akka.actor.ActorRef;
import ask.pattern.example.messages.Address;

import akka.actor.UntypedActor;

public class AddressActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Integer) {
			Integer userId = (Integer) message;
			// ideally we will get address for given user id
			Address address = new Address(userId, "Govind Bhone",
					"Kalyani Nagar", "Pune, India");
			getSender().tell(address,sender());
		}
	}
}