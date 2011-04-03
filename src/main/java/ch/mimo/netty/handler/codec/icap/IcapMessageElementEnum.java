package ch.mimo.netty.handler.codec.icap;


public enum IcapMessageElementEnum {
	REQHDR(IcapCodecUtil.ENCAPSULATION_ELEMENT_REQHDR),
	RESHDR(IcapCodecUtil.ENCAPSULATION_ELEMENT_RESHDR),
	REQBODY(IcapCodecUtil.ENCAPSULATION_ELEMENT_REQBODY),
	RESBODY(IcapCodecUtil.ENCAPSULATION_ELEMENT_RESBODY),
	OPTBODY(IcapCodecUtil.ENCAPSULATION_ELEMENT_OPTBODY),
	NULLBODY(IcapCodecUtil.ENCAPSULATION_ELEMENT_NULLBODY);
	
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
