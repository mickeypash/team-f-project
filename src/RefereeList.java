import java.util.ArrayList;

/**
 * Team Foxtrot - JavaBall Referees
 * Aggregation class for all available and registered referees
 * <p>
 * University of Glasgow
 * MSc/PGDip Information Technology/Software Development
 * Team Project 2014/15
 *
 * @author  Miroslav Pashov, 1005139P
 * @author  Andrew Lowson, 0800685L
 * @author  Marco Cook, 2152599C
 * @author  Raoul Rothfeld, 2164502R
 * 
 * @version 1.1
 * @since   21-01-2015
 */

public class RefereeList {
	/** maximum number of listed referees */
	private final static int MAX_REFEREES = 12;
	
	/** list of all registered referees */
	private final ArrayList<Referee> listedReferees;

	/** Default constructor */
	public RefereeList() {
		listedReferees = new ArrayList<Referee>();
	}
	
	/**
	 * Adds a referee to the list of registered referees and makes him/her
	 * available for match allocation.
	 * @param ref the Referee Object to be registered
	 * @return indication of successful referee registration
	 */
	public boolean add(Referee ref) {
		// test if another referee may be added
		if (listedReferees.size() <= MAX_REFEREES)
			return listedReferees.add(ref);
		else
			return false;
	}
	
	/**
	 * Removes a referee from the lost of registered referees
	 * @param ref the Referee Object to be removed from the list
	 * @return indication of successful referee removal
	 */
	public boolean removeReferee(Referee ref) {   
		return listedReferees.remove(ref); // FIXME trimToSize()?
	}
	
	
	/**
	 * 
	 * @param ID
	 * @return
	 */
	public Referee getReferee(String id) {
		// find referee with desired ID
		for (Referee ref : listedReferees) {
			if (ref.getID().equals(id))
				return ref;
                }
                return null;
        }
        
	/**
	 * 
	 * @param location
	 * @param type
	 * @return
	 */
	public Referee[] getSuitableReferees(Match.Location location, String type) {
		int n = 0, m = 0;
		
		// if senior dann min lvl 2
		// 
		
		Referee [] suitableReferees = {listedReferees.get(n), listedReferees.get(m)};
		return suitableReferees;
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	public ArrayList<Referee> getReferees(int level) {
		// list to hold referees after filtering
		ArrayList<Referee> filteredReferees = new ArrayList<Referee>();
		
		// add all referees with the desired qualification level
		for (Referee ref : listedReferees) {
			if (ref.getQualificationLevel() == level)
				filteredReferees.add(ref);
		}
		
		return filteredReferees;
	}
	
	// TODO get refs for LOCATIONs
	
        public int getRefereeListSize()
        {
            return listedReferees.size();
        }
        
	/**
	 * 
	 * @param name
	 * @return
	 */
	public ArrayList<Referee> getReferees(String name) {
		// list to hold referees after filtering
		ArrayList<Referee> filteredReferees = new ArrayList<Referee>();
		
		// add all referees with either the desired fore- or surname
		for (Referee ref : listedReferees) {
			if (ref.getForename().equals(name) || ref.getSurname().equals(name))
				filteredReferees.add(ref);
		}
		
		return filteredReferees;
	}
	
	/**
	 * 
	 * @param fname
	 * @param sname
	 * @return
	 */
	public ArrayList<Referee> getReferees(String fname, String sname) {
		// list to hold referees after filtering
		ArrayList<Referee> filteredReferees = new ArrayList<Referee>();
		
		// add all referees with either the desired fore- and surname
		for (Referee ref : listedReferees) {
			if (ref.getForename().equals(fname) && ref.getSurname().equals(sname))
				filteredReferees.add(ref);
		}
		
		return filteredReferees;
	}
        
        public void debug()
        {
            int counter = 1;
            for (Referee ref : listedReferees)
            {
                String forename = ref.getForename();
                String info = String.format("Info for Referee %d. %s",counter,forename);
                System.err.println(info);
                counter++;
                
            }
            
            System.err.println("List size: "+listedReferees.size());
            System.err.println("Adding Referee");
            
            String uniqueID = "AL1";
            String forename = "Andrew";
            String surname  = "Lowson";

            String homeLocation = "North";

            int alloc = 12;
            //convert travel locations to boolean
            String travel = "YYN";
            String qual = "IJB3";
            
            Referee referee = new Referee(uniqueID, forename, surname,qual,alloc, homeLocation, travel);
            listedReferees.add(referee);
            Referee testRef = listedReferees.get(6);
            
            String info = String.format("Info for Referee %d. %s",counter,testRef.getForename());
            System.err.println(info);
            
            System.err.println();
            System.err.println("List size: "+listedReferees.size());
            System.err.println("Removing Referee");
            listedReferees.remove(0);
            System.err.println("List size now: "+listedReferees.size());
        }
} 