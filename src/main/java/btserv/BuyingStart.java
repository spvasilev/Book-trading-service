package btserv;

import java.util.ArrayList;
import java.util.List;

import btserv.BKiller;
import btserv.Book;
import btserv.ResponseWaiting;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class BuyingStart extends OneShotBehaviour {

	private Agent agent;
	private List <Book> booklist;
	
	public BuyingStart (Agent agent, DataStore ds) {
		this.agent = agent;
		booklist = (List <Book>) ds.get("BookList");
		setDataStore(ds);
	}
	
	@Override
	public void action() {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		dfd.addServices(sd);
		DFAgentDescription[] result = null;
		
		try {
			result = DFService.search(myAgent, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setContent(booklist.get(0).getTitle()+"");
		
		List <AID> sellerAgents = new ArrayList<AID>();
		sellerAgents.add(new AID("Seller1", false));
		sellerAgents.add(new AID("Seller2", false));
		sellerAgents.add(new AID("Seller3", false));
		
		System.out.println(agent.getLocalName()+":" + " I've sent request! I want to buy " + 
		booklist.get(0).getTitle());
		
		System.out.println(agent.getLocalName() + 
				": count of found sellers in Yellow Pages = "+result.length);

		getDataStore().put("receiverList", sellerAgents);
		for (AID rec:sellerAgents) {
			request.addReceiver(rec);
		}
		request.setProtocol("bookbuying");
		agent.send(request);
		ResponseWaiting behaviourToKill = new ResponseWaiting(agent, getDataStore());
		agent.addBehaviour(behaviourToKill);
		agent.addBehaviour(new BKiller(agent, 1000, behaviourToKill));
	}
	
}
