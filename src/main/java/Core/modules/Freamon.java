package Core.modules;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import util.Config;

/**
 * Sends messages to the Freamon MonitorMasterActor
 */
public class Freamon {

    private static final ActorRef freamonMaster = new UntypedActor() {
        @Override
        public void onReceive(Object message) throws Exception {
            throw new RuntimeException("Received unexpected message " + message);
        }
    }.context().actorFor(Config.getInstance().getConfigItem("freamonMasterAddress"));

    public static class OnStart {
        public final String jobID;
        public final long startTime;

        public OnStart(String jobID, long startTime) {
            this.jobID = jobID;
            this.startTime = startTime;
        }
    }

    public static class OnStop {
        public final String jobID;
        public final long stopTime;

        public OnStop(String jobID, long stopTime) {
            this.jobID = jobID;
            this.stopTime = stopTime;
        }
    }

    public static void onSubmit(String jobID) {
    }

    public static void onStart(String jobID, long startTime) {
        freamonMaster.tell(new OnStart(jobID, startTime), null);
    }

    public static void onStop(String jobID, long stopTime) {
        freamonMaster.tell(new OnStop(jobID, stopTime), null);
    }
}
