package zesp03.servlet;

import zesp03.core.Database;
import zesp03.data.ControllerData;
import zesp03.data.DeviceData;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Details extends HttpServlet {
    public static final String PARAM_ID = "id";
    public static final String PARAM_HISTORY_LIMIT = "limit";
    // mapuje do zesp03.data.ControllerData
    public static final String ATTR_CONTROLLER = "zesp03.servlet.Details.ATTR_CONTROLLER";
    // mapuje do zesp03.data.DeviceData
    public static final String ATTR_DEVICE = "zesp03.servlet.Details.ATTR_DEVICE";
    // mapuje do List<DeviceSurvey> posortowanej: na początku najnowsze
    public static final String ATTR_SELECTED_SURVEYS = "zesp03.servlet.Details.ATTR_SELECTED_SURVEYS";
    // mapuje do Integer
    public static final String ATTR_TOTAL_SURVEYS = "zesp03.servlet.Details.ATTR_TOTAL_SURVEYS";
    // mapuje do Integer
    public static final String ATTR_HISTORY_LIMIT = "zesp03.servlet.Details.ATTR_HISTORY_LIMIT";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Device device;
        DeviceData deviceData;
        ControllerData controllerData;
        List<DeviceSurvey> selectedSurveys;
        Integer totalSurveys;
        Integer historyLimit;
        final String paramId = request.getParameter(PARAM_ID);
        final String paramHistoryLimit = request.getParameter(PARAM_HISTORY_LIMIT);

        long devId;
        try {
            if(paramId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "id required");
                return;
            }
            devId = Long.parseLong(paramId);
        }
        catch(NumberFormatException exc) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid id");
            return;
        }

        historyLimit = 100; // default value
        if(paramHistoryLimit != null) {
            try {
                historyLimit = Integer.parseInt(paramHistoryLimit);
            }
            catch(NumberFormatException exc) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid history limit");
                return;
            }
            if(historyLimit < 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid history limit");
                return;
            }
        }

        final EntityManager em = Database.createEntityManager();
        final EntityTransaction tran = em.getTransaction();
        tran.begin();

        device = em.find(Device.class, devId);
        if(device == null) {
            tran.commit();
            em.close();
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "no such device");
            return;
        }
        deviceData = new DeviceData(device);
        controllerData = new ControllerData(device.getController());
        selectedSurveys = device.getDeviceSurveys().stream()
                .sorted( (s1, s2) -> {
                    if( s1.getTimestamp().equals(s2.getTimestamp()) ) {
                        return s1.getId() > s2.getId() ? -1 : 1;
                    }
                    else return s1.getTimestamp() > s2.getTimestamp() ? -1 : 1;
                } )
                .limit(historyLimit)
                .collect(Collectors.toList());
        totalSurveys = device.getDeviceSurveys().size();

        tran.commit();
        em.close();

        request.setAttribute(ATTR_CONTROLLER, controllerData);
        request.setAttribute(ATTR_DEVICE, deviceData);
        request.setAttribute(ATTR_SELECTED_SURVEYS, selectedSurveys);
        request.setAttribute(ATTR_TOTAL_SURVEYS, totalSurveys);
        request.setAttribute(ATTR_HISTORY_LIMIT, historyLimit);
        request.getRequestDispatcher("WEB-INF/view/Details.jsp").include(request, response);
    }
}
