package de.polipol.analytics.task;

import java.util.Map;

public interface TaskScheduler {

	void schedule(String connectionId, String role, String paragraphId);

	void schedule(String connectionId, String role, String paragraphId, Map<String, String> variables);
}
