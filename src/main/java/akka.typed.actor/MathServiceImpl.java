package example.typed.actor;

import akka.dispatch.Futures;
import scala.concurrent.Future;

/**
 * Created by govind.bhone on 1/4/2017.
 */
class MathServiceImpl implements MathService {

    public Future<Integer> unblockingadd(int i) {
        return Futures.successful(i + 10);
    }

    public int blockingAdd(int i) {
        return i + 10;
    }

}