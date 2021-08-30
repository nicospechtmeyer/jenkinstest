package de.polipol.analytics.connect.r;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.commons.lang3.StringUtils;
import org.math.R.RserveSession;
import org.math.R.RserverConf;
import org.math.R.Rsession;

import de.polipol.analytics.connect.ConnectionPool;

public final class RsessionConnectionPool extends ConnectionPool<Rsession> {

	private static final String R = "R://";

	private String host;
	private int port;

	public RsessionConnectionPool() {
		super();
		this.host = EMPTY;
		this.port = 0;
	}

	public RsessionConnectionPool(final String host, final int port) {
		super();
		this.host = host;
		this.port = port;
	}

	@Override
	public Rsession create() {
		Rsession rsession;
		if (StringUtils.isEmpty(host)) {
			rsession = new RserveSession(ROutputStream.out, null, null);
		} else {
			rsession = new RserveSession(ROutputStream.out, null, RserverConf.parse(R + host + ":" + port));
		}
		rsession.sinkOutput(true);
		rsession.sinkMessage(true);
		return rsession;
	}

	@Override
	public void expire(final Rsession session) {
		session.end();
	}

	@Override
	public boolean isConnected(final Rsession session) {
		return ((RserveSession) session).connected;
	}
}