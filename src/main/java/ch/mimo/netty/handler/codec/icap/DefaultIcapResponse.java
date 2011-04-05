package ch.mimo.netty.handler.codec.icap;

import org.jboss.netty.handler.codec.http.HttpVersion;

public class DefaultIcapResponse extends DefaultIcapMessage implements IcapResponse {

	private IcapResponseStatus status;
	
	public DefaultIcapResponse(HttpVersion version, IcapResponseStatus status) {
		super(version);
		this.status = status;
	}

	@Override
	public void setIcapResponseStatus(IcapResponseStatus status) {
		this.status = status;
	}

	@Override
	public IcapResponseStatus getIcapResponseStatus() {
		return status;
	}

	@Override
	protected void validateHeader(String name) throws IllegalArgumentException {
		// NOOP
	}

}
