package juhlila.custom.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.openqa.grid.internal.*;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Julia Guimaraes on 07/02/19.
 */

public class ReleaseNodeSession extends RegistryBasedServlet {

    public ReleaseNodeSession() {
        this(null);
    }

    public ReleaseNodeSession(GridRegistry registry) {
        super(registry);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getParameter("sessionId");
        JsonArray array = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        array.add(jsonObject);

        if (sessionId != null && !sessionId.trim().isEmpty()) {
            ExternalSessionKey key = new ExternalSessionKey(sessionId);
            TestSession sessionToDelete = getRegistry().getSession(key);

            jsonObject.addProperty("status", "deleted");
            getRegistry().terminate(sessionToDelete, SessionTerminationReason.CLIENT_GONE);

            ProxySet ps = getRegistry().getAllProxies();
            for (RemoteProxy p : ps)
                for (TestSlot ts : p.getTestSlots()) {
                    if (ts.getSession() == sessionToDelete) {
                        p.getRegistry().forceRelease(ts, SessionTerminationReason.CLIENT_GONE);
                    }
                }
        } else {
            jsonObject.addProperty("status", "null session");
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        resp.getWriter().append(array.toString());
    }
}