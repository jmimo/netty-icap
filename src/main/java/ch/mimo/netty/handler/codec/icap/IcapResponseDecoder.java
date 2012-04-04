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
package ch.mimo.netty.handler.codec.icap;

/**
 * ICAP Response decoder which creates an @see {@link IcapResponse} instance.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 */
public class IcapResponseDecoder extends IcapMessageDecoder {

	public IcapResponseDecoder() {
		super();
	}

	/**
	 * @see IcapMessageDecoder IcapMessageDecoder constructor for more details.
	 * 
	 * @param maxInitialLineLength
	 * @param maxIcapHeaderSize
	 * @param maxHttpHeaderSize
	 * @param maxChunkSize
	 */
	public IcapResponseDecoder (int maxInitialLineLength, int maxIcapHeaderSize, int maxHttpHeaderSize, int maxChunkSize) {
		super(maxInitialLineLength, maxIcapHeaderSize, maxHttpHeaderSize, maxChunkSize);
	}
	
	@Override
	protected IcapMessage createMessage(String[] initialLine) {
		return new DefaultIcapResponse(IcapVersion.valueOf(initialLine[0]),IcapResponseStatus.fromCode(initialLine[1]));
	}

	@Override
	public boolean isDecodingResponse() {
		return true;
	}
}
