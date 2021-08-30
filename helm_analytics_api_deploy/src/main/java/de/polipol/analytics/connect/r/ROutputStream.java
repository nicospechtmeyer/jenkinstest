package de.polipol.analytics.connect.r;

import java.io.OutputStream;
import java.io.PrintStream;

public final class ROutputStream {

	public static final PrintStream out = new PrintStream(new OutputStream() {

		@Override
		public void close() {
		}

		@Override
		public void flush() {
		}

		@Override
		public void write(byte[] b) {
		}

		@Override
		public void write(byte[] b, int off, int len) {
		}

		@Override
		public void write(int b) {
		}
	});
}