package btserv;

import btserv.BKiller;
import btserv.Book;
import btserv.BookTitle;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.List;

public class RequestWaiting extends Behaviour {

	 private Agent agent;
	    private boolean msgArried = false;
	    private List<Book> bookList;

	    public RequestWaiting(Agent agent, DataStore ds){
	        super();
	        setDataStore(ds);
	        this.agent = agent;
	        bookList = (List<Book>) getDataStore().get("BookList");

	    }
	    @Override
	    public void action() {
	        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
	                MessageTemplate.MatchProtocol("bookbuying"));
	        ACLMessage request = agent.receive(mt);

	        if (request!= null){
	            ACLMessage answer = request.createReply();
	            msgArried = true;
	            BookTitle requestedBook = BookTitle.valueOf(request.getContent());

	            double offeredPrice = 0;
	            for (Book book:bookList){
	                if (book.getTitle() == requestedBook){
	                    offeredPrice = book.getPrice();
	                }
	            }
	            if (offeredPrice > 0) {
	                answer.setContent(offeredPrice + "");
	                answer.setPerformative(ACLMessage.INFORM);
	                System.out.println(agent.getLocalName() + 
	                        ": " + "I have this book. My price for it " + offeredPrice );

	            }
	            else{
	                System.out.println(agent.getLocalName() +  ": "
	                        + "Request is not for me. ");
	                answer.setPerformative(ACLMessage.CANCEL);
	            }
	            agent.send(answer);
	        }
	        else{
	            block();
	        }

	    }
	    @Override
	    public boolean done() {

	        return msgArried;
	    }
	    @Override
	    public int onEnd(){
	        ProposalWaiting behaviour = new  ProposalWaiting(agent, getDataStore());
	        agent.addBehaviour(behaviour);
	        agent.addBehaviour(new BKiller(agent, 5000, behaviour));
	        return super.onEnd();
	    }
	}
