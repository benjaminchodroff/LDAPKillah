package com.benchodroff.ldapkillah;

import java.security.cert.X509Certificate;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Searcher implements Runnable {

	private int i;
	private String providerURL;
	private String ldapbase;
	private String ldapUser;
	private String ldapPass;
	private int increment;
	private int timeLimit;
	private int delayTime;

	public Searcher(int i, String providerURL, String ldapbase, String ldapUser,
			String ldapPass, int increment, int timeLimit, int delayTime) {
		this.i = i;
		this.providerURL = providerURL;
		this.ldapbase = ldapbase;
		this.ldapUser = ldapUser;
		this.ldapPass = ldapPass;
		this.increment = increment;
		this.timeLimit = timeLimit;
		this.delayTime = delayTime;
	}

	private SearchControls getSimpleSearchControls(int timeLimit) {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		// Wait for up to 10 seconds before giving up
		searchControls.setTimeLimit(timeLimit);
		// String[] attrIDs = {"objectGUID"};
		// searchControls.setReturningAttributes(attrIDs);
		return searchControls;
	}

	@Override
	public void run() {
		String threadId = Thread.currentThread().getName();
		Hashtable env = new Hashtable(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, providerURL);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ldapUser);
		env.put(Context.SECURITY_CREDENTIALS, ldapPass);
		// supposedly this makes things faster?
		env.put(Context.REFERRAL, "ignore");

		TrustManager trm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { trm }, null);
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			LdapContext ctx = new InitialLdapContext(env, null);
			ctx.setRequestControls(null);

			String angrySearchString = "(|";
			for (int overload = 0; overload <= increment; overload++) {
				angrySearchString += "(uidnumber=" + (overload + i) + ")";
			}
			angrySearchString += ")";
			// System.err.println(angrySearchString);

			NamingEnumeration<?> namingEnum = ctx.search(ldapbase,
					angrySearchString, getSimpleSearchControls(timeLimit));
			for (int numresult = 0; namingEnum.hasMore()
					&& numresult < increment; numresult++) {
				SearchResult result = (SearchResult) namingEnum.next();
				Attributes attrs = result.getAttributes();
				System.out.println("ThreadId:"+threadId+"\t"+attrs.get("uidnumber") + " - "
						+ attrs.get("cn"));
			}
			namingEnum.close();
			ctx.close();
			try {
			    Thread.sleep(delayTime);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
