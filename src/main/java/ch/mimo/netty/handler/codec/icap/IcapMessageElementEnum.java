package ch.mimo.netty.handler.codec.icap;


public enum IcapMessageElementEnum {
	// TODO remove literals
	REQHDR("req-hdr"),
	RESHDR("res-hdr"),
	REQBODY("req-body"),
	RESBODY("res-body"),
	OPTBODY("opt-body"),
	NULLBODY("null-body");
	
	private String value;
	
	IcapMessageElementEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static IcapMessageElementEnum fromString(String value) {
		if(value != null) {
			for(IcapMessageElementEnum entryName : IcapMessageElementEnum.values()) {
				if(value.equalsIgnoreCase(entryName.getValue())) {
					return entryName;
				}
			}
		}
		return null;
	}
}
