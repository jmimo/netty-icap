package ch.mimo.netty.handler.codec.icap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public final class IcapHeaders {
	
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
	
//	public static final class Values {
//		// TODO add preview and Encapsulation values.
//	}

    private static final int BUCKET_SIZE = 17;

    private static int hash(String name) {
        int h = 0;
        for (int i = name.length() - 1; i >= 0; i --) {
            char c = name.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c += 32;
            }
            h = 31 * h + c;
        }

        if (h > 0) {
            return h;
        } else if (h == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return -h;
        }
    }

    private static boolean eq(String name1, String name2) {
        int nameLen = name1.length();
        if (nameLen != name2.length()) {
            return false;
        }

        for (int i = nameLen - 1; i >= 0; i --) {
            char c1 = name1.charAt(i);
            char c2 = name2.charAt(i);
            if (c1 != c2) {
                if (c1 >= 'A' && c1 <= 'Z') {
                    c1 += 32;
                }
                if (c2 >= 'A' && c2 <= 'Z') {
                    c2 += 32;
                }
                if (c1 != c2) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int index(int hash) {
        return hash % BUCKET_SIZE;
    }

    private final Entry[] entries = new Entry[BUCKET_SIZE];
    private final Entry head = new Entry(-1, null, null);

    public IcapHeaders() {
        head.before = head.after = head;
    }

    void validateHeaderName(String name) {
    	IcapCodecUtil.validateHeaderName(name);
    }

    void addHeader(final String name, final Object value) {
        validateHeaderName(name);
        String strVal = toString(value);
        IcapCodecUtil.validateHeaderValue(strVal);
        int h = hash(name);
        int i = index(h);
        addHeader0(h, i, name, strVal);
    }

    private void addHeader0(int h, int i, final String name, final String value) {
        // Update the hash table.
        Entry e = entries[i];
        Entry newEntry;
        entries[i] = newEntry = new Entry(h, name, value);
        newEntry.next = e;

        // Update the linked list.
        newEntry.addBefore(head);
    }

    void removeHeader(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        int h = hash(name);
        int i = index(h);
        removeHeader0(h, i, name);
    }

    private void removeHeader0(int h, int i, String name) {
        Entry e = entries[i];
        if (e == null) {
            return;
        }

        for (;;) {
            if (e.hash == h && eq(name, e.key)) {
                e.remove();
                Entry next = e.next;
                if (next != null) {
                    entries[i] = next;
                    e = next;
                } else {
                    entries[i] = null;
                    return;
                }
            } else {
                break;
            }
        }

        for (;;) {
            Entry next = e.next;
            if (next == null) {
                break;
            }
            if (next.hash == h && eq(name, next.key)) {
                e.next = next.next;
                next.remove();
            } else {
                e = next;
            }
        }
    }

    void setHeader(final String name, final Object value) {
        validateHeaderName(name);
        String strVal = toString(value);
        IcapCodecUtil.validateHeaderValue(strVal);
        int h = hash(name);
        int i = index(h);
        removeHeader0(h, i, name);
        addHeader0(h, i, name, strVal);
    }

    void setHeader(final String name, final Iterable<?> values) {
        if (values == null) {
            throw new NullPointerException("values");
        }

        validateHeaderName(name);

        int h = hash(name);
        int i = index(h);

        removeHeader0(h, i, name);
        for (Object v: values) {
            if (v == null) {
                break;
            }
            String strVal = toString(v);
            IcapCodecUtil.validateHeaderValue(strVal);
            addHeader0(h, i, name, strVal);
        }
    }

    void clearHeaders() {
        for (int i = 0; i < entries.length; i ++) {
            entries[i] = null;
        }
        head.before = head.after = head;
    }

    String getHeader(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        int h = hash(name);
        int i = index(h);
        Entry e = entries[i];
        while (e != null) {
            if (e.hash == h && eq(name, e.key)) {
                return e.value;
            }

            e = e.next;
        }
        return null;
    }

    List<String> getHeaders(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        LinkedList<String> values = new LinkedList<String>();

        int h = hash(name);
        int i = index(h);
        Entry e = entries[i];
        while (e != null) {
            if (e.hash == h && eq(name, e.key)) {
                values.addFirst(e.value);
            }
            e = e.next;
        }
        return values;
    }

    List<Map.Entry<String, String>> getHeaders() {
        List<Map.Entry<String, String>> all =
            new LinkedList<Map.Entry<String, String>>();

        Entry e = head.after;
        while (e != head) {
            all.add(e);
            e = e.after;
        }
        return all;
    }

    boolean containsHeader(String name) {
        return getHeader(name) != null;
    }

    Set<String> getHeaderNames() {
        Set<String> names =
            new TreeSet<String>(CaseIgnoringComparator.INSTANCE);

        Entry e = head.after;
        while (e != head) {
            names.add(e.key);
            e = e.after;
        }
        return names;
    }

    private static String toString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    private static final class Entry implements Map.Entry<String, String> {
        final int hash;
        final String key;
        String value;
        Entry next;
        Entry before, after;

        Entry(int hash, String key, String value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        void remove() {
            before.after = after;
            after.before = before;
        }

        void addBefore(Entry e) {
            after  = e;
            before = e.before;
            before.after = this;
            after.before = this;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public String setValue(String value) {
            if (value == null) {
                throw new NullPointerException("value");
            }
            IcapCodecUtil.validateHeaderValue(value);
            String oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
