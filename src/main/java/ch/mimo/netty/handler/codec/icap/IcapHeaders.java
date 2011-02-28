package ch.mimo.netty.handler.codec.icap;


public final class IcapHeaders {
	// TODO add utility class that can parse the Encapsulation and Preview headers.

	private IcapHeaders() {
	}
	
	public static final class Names {
        /**
         * {@code "Cache-Control"}
         */
		public static final String CACHE_CONTROL = "Cache-Control";
        /**
         * {@code "Connection"}
         */
		public static final String CONNECTION = "Connection";
        /**
         * {@code "Date"}
         */
		public static final String DATE = "Date";	
        /**
         * {@code "Expires"}
         */
		public static final String EXPIRES = "Expires";
        /**
         * {@code "Pragma"}
         */
		public static final String PRAGMA = "Pragma";
        /**
         * {@code "Trailer"}
         */
		public static final String TRAILER = "Trailer";
        /**
         * {@code "Upgrade"}
         */
		public static final String UPGRADE = "Upgrade";
        /**
         * {@code "Encapsulated"}
         */
		public static final String ENCAPSULATED = "Encapsulated";
        /**
         * {@code "Authorization"}
         */
		public static final String AUTHORIZATION = "Authorization";
        /**
         * {@code "Allow"}
         */
		public static final String ALLOW = "Allow";
        /**
         * {@code "From"}
         */
		public static final String FROM = "From";
        /**
         * {@code "Host"}
         */
		public static final String HOST = "Host";
        /**
         * {@code "Referer"}
         */
		public static final String REFERER = "Referer";
        /**
         * {@code "User-Agent"}
         */
		public static final String USER_AGENT = "User-Agent";
        /**
         * {@code "Preview"}
         */
		public static final String PREVIEW = "Preview";
	}
	
	public static final class Values {
		// TODO add preview and Encapsulation values.
	}
}
