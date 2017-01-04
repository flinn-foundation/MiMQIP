package flinn.util;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminNavigation {

	/*# This variable maps navigational elements to functionality.
	 If the first element of the array is "id", then this section has no 
	 sub-sections.
	 If not, then the subsections are there to be listed.
	 "sub-sub-sections" are assumed to be id-based for the purpose of this array.
	*/
	
	public static HashMap<String, ArrayList<String>> getNavigation(){
		HashMap<String, ArrayList<String>> navigation = new HashMap<String, ArrayList<String>>();
		ArrayList<String> n1 = new ArrayList<String>();
		ArrayList<String> n2 = new ArrayList<String>();
		ArrayList<String> n3 = new ArrayList<String>();
		ArrayList<String> n4 = new ArrayList<String>();
		ArrayList<String> n5 = new ArrayList<String>();
	
		n1.add("Users");
		n2.add("Facilities");
		n3.add("Lab Definitions");
		n4.add("Progress Note Tags");
		n5.add("Treatments");
		n5.add("Treatment Groups");
	
		navigation.put("Manage Users", n1);
		navigation.put("Manage Facilities", n2);
		navigation.put("Manage Lab Definitions", n3);
		navigation.put("Manage Progress Note Tags", n4);
		navigation.put("Manage Treatments", n5);
		
		return navigation;
	}

	
	public static HashMap<String, ArrayList<String>> getNavigationURL(){
	/*
	 This variable maps navigational elements to URLs.
	 "sub-sub-sections" are mapped to the same URLs as the subsection, but with
	 an id argument.
	*/	
		HashMap<String, ArrayList<String>> navigation_url = new HashMap<String, ArrayList<String>>();
	
		ArrayList<String> nu1 = new ArrayList<String>();
		ArrayList<String> nu2 = new ArrayList<String>();
		ArrayList<String> nu3 = new ArrayList<String>();
		ArrayList<String> nu4 = new ArrayList<String>();
		ArrayList<String> nu5 = new ArrayList<String>();
		
		nu1.add("user.jsp");
		nu2.add("facility.jsp");
		nu3.add("lab_definition.jsp");
		nu4.add("progress_note_tag.jsp");
		nu5.add("treatment.jsp");
		nu5.add("treatment_group.jsp");		
	
		navigation_url.put("Manage Users", nu1);
		navigation_url.put("Manage Facilities", nu2);
		navigation_url.put("Manage Lab Definitions", nu3);
		navigation_url.put("Manage Progress Note Tags", nu4);
		navigation_url.put("Manage Treatments", nu5);
		
		return navigation_url;
	}

	public static HashMap<String, ArrayList<String>> getNavigationRole(){
	/* This variable sets up role-based permissions to navigational items.  These
	 roles and permissions will persist throughout the interface, But are 
	 initially defined here.  (This may not be the only place that needs to 
	 change to change permissions, however.  Proceed with caution.)
	*/

		HashMap<String, ArrayList<String>> navigation_role = new HashMap<String, ArrayList<String>>();
		ArrayList<String> nr1 = new ArrayList<String>();
		ArrayList<String> nr2 = new ArrayList<String>();
		nr1.add("Admin");
		nr1.add("FacilityAdmin");
		nr2.add("Admin");
		
		navigation_role.put("Manage Users", nr1);
		navigation_role.put("Manage Facilities",  nr1);
		navigation_role.put("Manage Lab Definitions", nr2);
		navigation_role.put("Manage Progress Note Tags",  nr2);
		navigation_role.put("Manage Treatments",  nr2);
		
		return navigation_role;
	}
}
