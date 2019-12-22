package btserv;

import java.util.ArrayList;
import java.util.List;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgentSeller extends Agent {
	private List<Book> booklist;
	private Agent myAgent = this;

	@Override
    protected void setup() {
		super.setup();
		booklist = new ArrayList<Book>();
		createSettingForSeller(booklist);
		DataStore ds = new DataStore();
		ds.put("BookList", booklist);
		
		addBehaviour(new RequestWaiting(this, ds));
	}

	private void createSettingForSeller (List<Book> booklist) {
		
		DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("book-selling");
        sd.setName(this.getLocalName()+"-book-seller");
        dfd.addServices(sd);
        System.out.println(this.getLocalName() + " is ready");

		DFAgentDescription[] foundService = null;
          	try {
              DFService.register(myAgent,dfd);
            }catch (FIPAException e) {
              e.printStackTrace();
            }
		
		if (this.getLocalName().equals("Seller1")) {
			booklist.add(new Book(BookTitle.CrimeAndPunishment,290));
			booklist.add(new Book(BookTitle.LeComteDeMonteCristo,810));
		}
		else if (this.getLocalName().equals("Seller2")) {
		
			booklist.add(new Book(BookTitle.LeComteDeMonteCristo,850));
			booklist.add(new Book(BookTitle.WarAndPeace,1350));
		} 
		else if (this.getLocalName().equals("Seller3")) {
			booklist.add(new Book(BookTitle.TheGodfather,1200));
			booklist.add(new Book(BookTitle.CrimeAndPunishment,700));
		}
	}
	//ubrat' s jeltih stranic
	
	/*protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}*/
}

