package use.com.flashPolicyNetty.policy;


import java.util.concurrent.TimeUnit;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

@SuppressWarnings("rawtypes")
public class PolicyChildHandleInitial extends ChannelInitializer{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		// TODO Auto-generated method stub
		ch.pipeline().addLast(new IdleStateHandler( 60 , 0 , 0 , TimeUnit.SECONDS));
		DelimiterBasedFrameDecoder r = new DelimiterBasedFrameDecoder(50 , false ,Unpooled.copiedBuffer(">".getBytes()));
		ch.pipeline().addLast(r);
		ch.pipeline().addLast(new StringDecoder());
		ch.pipeline().addLast(new PolicyChildChannelInboundHandlerAdapter());
	}

}
