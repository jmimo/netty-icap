package ch.mimo.netty.handler.codec.icap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpChunk;

public interface IcapChunk extends HttpChunk {

	static IcapChunkTrailer LAST_ICAP_CHUNK = new IcapChunkTrailer() {
        public ChannelBuffer getContent() {
            return ChannelBuffers.EMPTY_BUFFER;
        }

        public void setContent(ChannelBuffer content) {
            throw new IllegalStateException("read-only");
        }

        public boolean isLast() {
            return true;
        }

        public void addHeader(String name, Object value) {
            throw new IllegalStateException("read-only");
        }

        public void clearHeaders() {
            // NOOP
        }

        public boolean containsHeader(String name) {
            return false;
        }

        public String getHeader(String name) {
            return null;
        }

        public Set<String> getHeaderNames() {
            return Collections.emptySet();
        }

        public List<String> getHeaders(String name) {
            return Collections.emptyList();
        }

        public List<Map.Entry<String, String>> getHeaders() {
            return Collections.emptyList();
        }

        public void removeHeader(String name) {
            // NOOP
        }

        public void setHeader(String name, Object value) {
            throw new IllegalStateException("read-only");
        }

        public void setHeader(String name, Iterable<?> values) {
            throw new IllegalStateException("read-only");
        }

		@Override
		public boolean isPreviewChunk() {
			return false;
		}

		@Override
		public boolean wasEarlyTerminated() {
			return false;
		}
	};
	
	public boolean isPreviewChunk();
	
	public boolean wasEarlyTerminated();
}
