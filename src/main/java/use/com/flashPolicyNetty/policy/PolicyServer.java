package use.com.flashPolicyNetty.policy;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import use.com.flashPolicyNetty.inital.FlashPolicyInitial;


public class PolicyServer  implements Runnable{
	
	
	protected final Logger log = LoggerFactory.getLogger(PolicyServer.class);
	
	private int policy_port = 843;  
	
	
	private EventLoopGroup bossGroup =null;
	private EventLoopGroup workerGroup = null;
	private ChannelFuture  socketChannelFuture = null;
	
	
	public PolicyServer(int port)
	{
		policy_port = port;
	}
	
	public synchronized void close()
	{
		if (socketChannelFuture == null)
		{
			return;
		}
		try
		{
			log.info(FlashPolicyInitial.title+"关闭开始");
			socketChannelFuture.channel().close();
			log.info(FlashPolicyInitial.title+"关闭成功");
		}catch(Exception er)
		{
			log.info(FlashPolicyInitial.title+"关闭失败！");
		}
	}
	
	public void run() 
	{  
		try
		{
			bossGroup = new NioEventLoopGroup();
			workerGroup = new NioEventLoopGroup();
			ServerBootstrap bootStrap = new ServerBootstrap();
			bootStrap.group(bossGroup , workerGroup);
			bootStrap.channel(NioServerSocketChannel.class);
			bootStrap.option(ChannelOption.SO_BACKLOG, 1024);
			bootStrap.childOption(ChannelOption.TCP_NODELAY, true);
			
			bootStrap.childHandler(new PolicyChildHandleInitial());
			
			socketChannelFuture =bootStrap.bind(policy_port).sync();
			socketChannelFuture.channel().closeFuture().sync();

		}catch(Exception er)
		{
			log.error(FlashPolicyInitial.title+er.getMessage());
		}
		finally
		{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			log.error(FlashPolicyInitial.title+"over");
		}
		
	}  
	
	public static void main(String args[])
	{
		PolicyServer a = new PolicyServer(843);
		a.run();
	}
	
}
