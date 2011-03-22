/*******************************************************************************
 * Copyright (c) 2011 Michael Mimo Moratti.
 *
 * Michael Mimo Moratti licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

public abstract class IcapMessageDecoder extends ReplayingDecoder<StateEnum> {

    protected final int maxInitialLineLength;
    protected final int maxIcapHeaderSize;
    protected final int maxHttpHeaderSize;
    protected final int maxChunkSize;
    
	protected IcapMessage message;
	
	protected int currentChunkSize;
	
    /**
     * Creates a new instance with the default
     * {@code maxInitialLineLength (4096}}, {@code maxIcapHeaderSize (8192)}, {@code maxHttpHeaderSize (8192)}, and
     * {@code maxChunkSize (8192)}.
     */
    protected IcapMessageDecoder() {
        this(4096,8192,8192,8192);
    }
    
    /**
     * Creates a new instance with the specified parameters.
     */
    protected IcapMessageDecoder(int maxInitialLineLength, int maxIcapHeaderSize, int maxHttpHeaderSize, int maxChunkSize) {
        super(StateEnum.SKIP_CONTROL_CHARS,true);
        if (maxInitialLineLength <= 0) {
            throw new IllegalArgumentException("maxInitialLineLength must be a positive integer: " + maxInitialLineLength);
        }
        if (maxIcapHeaderSize <= 0) {
            throw new IllegalArgumentException("maxIcapHeaderSize must be a positive integer: " + maxIcapHeaderSize);
        }
        if (maxChunkSize < 0) {
            throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
        }
        this.maxInitialLineLength = maxInitialLineLength;
        this.maxIcapHeaderSize = maxIcapHeaderSize;
        this.maxHttpHeaderSize = maxHttpHeaderSize;
        this.maxChunkSize = maxChunkSize;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, StateEnum stateEnumValue) throws Exception {
		State state = stateEnumValue.getState();
		//TODO remove when finished
		System.out.print(stateEnumValue.name() + " ");
		state.onEntry(buffer,this);
		StateReturnValue returnValue = state.execute(buffer,this);
		//TODO remove when finished
		if(returnValue == null) {
			System.out.print("return value is null ");
		} else {
			System.out.print(returnValue.toString() + " ");
		}
		StateEnum nextState = state.onExit(buffer,this,returnValue.getDecisionInformation());
		//TODO remove when finished
		System.out.println(nextState);
		// TODO set checkpoint only if required. see preview chunk reading
		// TODO re-reading
		if(nextState != null) {
			checkpoint(nextState);
		} 
//		else {
//			checkpoint();
//		}
		if(returnValue.isRelevant()) {
			return returnValue.getValue();
		}
		return null;
	}
	
	public abstract boolean isDecodingRequest();
	
	protected abstract IcapMessage createMessage(String[] initialLine);
}
