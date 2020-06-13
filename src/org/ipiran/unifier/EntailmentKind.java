package org.ipiran.unifier;

import java.util.ArrayList;
import java.util.List;

public enum EntailmentKind {
	POSITIVE { 
		public String toString() { return "positive"; }
		public boolean toBoolean() { return true; }
	},
	NEGATIVE { 
		public String toString() { return "negative"; }
		public boolean toBoolean() { return false; }
	};
	
	public static String[] toStringArray(){
        List<String> items = new ArrayList<String>();
        for(EntailmentKind item: EntailmentKind.values()){
        	items.add(item.toString());
        }
        String[] itemsArray = new String[items.size()];  
        items.toArray(itemsArray);
        
        return itemsArray;
	}
	
	public static EntailmentKind stringToValue(String str){
		EntailmentKind value = null;
		
		for(EntailmentKind item: EntailmentKind.values()){
			if(item.toString().compareTo(str) == 0)
				return item;
		}
		
		return value;
	}
	
}
