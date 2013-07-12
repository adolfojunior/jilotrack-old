package br.com.cubekode.jilotrack.tracker.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import br.com.cubekode.jilotrack.tracker.Tracker;

/**
 * @author adolfojunior
 */
public class TrackerServletListener implements ServletRequestListener {

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		
		Tracker.push("Request");
		
		TrackerServletInfo.trackServlet(sre.getServletRequest());
	}

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		
		Tracker.pop();
	}
}
