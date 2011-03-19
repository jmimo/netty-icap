package ch.mimo.netty.handler.codec.icap;

public enum StateEnum {
	SKIP_CONTROL_CHARS(new SkipControlCharsState()),
	READ_ICAP_INITIAL_STATE(new ReadIcapInitialState()),
	READ_ICAP_HEADER_STATE(new ReadIcapHeaderState()),
//	OPTIONS_REQUEST_ACTION_STATE(new OptionsRequestActionState()),
	BODY_PROCESSING_DECISION_STATE(new BodyProcessingDecisionState()),
	READ_HTTP_REQUEST_INITIAL_AND_HEADERS(new ReadHttpRequestInitialAndHeadersState()),
	READ_HTTP_RESPONSE_INITIAL_AND_HEADERS(new ReadHttpResponseInitalAndHeadersState()),
	PREVIEW_STATE(new PreviewState()),
	READ_CHUNK_SIZE_STATE(new ReadChunkSizeState()),
	READ_CHUNK_STATE(new ReadChunkState()),
	READ_CHUNKED_CONTENT_AS_CHUNKS_STATE(new ReadChunkedContentAsChunksState()),
	READ_CHUNK_DELIMITER_STATE(new ReadChunkDelimiterState()),
	READ_TRAILING_HEADERS_STATE(new ReadTrailingHeadersState());
	
	private State<? extends Object> state;
	
	StateEnum(State<? extends Object> state) {
		this.state = state;
	}
	
	public State<? extends Object> getState() {
		return state;
	}
}
