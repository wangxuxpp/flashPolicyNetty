package use.com.flashPolicyNetty.inital;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import use.com.common.security.BaseInfo;
import use.com.common.util.propertyFile.ReadPropertyValue;
import use.com.flashPolicyNetty.policy.PolicyServer;
import use.com.flashPolicyNetty.util.FlashPolicyUtil;


public class FlashPolicyInitial implements ServletContainerInitializer {

	protected final Logger log = LoggerFactory.getLogger(FlashPolicyInitial.class);
	
	public final static String title ="NettyFlashPolicy_";

	public void onStartup(Set<Class<?>> arg, ServletContext sct) throws ServletException 
	{
		if (!(BaseInfo.getSecurityInfo().getInfo() instanceof Boolean))
		{
			return ;
		}
		
		log.info(title+"服务初始化开始");
		
		FlashPolicyUtil.getFlashPolicyParameter().setPolicyPort(843);
		FlashPolicyUtil.getFlashPolicyParameter().setPolicyIsEnable(false);
		
    	Properties prop = new Properties();   
    	URL uPath = this.getClass().getResource("/"); 
    	InputStream in = null;
    	try 
    	{   
    		File f = new File(uPath.getFile() , "../config/flash.properties");
    		if(f.exists())
    		{
    			in = new FileInputStream(f);
	    		prop.load(in);
	    		FlashPolicyUtil.getFlashPolicyParameter().setPolicyIsEnable(ReadPropertyValue.getBoolean(prop, "flashPolicy.enable", false));
	    		FlashPolicyUtil.getFlashPolicyParameter().setPolicyPort(ReadPropertyValue.getInt(prop, "flashPolicy.port", 843));

    		} else {
    			log.info(title+"服务配置文件不存在，启用默认配置项。");
    		}
    		
    		if (!FlashPolicyUtil.getFlashPolicyParameter().getPolicyIsEnable())
    		{
    			log.info(title+" 沙箱功能停止启动！");
    		}
    		try
    		{
    			PolicyServer r = new PolicyServer(FlashPolicyUtil.getFlashPolicyParameter().getPolicyPort());
    			FlashPolicyUtil.setFFlashPolicyServer(r);
    			new Thread(r).start();
    		}catch(Exception er)
    		{
    			log.error(title+"服务失败，失败原因："+er.getMessage());
    		}
    		log.info(title+"服务初始化完成");
    	}catch(Exception er)
    	{
    		log.error(title+"服务初始化异常,异常原因:"+er.getMessage());
    	}
    	try
    	{
    		log.info(title+"注册FlashPolicy servlet");
    		sct.addListener(FlashPolicyDestroy.class);
    	}catch(Exception er)
    	{
    		log.error(title+"注册flashPolicy失败，失败原因:"+er.getMessage());
    	}
	}
}
