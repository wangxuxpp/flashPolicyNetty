package use.com.flashPolicyNetty.policy;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import use.com.flashPolicyNetty.inital.FlashPolicyInitial;
import use.com.flashPolicyNetty.util.FlashPolicyUtil;

public class PolicyChildChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter{

	protected final Logger log = LoggerFactory.getLogger(PolicyChildChannelInboundHandlerAdapter.class);
	
    /*public void channelActive(ChannelHandlerContext ctx)
            throws Exception
    {
        System.out.println(((InetSocketAddress)ctx.channel().remoteAddress()).getHostName()+"连接激活");
    }

    public void channelInactive(ChannelHandlerContext ctx)
        throws Exception
    {
    	 System.out.println(((InetSocketAddress)ctx.channel().remoteAddress()).getHostName()+"连接关闭");
    }*/
    
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception
    {
    	if (evt instanceof IdleStateEvent) 
    	{
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE)
            {
            	log.info(((InetSocketAddress)ctx.channel().remoteAddress()).getHostName()+"连接超时，断开连接");
            	ctx.close();
            }
    	}else{
    		ctx.fireUserEventTriggered(evt);
    	}

    }   
    
	public void channelRead(final ChannelHandlerContext ctx, Object msg)throws Exception
    {
		ByteBuf b = null;
		try
		{
			String body =(String)msg;
	        if (body.equals("<policy-file-request/>"))
	        {
	        	b = Unpooled.copiedBuffer(FlashPolicyUtil.cross_xml.getBytes()); 
	        	ChannelFuture f = ctx.writeAndFlush(b);	
	        	f.addListener(new ChannelFutureListener()
		        	{
		        		public void operationComplete(ChannelFuture future) 
		        		{
		        			ctx.close();
		                }
		        	}
	        	);
	        } else {
	        	ctx.close();
	        }
		}finally
		{
			//ReferenceCountUtil.release(msg);
			if (b != null)
			{
				//ReferenceCountUtil.release(b);
			}
		}
    }
	
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception
    {
    	log.error(FlashPolicyInitial.title+cause.getMessage());
    	//System.out.println(cause.getMessage());
    	ctx.close();
    }
}
