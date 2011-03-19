package ch.mimo.netty.handler.codec.icap;

public class StateReturnValue {
	
	private boolean relevance;
	private Object value;
	private Object decisionInformation;
	
	public StateReturnValue(boolean relevance, Object value, Object decisionInformation) {
		this.relevance = relevance;
		this.value = value;
		this.decisionInformation = decisionInformation;
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
	
	public String toString() {
		String printValue = "null";
		String printDecisionInformation = "null";
		if(value != null) {
			printValue = value.getClass().getCanonicalName();
		}
		if(decisionInformation != null) {
			printDecisionInformation = decisionInformation.getClass().getCanonicalName();
		}
		return "StateReturnValue: [relevance=" + relevance + 
				"] [value=" + printValue + "] [decision information=" + printDecisionInformation + "]";
	}
}
