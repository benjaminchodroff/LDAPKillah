/**
 * Benjamin Chodroff
 * benjamin.chodroff@gmail.com
 * 
 * A utility for load testing ldap servers
 */
package com.benchodroff.ldapkillah;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author benjamin.chodroff
 * 
 */
public class LDAPKillah {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String providerURL = args[0];
		System.out.println("providerURL: "+providerURL);
		String ldapbase = args[1];
		String ldapUser = args[2];
		System.out.println("ldapUser: "+ldapUser);
		String ldapPass = args[3];
		System.out.println("ldapPass: "+ldapPass);
		int numThreads = Integer.parseInt(args[4]);
		System.out.println("numThreads: "+numThreads);
		int increment = Integer.parseInt(args[5]);
		System.out.println("increment: "+increment);
		if (increment>=500){
			System.err.println("Warning! You have possibly set an incrementer higher than your ldap may support in search results (500)");
		}
		int maxUsers = Integer.parseInt(args[6]);
		System.out.println("maxUsers: "+maxUsers);
		int timeLimit = Integer.parseInt(args[7]);
		System.out.println("timeLimit: "+timeLimit);
		if (timeLimit<5000){
			System.err.println("Warning! You have a set a search time limit lower than your ldap may need (less than 5 seconds)");
		}
		int delayTime = Integer.parseInt(args[8]);
		System.out.println("delayTime: "+delayTime);
		
		 ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		    for (int i = 1; i <= maxUsers; i+=increment) {
		      Runnable worker = new com.benchodroff.ldapkillah.Searcher(i, providerURL, ldapbase, ldapUser, ldapPass,
			  			increment, timeLimit, delayTime);
		      executor.execute(worker);
		    }
		    // This will make the executor accept no new threads
		    // and finish all existing threads in the queue
		    executor.shutdown();
		    // Wait until all threads are finish
		    try {
		    	  executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		    	} catch (InterruptedException e) {
		    	  e.printStackTrace();
		    	}
		    System.out.println("Finished all threads");
	  }
	}


