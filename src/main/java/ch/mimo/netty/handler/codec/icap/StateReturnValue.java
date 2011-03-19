package ch.mimo.netty.handler.codec.icap;

public class StateReturnValue {

	private boolean relevance;
	private Object value;
	
	public StateReturnValue(boolean relevance, Object value) {
		this.relevance = relevance;
		this.value = value;
	}
	
	public static StateReturnValue createNullResult() {
		return new StateReturnValue(true,null);
	}
	
	public static StateReturnValue createIrrelevantResult() {
		return new StateReturnValue(false,null);
	}
	
	public static StateReturnValue createRelevantResult(Object result) {
		return new StateReturnValue(true,result);
	}
	
	public boolean isRelevant() {
		return relevance;
	}
	
	public Object getValue() {
		return value;
	}
}
