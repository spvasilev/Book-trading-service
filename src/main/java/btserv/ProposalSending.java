package btserv;

import java.util.List;

import btserv.Book;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProposalSending extends Behaviour {

	 private Agent agent;
	    private AID bestSeller; //the seller agent who provides the best offer
	    private double bestPrice; //the best offered price
	    private List<Book> bookList;
	    private List<AID> receiversList;
	    private boolean answerReceived = false;
	    private int counter; // the counter of replies from seller agents


	    public ProposalSending(Agent agent, DataStore ds, AID bestSeller, double bestPrice){
	        this.bestSeller = bestSeller;
	        this.bestPrice = bestPrice;
	        this.agent = agent;
	        this.receiversList = (List<AID>) ds.get("receiverList");
	        this.bookList = (List<Book>) ds.get("BookList");
	        counter = receiversList.size();
	        setDataStore(ds);
	    }
	    @Override
	    public void onStart(){
	        super.onStart();
	        //send propose to winner and refuse to other
	        ACLMessage proposal = new ACLMessage(ACLMessage.PROPOSE);
	        proposal.addReceiver(bestSeller);
	        proposal.setContent(bestPrice+"");
	        proposal.setProtocol("confirm");
	        agent.send(proposal);
	        
	        ACLMessage refuse = new ACLMessage(ACLMessage.REFUSE);
	        refuse.setProtocol("confirm");
	        for (AID rec: receiversList){
//	            if (rec!=bestSeller){
	            if (!rec.equals(bestSeller)){ 		//skorrektirovano na pare
	                refuse.addReceiver(rec);
	            }
	        }
	        agent.send(refuse);
	    }

	    @Override
	    public void action() {
	        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchProtocol("confirm"),
	                MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
	                        MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL)));
	        ACLMessage answer = agent.receive(mt);
	        if (answer != null) {
	        	//infa o prieme prdelozheniya
	            if (answer.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {

	                System.out.println(agent.getLocalName() +
	                        ":" + " I've got a confirm from " + answer.getSender().getLocalName()
	                        + ". I've bought " + bookList.get(0).getTitle() + " for " + bestPrice);
	                //nuzhno udalit'etot nulevoi element iz massiva
//	                bookList.remove(0);
//	                getDataStore().put("bookList", bookList);
	                counter--;
	             //infa ob otklonenii predlozheniya
	            } else if (answer.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
	                System.out.println(agent.getLocalName() +
	                        ":" + " I've got a disconfirm from " + answer.getSender().getLocalName());
	                counter--;

	            }
	        }
	        else {
	            block();
	        }
	        if (counter == 0){
	            answerReceived = true;
	        }

	    }

	    @Override
	    public boolean done() {

	        return answerReceived;
	    }

	    @Override
	    public int onEnd() {
	        if (bookList.size() != 0) {    //added
	            Book book = bookList.get(0);
	            bookList.remove(0);
	        }
	        if (bookList.size() == 0){
	            System.out.println("Mission copmpleted! " +  agent.getLocalName() +  ":" +
	                     "I've finished bookbuying!");   //esli vse knigi kuplenbl
	        }
	        else {
	            System.out.println(agent.getLocalName() +
	                    ":" +  "there are some books in my list. Let's get it!");
	            System.out.println("_____________________________________________________________________________________________");
	            try{
	                Thread.sleep(1000);
	            }catch (InterruptedException e){
	                e.printStackTrace();
	            }
	            agent.addBehaviour(new BuyingStart(agent, getDataStore()));
	        }
	        return super.onEnd();
	    }
	}
