package example.typed.actor;

/**
 * Created by govind.bhone on 1/4/2017.
 */

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.dispatch.OnSuccess;
import akka.japi.Creator;

public class TypedActorExample {

    public static void main(String args[]){
        ActorSystem system = ActorSystem.create("TypedActorDemo");

        MathService otherSquarer = TypedActor.get(system).typedActorOf(
                new TypedProps<MathServiceImpl>(MathService.class, new Creator<MathServiceImpl>() {

                    public MathServiceImpl create() {
                        return new MathServiceImpl();
                    }

                }), "name");

        otherSquarer.unblockingadd(10).onSuccess(new OnSuccess<Integer>() {
            public void onSuccess(Integer result) {
                System.out.println(result);
            }
        }, system.dispatcher());

        System.out.println(otherSquarer.blockingAdd(100));

    }



}
