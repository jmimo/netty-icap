package ch.mimo.netty.handler.codec.icap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class IcapHeaderTest extends AbstractIcapTest {

	@Test
	public void callGetterWithoutContent() {
		IcapHeaders headers = new IcapHeaders();
		assertNull("null was expected",headers.getHeader("MIMO"));
	}
	
	@Test
	public void addAndGetHeader() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		assertEquals("unexpected value for header","JOGGEL",headers.getHeader("MIMO"));
	}
	
	@Test
	public void addAndGetMultipleHeader() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMO1","JOGGEL1");
		headers.addHeader("MIMO2","JOGGEL2");
		headers.addHeader("MIMO3","JOGGEL3");
		assertEquals("unexpected value for header","JOGGEL",headers.getHeader("MIMO"));
		assertEquals("unexpected value for header","JOGGEL1",headers.getHeader("mimo1"));
		assertEquals("unexpected value for header","JOGGEL2",headers.getHeader("MIMO2"));
		assertEquals("unexpected value for header","JOGGEL3",headers.getHeader("mimo3"));
	}
	
	@Test
	public void containsHeader() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMOx","JOGGEL");
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMOy","JOGGEL");
		assertTrue("contains returned false",headers.containsHeader("mimo"));
	}
	
	@Test
	public void removeFirstHeader() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMO1","JOGGEL1");
		headers.addHeader("MIMO2","JOGGEL2");
		headers.removeHeader("MIMO");
		assertNull("expected null",headers.getHeader("MIMO"));
		assertNotNull("expected result",headers.getHeader("MIMO1"));
	}
	
	@Test
	public void removeHeader() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMO1","JOGGEL1");
		headers.addHeader("MIMO2","JOGGEL2");
		headers.removeHeader("MIMO1");
		assertNull("expected null",headers.getHeader("MIMO1"));
		assertEquals("wrong amount of names",2,headers.getHeaderNames().size());
		Iterator<String> names = headers.getHeaderNames().iterator();
		assertEquals("wrong header name","MIMO",names.next());
		assertEquals("wrong header name","MIMO2",names.next());
	}
	
	@Test
	public void removeOnlyHeader() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.removeHeader("MIMO");
		assertNull("expected null",headers.getHeader("MIMO"));
	}
	
	@Test
	public void setSingleHeader() {
		IcapHeaders headers = new IcapHeaders();
		headers.setHeader("MIMO","JOGGEL");
		assertEquals("wrong header value","JOGGEL",headers.getHeader("MIMO"));
	}
	
	@Test
	public void setHeader() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		assertEquals("unexpected value for header","JOGGEL",headers.getHeader("MIMO"));
		headers.setHeader("MIMO","JOGGEL1");
		assertEquals("unexpected value for header","JOGGEL1",headers.getHeader("MIMO"));
	}
	
	@Test
	public void removeHeaderWhenAddedThroughMessageConstructor() {
			IcapRequest request = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"icap://icap.mimo.ch:1344/reqmod","icap-server.net");
			request.addHeader("Preview","151");
			request.removeHeader("Preview");
			assertFalse("Preview header is still present",request.containsHeader("Preview"));
	}
	
	@Test
	public void setNewHeader() {
		IcapHeaders header = new IcapHeaders();
		header.setHeader("MIMO","JOGGEL");
		assertTrue("header not found",header.containsHeader("MIMO"));
		assertEquals("unexpected value for header","JOGGEL",header.getHeader("MIMO"));
	}
	
	@Test
	public void addAndSetHeader() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("FOO1","FOOV1");
		headers.addHeader("FOO2","FOOV2");
		headers.setHeader("FOO3","FOOV3");
		headers.addHeader("FOO4","FOOV4");
		assertEquals("wrong header value","FOOV1",headers.getHeader("FOO1"));
		assertEquals("wrong header value","FOOV2",headers.getHeader("FOO2"));
		assertEquals("wrong header value","FOOV3",headers.getHeader("FOO3"));
		assertEquals("wrong header value","FOOV4",headers.getHeader("FOO4"));
	}
	
	@Test
	public void setHeaders() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		assertEquals("unexpected value for header","JOGGEL",headers.getHeader("MIMO"));
		List<String> values = new ArrayList<String>();
		values.add("JOGGEL11");
		values.add("JOGGEL12");
		values.add("JOGGEL13");
		headers.setHeader("MIMO",values);
		assertEquals("unexpected value for header","JOGGEL11",headers.getHeader("MIMO"));
		Set<String> headerValues = headers.getHeaders("MIMO");
		assertEquals("wrong number of values",3,values.size());
		Iterator<String> valueIterator = headerValues.iterator();
		assertEquals("wrong value","JOGGEL11",valueIterator.next());
		assertEquals("wrong value","JOGGEL12",valueIterator.next());
		assertEquals("wrong value","JOGGEL13",valueIterator.next());
	}
	
	@Test
	public void containsHeaderWithName() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMO1","JOGGEL1");
		headers.addHeader("MIMO2","JOGGEL21");
		assertTrue("no header found with that name",headers.containsHeader("MIMO1"));
	}
	
	@Test
	public void getAllHeaderValuesForOneName() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMO1","JOGGEL1");
		headers.addHeader("MIMO2","JOGGEL21");
		headers.addHeader("MIMO2","JOGGEL22");
		headers.addHeader("MIMO2","JOGGEL23");
		headers.addHeader("MIMO3","JOGGEL2");
		Set<String> values = headers.getHeaders("MIMO2");
		assertEquals("wrong number of values",3,values.size());
		Iterator<String> valueIterator = values.iterator();
		assertEquals("wrong value","JOGGEL21",valueIterator.next());
		assertEquals("wrong value","JOGGEL22",valueIterator.next());
		assertEquals("wrong value","JOGGEL23",valueIterator.next());
	}
	
	@Test
	public void retrieveHeaderNames() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMO1","JOGGEL1");
		headers.addHeader("MIMO2","JOGGEL2");
		Set<String> headerNames = headers.getHeaderNames();
		assertEquals("wrong amount of names",3,headerNames.size());
		Iterator<String> headerNameIterator = headerNames.iterator();
		assertEquals("wrong header name","MIMO",headerNameIterator.next());
		assertEquals("wrong header name","MIMO1",headerNameIterator.next());
		assertEquals("wrong header name","MIMO2",headerNameIterator.next());
	}
	
	@Test
	public void retrieveAllHeaders() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMO1","JOGGEL1");
		headers.addHeader("MIMO1","JOGGEL11");
		headers.addHeader("MIMO2","JOGGEL2");
		Set<Map.Entry<String, String>> headerSet = headers.getHeaders();
		assertEquals("wrong amount of headers",4,headerSet.size());
		Iterator<Map.Entry<String, String>> iterator = headerSet.iterator();
		Map.Entry<String, String> header1 = iterator.next();
		assertEquals("wrong header name","MIMO|JOGGEL",header1.getKey() + "|" + header1.getValue());
		Map.Entry<String, String> header2 = iterator.next();
		assertEquals("wrong header name","MIMO1|JOGGEL1",header2.getKey() + "|" + header2.getValue());
		Map.Entry<String, String> header3 = iterator.next();
		assertEquals("wrong header name","MIMO1|JOGGEL11",header3.getKey() + "|" + header3.getValue());
		Map.Entry<String, String> header4 = iterator.next();
		assertEquals("wrong header name","MIMO2|JOGGEL2",header4.getKey() + "|" + header4.getValue());
	}
	
	@Test
	public void exeedInternalBucketSize() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMO1","JOGGEL1");
		headers.addHeader("MIMO2","JOGGEL2");
		headers.addHeader("MIMO3","JOGGEL");
		headers.addHeader("MIMO4","JOGGEL1");
		headers.addHeader("MIMO5","JOGGEL2");
		headers.addHeader("MIMO6","JOGGEL");
		headers.addHeader("MIMO7","JOGGEL1");
		headers.addHeader("MIMO8","JOGGEL2");
		headers.addHeader("MIMO9","JOGGEL");
		headers.addHeader("MIMO10","JOGGEL1");
		headers.addHeader("MIMO11","JOGGEL2");
		headers.addHeader("MIMO12","JOGGEL2");
		headers.addHeader("MIMO13","JOGGEL2");
		headers.addHeader("MIMO14","JOGGEL2");
		headers.addHeader("MIMO15","JOGGEL2");
		headers.addHeader("MIMO16","JOGGEL2");
		headers.addHeader("MIMO17","JOGGEL2");
		headers.addHeader("MIMO18","JOGGEL2");
		headers.addHeader("MIMO19","JOGGEL2");
		headers.addHeader("MIMO20","JOGGEL2");
		headers.addHeader("MIMO21","JOGGEL2");
		headers.addHeader("MIMO22","JOGGEL2");
		Set<String> headerNames = headers.getHeaderNames();
		assertEquals("wrong amount of names",23,headerNames.size());
		assertEquals("header has wrong value","JOGGEL2",headers.getHeader("MIMO21"));
	}
	
	@Test
	public void clean() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO","JOGGEL");
		headers.addHeader("MIMO1","JOGGEL1");
		headers.addHeader("MIMO2","JOGGEL2");
		assertEquals("wrong amount of headers",3,headers.getHeaderNames().size());
		headers.clearHeaders();
		assertEquals("wrong amount of headers",0,headers.getHeaderNames().size());
	}
	
	@Test
	public void invalidPreviewValue() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("PREVIEW","JOGGEL");
		boolean error = false;
		try {
			headers.getPreviewHeaderValue();
		} catch(IcapDecodingError ide) {
			error = true;
		}
		assertTrue("no error was thrown",error);
	}
	
	@Test
	public void validPreviewValue() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("PREVIEW","123");
		boolean error = false;
		try {
			assertEquals("unexpected preview amount",123,headers.getPreviewHeaderValue());
		} catch(IcapDecodingError ide) {
			ide.printStackTrace();
			error = true;
		}
		assertFalse("error was thrown",error);
	}
	
	@Test
	public void addHeaderWithoutValue() {
		IcapHeaders headers = new IcapHeaders();
		headers.addHeader("MIMO",null);
		Set<String> headerNames = headers.getHeaderNames();
		assertEquals("wrong amount of names",1,headerNames.size());
		Set<Map.Entry<String, String>> entries = headers.getHeaders();
		assertEquals("no header found",1,entries.size());
		Iterator<Map.Entry<String, String>> iterator = entries.iterator();
		Map.Entry<String, String> entry = iterator.next();
		assertEquals("wrong header name","MIMO",entry.getKey());
		assertNull("value was not null",entry.getValue());
	}
	
	@Test
	public void addAndSetHeaderConsistencyTest() {
		IcapRequest message = new DefaultIcapRequest(IcapVersion.ICAP_1_0,IcapMethod.REQMOD,"/foo","icap.somehost.com");
		assertEquals("host entry does not match","icap.somehost.com",message.getHeader(IcapHeaders.Names.HOST));
		message.setHeader(IcapHeaders.Names.PREVIEW,"42");
		assertEquals("host entry does not match","icap.somehost.com",message.getHeader(IcapHeaders.Names.HOST));
		assertEquals("host entry does not match","42",message.getHeader(IcapHeaders.Names.PREVIEW));
	}
	
	@Test
	public void addDateHeader() {
		IcapHeaders headers = new IcapHeaders();
		Date date = new Date();
		headers.addDateHeader(IcapHeaders.Names.DATE,date);
		String dateValue = headers.getHeader(IcapHeaders.Names.DATE);
		SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z",Locale.ENGLISH);
		assertEquals("Date is not as expected",format.format(date),dateValue);
	}
	
	@Test
	public void getDateHeader() {
		IcapHeaders headers = new IcapHeaders();
		Date date = new Date();
		headers.addDateHeader(IcapHeaders.Names.DATE,date);
		Date returnedDate = headers.getDateHeader(IcapHeaders.Names.DATE);
		assertNotNull("The returned date was null",returnedDate);
	}
}
