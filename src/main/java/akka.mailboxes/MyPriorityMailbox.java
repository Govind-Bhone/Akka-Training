package mailboxes.examples;

import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;
import com.typesafe.config.Config;

/**
 * Created by govind.bhone on 1/5/2017.
 */

class HighPriority{
    @Override
    public String toString(){return "HighPriority";}
}

class LowPriority{
    @Override
    public String toString(){return "LowPriority";}
}


public class MyPriorityMailbox extends UnboundedPriorityMailbox {

    public MyPriorityMailbox(ActorSystem.Settings settings, Config config) {
        super(new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                if (message instanceof HighPriority)
                    return 0; // 'highpriority messages should be treated first if possible
                else if (message instanceof LowPriority)
                    return 2; // 'lowpriority messages should be treated last if possible
                else if (message instanceof PoisonPill)
                    return 3; // PoisonPill when no other left
                else
                    return 1; // By default they go between high and low prio
            }
        });
    }
}

