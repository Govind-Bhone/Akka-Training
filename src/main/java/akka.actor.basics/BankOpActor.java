package bank.operation.example;

import akka.actor.UntypedActor;
import akka.japi.Procedure;

/**
 * Created by govind.bhone on 1/3/2017.
 */
public class BankOpActor extends UntypedActor{

    private final int BALANCE =10000;

    Procedure<Object> withdraw = new Procedure<Object>() {
        @Override
        public void apply(Object message) {
            if (message.equals("bar")) {
                getSender().tell("I am already angry?", getSelf());
            } else if (message.equals("foo")) {
                getContext().become(deposite);
            }
        }
    };

    Procedure<Object> deposite = new Procedure<Object>() {
        @Override
        public void apply(Object message) {
            if (message.equals("bar")) {
                getSender().tell("I am already happy :-)", getSelf());
            } else if (message.equals("foo")) {
                getContext().become(withdraw);
            }
        }
    };

    @Override
    public void onReceive(Object message) throws Exception {
       Integer balance =(Integer)message;
        if(balance < BALANCE){
            getContext().become(withdraw);
        }else {
            getContext().become(deposite);
        }

    }
}
