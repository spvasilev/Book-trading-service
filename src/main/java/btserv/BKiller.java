package btserv;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.WakerBehaviour;

public class BKiller extends WakerBehaviour {
	
	private Agent agent;
	private Behaviour killinbeh;
	private long timeout;
	
	public BKiller(Agent agent, long timeout,  Behaviour killinbeh) {
		super(agent, timeout);
		this.agent = agent;
		this.timeout = timeout;
		this.killinbeh = killinbeh;
	}
	
	@Override
	protected void onWake() {
		super.onWake();
		agent.removeBehaviour(killinbeh);
	}
}

