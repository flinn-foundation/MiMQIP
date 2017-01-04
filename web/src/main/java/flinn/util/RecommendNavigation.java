package flinn.util;

import java.util.ArrayList;
import java.util.HashMap;

public class RecommendNavigation {
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
	
		n1.add("Settings");
		n2.add("Rules");
		n3.add("Messages");
		n4.add("Export");
		n5.add("Diagnoses");
	
		navigation.put("Manage Settings", n1);
		navigation.put("Manage Rules", n2);
		navigation.put("Manage Messages", n3);
		navigation.put("Export Data", n4);
		navigation.put("Manage Diagnoses", n5);
		
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
		
		nu1.add("setting.jsp");
		nu2.add("rule.jsp");
		nu3.add("message.jsp");
		nu4.add("export.jsp");
		nu5.add("diagnosis.jsp");
	
		navigation_url.put("Manage Settings", nu1);
		navigation_url.put("Manage Rules", nu2);
		navigation_url.put("Manage Messages", nu3);
		navigation_url.put("Export Data", nu4);
		navigation_url.put("Manage Diagnoses", nu5);
		
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
		ArrayList<String> nr3 = new ArrayList<String>();
		ArrayList<String> nr4 = new ArrayList<String>();
		ArrayList<String> nr5 = new ArrayList<String>();

		nr1.add("Admin");
		nr2.add("Admin");
		nr3.add("Admin");
		nr4.add("Admin");
		nr5.add("Admin");
		
		navigation_role.put("Manage Settings",  nr1);
		navigation_role.put("Manage Rules",  nr2);
		navigation_role.put("Manage Messages",  nr3);
		navigation_role.put("Export Data",  nr4);
		navigation_role.put("Manage Diagnoses", nr5);
		
		return navigation_role;
	}
}
