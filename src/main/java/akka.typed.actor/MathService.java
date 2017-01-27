package example.typed.actor;

import scala.concurrent.Future;

/**
 * Created by govind.bhone on 1/4/2017.
 */
public interface MathService {
    Future<Integer> unblockingadd(int i); //non-blocking send-request-reply

    int blockingAdd(int i); //blocking send-request-reply
}
