package com.esir.resiot;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import tuwien.auto.calimero.KNXException;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Objects;

public class Main
{
    public static void main(String[] args) throws ServletException, DeploymentException, KNXException, InterruptedException {
        final InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8080);
        Server server = new Server(inetSocketAddress);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        //context.addServlet(new ServletHolder(new HelloServlet()), "/");

        HTTPHandler httpHandler = new HTTPHandler();
        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(new Handler[] {context, httpHandler});
        server.setHandler(handlerCollection);

        // Add javax.websocket support
        ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);

        // Add echo endpoint to server container
        ServerEndpointConfig echoConfig = ServerEndpointConfig.Builder.create(Websocket.class,"/test").build();
        container.addEndpoint(echoConfig);

        // Add default servlet (to serve the html/css/js)
        // Figure out where the static files are stored.
        URL urlStatics = Thread.currentThread().getContextClassLoader().getResource("index.html");
        Objects.requireNonNull(urlStatics,"Unable to find index.html in classpath");
        String urlBase = urlStatics.toExternalForm().replaceFirst("/[^/]*$","/");
        ServletHolder defHolder = new ServletHolder("default",new DefaultServlet());
        defHolder.setInitParameter("resourceBase",urlBase);
        defHolder.setInitParameter("dirAllowed","true");
        context.addServlet(defHolder,"/");

        try
        {
            server.start();
            server.join();
            System.out.println("Serveur démarré");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
    public static class HelloServlet extends HttpServlet {
        final Logger LOG = Log.getLogger(Main.class);

        protected void doGet(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException,
                IOException {

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println();
        }

    }

     */
}
