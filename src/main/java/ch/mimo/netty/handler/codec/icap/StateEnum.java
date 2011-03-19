package ch.mimo.netty.handler.codec.icap;

public enum StateEnum {
	SKIP_CONTROL_CHARS(new SkipControlCharsState()),
	READ_ICAP_INITIAL_STATE(new ReadIcapInitialState()),
	READ_ICAP_HEADER_STATE(new ReadIcapHeaderState()),
	OPTIONS_REQUEST_ACTION_STATE(new OptionsRequestActionState()),
	BODY_PROCESSING_DECISION_STATE(new BodyProcessingDecisionState()),
	READ_HTTP_REQUEST_INITIAL_AND_HEADERS(new ReadHttpRequestInitialAndHeadersState()),
	READ_HTTP_RESPONSE_INITIAL_AND_HEADERS(new ReadHttpResponseInitalAndHeadersState()),
	PREVIEW_STATE(new PreviewState()),
	BODY_PROCESSING_STATE(new BodyProcessingState());
	
	private State state;
	
	StateEnum(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
}
