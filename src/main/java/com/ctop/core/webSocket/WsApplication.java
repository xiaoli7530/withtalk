package com.ctop.core.webSocket;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;

/**
 * This is the entry point in the application, executed when the servlet container first starts. 
 * It registers the websocket endpoint with the server container.<p>
 * You can have this class listen to the servlet context initialization by using the @WebListener annotation or
 * configuring it in the web.xml.
 * @author Carlos Martins
 *
 */
@WebListener
public class WsApplication implements ServletContextListener {
	private final static String SERVER_CONTAINER_ATTRIBUTE = "javax.websocket.server.ServerContainer";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext container = sce.getServletContext();

		final ServerContainer serverContainer = (ServerContainer) container.getAttribute(SERVER_CONTAINER_ATTRIBUTE);
		try {
			serverContainer.addEndpoint(new WsEndpointConfig(WsEndpoint.class, "/websocket"));
		} catch (DeploymentException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
