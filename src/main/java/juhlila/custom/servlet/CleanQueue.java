package juhlila.custom.servlet;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Julia Guimaraes on 27/05/19.
 */

public class CleanQueue extends RegistryBasedServlet {

    public CleanQueue() {
        this(null);
    }

    public CleanQueue(GridRegistry registry) {
        super(registry);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getRegistry().clearNewSessionRequests();
    }
}