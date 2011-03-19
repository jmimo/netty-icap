package ch.mimo.netty.handler.codec.icap;

public enum StateEnum {
	SKIP_CONTROL_CHARS(new SkipControlCharsState()),
	READ_ICAP_INITIAL_STATE(new ReadIcapInitialState()),
	READ_ICAP_HEADER_STATE(new ReadIcapHeaderState()),
	OPTIONS_REQUEST_ACTION_STATE(new OptionsRequestActionState()),
	BODY_PROCESSING_DECISION_STATE(new BodyProcessingDecisionState()),
	READ_HTTP_REQUEST_INITIAL_AND_HEADERS(new ReadHttpRequestInitialAndHeadersState()),
	READ_HTTP_RESPONSE_INITIAL_AND_HEADERS(new ReadHttpResponseInitalAndHeadersState());
//	OPTIONS_REQUEST_ACTION,
//	READ_HTTP_REQUEST_HEADER,
//	READ_HTTP_RESPONSE_HEADER,
//	READ_BODY
	
	private State state;
	
	StateEnum(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
}
