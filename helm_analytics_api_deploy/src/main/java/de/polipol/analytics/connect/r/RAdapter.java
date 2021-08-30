package de.polipol.analytics.connect.r;

import java.io.IOException;
import java.nio.file.Path;

import de.polipol.analytics.connect.Adapter;
import de.polipol.analytics.exception.AnalyticsException;

public interface RAdapter extends Adapter {

	void clearWorkspace() throws AnalyticsException;

	String[] eval(String expression) throws AnalyticsException;

	String getLastError();

	Path getWorkingDirectory() throws AnalyticsException, IOException;

	void loadFile(Path path) throws AnalyticsException, IOException;

	void loadPackage(String[] rPackage) throws AnalyticsException;

	void setWorkingDirectory(Path path) throws AnalyticsException, IOException;
}
