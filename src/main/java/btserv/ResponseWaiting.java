package btserv;

import java.util.List;

import btserv.BKiller;
import btserv.Book;
import btserv.ProposalSending;
import btserv.BuyingStart;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ResponseWaiting extends Behaviour {

	 private Agent agent;
	    private double bestPrice = 10000;
	    private AID bestSeller = null;
	    private List<AID> receivers;
	    private List<Book> bookList;
	    private int counter;
	    private boolean behaviourDone = false;

	    public ResponseWaiting (Agent agent, DataStore ds){
	        super();
	        this.agent = agent;
	        setDataStore(ds);
	        this.receivers = (List<AID>) ds.get("receiverList");
	        this.bookList = (List<Book>) ds.get("BookList");
	        counter = receivers.size();
	    }

	    @Override
	    public void action() {
	        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchProtocol("bookbuying"),
	                MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CANCEL),
	                        MessageTemplate.MatchPerformative(ACLMessage.INFORM)));
	        ACLMessage response = agent.receive(mt);

	        if (response != null) {
	            if (response.getPerformative() == ACLMessage.INFORM) {
	            	counter--;
	                System.out.println(agent.getLocalName() +  ":"
	                + " I've received the price " + response.getContent()  
	                + " from " +  response.getSender().getLocalName());
	                double price = Double.parseDouble(response.getContent());

	                if (price < bestPrice) {
	                    bestSeller = response.getSender();
	                    bestPrice = price;
	                }
	            } else if (response.getPerformative() == ACLMessage.CANCEL){
	            	counter--;
	                System.out.println(agent.getLocalName() +  ":"  
	                        + response.getSender().getLocalName() + " hasn't got "   
	                        + bookList.get(0).getTitle());

	            }
	        } else {
	            block();
	        }
	        if (counter == 0) {
	            behaviourDone = true;
	        }
	    }

	    @Override
	    public boolean done() {

	        return behaviourDone;
	    }

	    @Override
	    public int onEnd() {
	        if (behaviourDone && bestSeller != null) {
	            System.out.println("Winner is "  + bestSeller.getLocalName() + "!");
	            ProposalSending behaviour = new ProposalSending(agent, getDataStore(), bestSeller, bestPrice);
	            agent.addBehaviour(behaviour);
	            agent.addBehaviour(new BKiller(agent,2000, behaviour));
	        } else {
	            System.out.println( " Seller " + agent.getLocalName() + "not found!" );
	            agent.addBehaviour(new WakerBehaviour(agent, 1000) {
	                @Override
	                protected void onWake() {
	                    super.onWake();
	                    agent.addBehaviour(new BuyingStart(agent, getDataStore()));
	                }
	            });
	        }
	        return super.onEnd();
	    }
	}