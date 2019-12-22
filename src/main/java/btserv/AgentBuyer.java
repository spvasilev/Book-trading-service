package btserv;

import java.util.ArrayList;
import java.util.List;
import jade.core.Agent;
import jade.core.behaviours.DataStore;

public class AgentBuyer extends Agent {
	private List <Book> buyingbook;

	@Override
	protected void setup(){
		System.out.println("Let's buy some books!");
		super.setup();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buyingbook = new ArrayList <Book> (); 
		buyingbook.add(new Book(BookTitle.LeComteDeMonteCristo));
		buyingbook.add(new Book(BookTitle.TheGodfather));
		buyingbook.add(new Book(BookTitle.CrimeAndPunishment));
		buyingbook.add(new Book(BookTitle.WarAndPeace));
			
		DataStore ds = new DataStore();
		ds.put("BookList", buyingbook);
			
		addBehaviour(new BuyingStart(this, ds));

	}

}

