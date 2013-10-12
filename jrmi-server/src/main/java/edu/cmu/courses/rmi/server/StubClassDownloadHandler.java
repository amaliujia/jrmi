package edu.cmu.courses.rmi.server;

import edu.cmu.courses.rmi.RemoteStub;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

/**
 * The <code>StubClassDownloadHandler</code> is used to handle
 * the stub download request from client.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class StubClassDownloadHandler extends HttpServlet {

    private static String CONTENT_TYPE = "application/x-java-class";

    /**
     * Parse the client request to get the URL of stub class.
     * 
     * @param request client request.
     * @return URL of stub class.
     */
    private static URL getClassResource(HttpServletRequest request){
        String uri = request.getRequestURI();
        String className = FilenameUtils.removeExtension(uri).substring(RemoteStub.HTTP_URI.length());
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

    /**
     * Deal with client stub download request and return
     * the stub class to client.
     *
     * @param request client request.
     * @param response server response
     */
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
