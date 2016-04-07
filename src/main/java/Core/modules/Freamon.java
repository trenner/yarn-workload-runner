package Core.modules;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;
import util.Config;

/**
 * Sends messages to the Freamon MonitorMasterActor
 */
public class Freamon extends UntypedActor {

    private static final com.typesafe.config.Config config = ConfigFactory
        .parseString("akka.actor.provider = \"akka.remote.RemoteActorRefProvider\"\n" +
        "akka.remote.enabled-transport = [\"akka.remote.netty.NettyRemoteTransport\"]")
        .withFallback(ConfigFactory.load());
    private static final ActorSystem actorSystem = ActorSystem.create("yarnWorkloadRunnerSystem", config);
    private static final ActorRef instance = actorSystem.actorOf(Props.create(Freamon.class), "freamonSender");

    private final ActorSelection freamonMaster = getContext().actorSelection(Config.getInstance().getConfigItem("freamonMasterAddress"));

    @Override
    public void onReceive(Object message) throws Exception {
        freamonMaster.forward(message, getContext());
    }

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
        instance.tell(new OnStart(jobID, startTime), instance);
    }

    public static void onStop(String jobID, long stopTime) {
        instance.tell(new OnStop(jobID, stopTime), instance);
    }
}
