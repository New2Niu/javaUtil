package com.mapsortbyvalue;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapSortByValue {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String,Integer> m = new HashMap<String, Integer>();
		m.put("ssid1", 3);
		m.put("ssid2", 5);
		m.put("ssid3", 1);
		m.put("dd", 10);
		m.put("aa", 3);
		System.out.println(m);
		m = sortByValue(m, true);
		System.out.println(m);

	}
	
	 public static Map sortByValue(Map map, final boolean reverse) {
	        List list = new LinkedList(map.entrySet());
	        Collections.sort(list, new Comparator() {

	            public int compare(Object o1, Object o2) {
	                if (reverse) {
	                    return -((Comparable) ((Map.Entry) (o1)).getValue())
	                            .compareTo(((Map.Entry) (o2)).getValue());
	                }
	                return ((Comparable) ((Map.Entry) (o1)).getValue())
	                        .compareTo(((Map.Entry) (o2)).getValue());
	            }
	        });

	        Map result = new LinkedHashMap();
	        for (Iterator it = list.iterator(); it.hasNext();) {
	            Map.Entry entry = (Map.Entry) it.next();
	            result.put(entry.getKey(), entry.getValue());
	        }
	        return result;
	    }

}
