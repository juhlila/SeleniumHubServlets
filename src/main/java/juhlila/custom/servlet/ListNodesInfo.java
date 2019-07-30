package juhlila.custom.servlet;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.internal.ProxySet;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.TestSlot;
import org.openqa.grid.web.servlet.RegistryBasedServlet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Julia Guimaraes on 30/07/19.
 */

public class ListNodesInfo extends RegistryBasedServlet {

    public ListNodesInfo() {
        this(null);
    }
    public ListNodesInfo(GridRegistry registry) {
        super(registry);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProxySet proxySet = getRegistry().getAllProxies();
        Iterator<RemoteProxy> iterator = proxySet.iterator();
        JsonArray array = new JsonArray();
        while (iterator.hasNext()) {
            JsonObject jsonObject = new JsonObject();
            RemoteProxy proxy = iterator.next();
//            Iterable p = proxy.getRegistry().getDesiredCapabilities();
            Integer psize = getRegistry().getUsedProxies().size();
            TestSlot session = proxy.getTestSlots().iterator().next();
            Map caps = proxy.getTestSlots().iterator().next().getCapabilities();
//            Collection<Object> hey = proxy.getTestSlots().iterator().next().getCapabilities().values();
            Object device = proxy.getTestSlots().iterator().next().getCapabilities().get("deviceName");
            Object browser = proxy.getTestSlots().iterator().next().getCapabilities().get("browserName");
            Object platform = proxy.getTestSlots().iterator().next().getCapabilities().get("platformName");

            jsonObject.addProperty("ip address", proxy.getRemoteHost().toString());
            jsonObject.addProperty("node id", proxy.getId());
            jsonObject.addProperty("proxystatus", proxy.getProxyStatus().toString());
            jsonObject.addProperty("used proxy", psize.toString());
            jsonObject.addProperty("is busy", proxy.isBusy());
            jsonObject.addProperty("total used", String.valueOf(proxy.getTotalUsed()));
            jsonObject.addProperty("node session", session.toString());
            jsonObject.addProperty("node capabilities",caps.toString());
            jsonObject.addProperty("platform name",platform.toString());
            jsonObject.addProperty("browser", browser.toString());
            jsonObject.addProperty("device name",device.toString());
            jsonObject.addProperty("config", proxy.getConfig().toString());
            array.add(jsonObject);
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        resp.getWriter().append(array.toString());

    }
}