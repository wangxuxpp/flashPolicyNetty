package use.com.flashPolicyNetty.inital;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import use.com.flashPolicyNetty.util.FlashPolicyUtil;


public class FlashPolicyDestroy implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		if (FlashPolicyUtil.getFFlashPolicyServer() != null)
		{
			FlashPolicyUtil.getFFlashPolicyServer().close();
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
