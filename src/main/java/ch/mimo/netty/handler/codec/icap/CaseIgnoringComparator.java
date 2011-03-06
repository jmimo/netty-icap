package ch.mimo.netty.handler.codec.icap;

import java.io.Serializable;
import java.util.Comparator;

final class CaseIgnoringComparator implements Comparator<String>, Serializable {

    private static final long serialVersionUID = 4582133183775373862L;

    static final CaseIgnoringComparator INSTANCE = new CaseIgnoringComparator();

    private CaseIgnoringComparator() {
        super();
    }

    public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
