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
 * Return value encapsulation used in all message decoder states.
 * 
 * @author Michael Mimo Moratti (mimo@mimo.ch)
 *
 * @see IcapMessageDecoder
 * @see State
 */
public class StateReturnValue {
	
	private boolean relevance;
	private Object value;
	private Object decisionInformation;
	
	public StateReturnValue(boolean relevance, Object value, Object decisionInformation) {
		this.relevance = relevance;
		this.value = value;
		this.decisionInformation = decisionInformation;
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
		if(value != null) {
			printValue = value.getClass().getCanonicalName();
		}
		return "StateReturnValue: [relevance=" + relevance + 
				"] [value=" + printValue + "] [decision information=" + decisionInformation + "]";
	}
}
