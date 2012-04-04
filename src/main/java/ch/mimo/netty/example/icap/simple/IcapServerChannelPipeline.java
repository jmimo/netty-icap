/*******************************************************************************
 * Copyright 2011 - 2012 Michael Mimo Moratti
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ch.mimo.netty.example.icap.simple;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import ch.mimo.netty.handler.codec.icap.IcapChunkAggregator;
import ch.mimo.netty.handler.codec.icap.IcapChunkSeparator;
import ch.mimo.netty.handler.codec.icap.IcapRequestDecoder;
import ch.mimo.netty.handler.codec.icap.IcapResponseEncoder;

public class IcapServerChannelPipeline implements ChannelPipelineFactory {

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();
    	pipeline.addLast("decoder",new IcapRequestDecoder());
    	pipeline.addLast("chunkAggregator",new IcapChunkAggregator(4096));
    	pipeline.addLast("encoder",new IcapResponseEncoder());
    	pipeline.addLast("chunkSeparator",new IcapChunkSeparator(4096));
    	pipeline.addLast("handler",new IcapServerHandler());
        return pipeline;
    }
    
}
