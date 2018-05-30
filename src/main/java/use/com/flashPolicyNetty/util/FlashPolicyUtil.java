package use.com.flashPolicyNetty.util;

import use.com.flashPolicyNetty.policy.PolicyServer;

public class FlashPolicyUtil 
{
	private static FlashPolicyParameter fParm = new FlashPolicyParameter();
	private static PolicyServer fPolicyServer = null; 
	
	public static void setFFlashPolicyServer(PolicyServer v)
	{
		fPolicyServer = v;
	}
	public static PolicyServer getFFlashPolicyServer()
	{
		return fPolicyServer;
	}
	
	public static FlashPolicyParameter getFlashPolicyParameter()
	{
		return fParm;
	}

	public static String cross_xml ="<?xml version=\"1.0\"?>"
								+"<cross-domain-policy>"
								+"<site-control permitted-cross-domain-policies=\"all\"/>"
								+"<allow-access-from domain=\"*\" to-ports=\"*\"/>"
								+"</cross-domain-policy>\0" ;
	
}
