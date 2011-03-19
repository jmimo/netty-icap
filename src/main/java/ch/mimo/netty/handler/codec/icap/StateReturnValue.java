package ch.mimo.netty.handler.codec.icap;

public class StateReturnValue {
	
	private boolean relevance;
	private Object value;
	private Object decisionInformation;
	
	public StateReturnValue(boolean relevance, Object value, Object decisionInformation) {
		this.relevance = relevance;
		this.value = value;
	}
	
	public static StateReturnValue createNullResult() {
		return new StateReturnValue(true,null,null);
	}
	
	public static StateReturnValue createNullResultWithDecisionInformation(Object decisionInformation) {
		return new StateReturnValue(true,null,decisionInformation);
	}
	
	public static StateReturnValue createIrrelevantResult() {
		return new StateReturnValue(false,null,null);
	}
	
	public static StateReturnValue createIrrelevantResultWithDecisionInformation(Object decisionInformation) {
		return new StateReturnValue(false,null,decisionInformation);
	}
	
	public static StateReturnValue createRelevantResult(Object result) {
		return new StateReturnValue(true,result,null);
	}
	
	public static StateReturnValue createRelevantResultWithDecisionInformation(Object result, Object decisionInformation) {
		return new StateReturnValue(true,result,decisionInformation);
	}
	
	public boolean isRelevant() {
		return relevance;
	}
	
	public Object getValue() {
		return value;
	}
	
	public Object getDecisionInformation() {
		return decisionInformation;
	}
}
