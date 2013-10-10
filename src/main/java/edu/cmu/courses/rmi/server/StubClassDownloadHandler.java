package edu.cmu.courses.rmi.server;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

public class StubClassDownloadHandler extends HttpServlet {

    public static String URI = "/stub/";
    private static String CONTENT_TYPE = "application/x-java-class";

    private static URL getClassResource(HttpServletRequest request){
        String uri = request.getRequestURI();
        String className = FilenameUtils.removeExtension(uri).substring(URI.length());
        if(className == ""){
            return null;
        }
        className = className.replaceAll("/", ".");
        try {
            Class c = Class.forName(className);
            return c.getResource(FilenameUtils.getName(uri));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        URL resource = getClassResource(request);
        if(resource == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            OutputStream out = response.getOutputStream();
            InputStream in = resource.openStream();
            int size = 0;
            while(in.read() != -1){
                size++;
            }
            in.close();
            in = resource.openStream();
            response.setContentLength(size);
            IOUtils.copy(in, out);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
