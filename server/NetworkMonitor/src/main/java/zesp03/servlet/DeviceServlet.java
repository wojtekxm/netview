package zesp03.servlet;

import zesp03.core.Database;
import zesp03.data.ControllerData;
import zesp03.data.DeviceData;
import zesp03.data.DeviceSurveyData;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(value = "/device", name = "DeviceServlet")
public class DeviceServlet extends HttpServlet {
    // wymagany, typ long
    public static final String GET_ID = "id";
    // opcjonalny, typ int
    public static final String GET_HISTORY_LIMIT = "limit";

    // mapuje do zesp03.data.ControllerData
    public static final String ATTR_CONTROLLER = "zesp03.servlet.DeviceServlet.ATTR_CONTROLLER";
    // mapuje do zesp03.data.DeviceData
    public static final String ATTR_DEVICE = "zesp03.servlet.DeviceServlet.ATTR_DEVICE";
    // mapuje do ArrayList<DeviceSurveyData> posortowanej: na początku najnowsze
    public static final String ATTR_SELECTED_SURVEYS = "zesp03.servlet.DeviceServlet.ATTR_SELECTED_SURVEYS";
    // mapuje do Integer
    public static final String ATTR_TOTAL_SURVEYS = "zesp03.servlet.DeviceServlet.ATTR_TOTAL_SURVEYS";
    // mapuje do Integer
    public static final String ATTR_HISTORY_LIMIT = "zesp03.servlet.DeviceServlet.ATTR_HISTORY_LIMIT";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String paramId = request.getParameter(GET_ID);
        if (paramId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "id required");
            return;
        }
        long deviceId;
        try {
            deviceId = Long.parseLong(paramId);
        }
        catch(NumberFormatException exc) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid id");
            return;
        }

        final String paramHistoryLimit = request.getParameter(GET_HISTORY_LIMIT);
        int historyLimit = 100; // default value
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
        int totalSurveys = 0;
        final ArrayList<DeviceSurveyData> selectedSurveys = new ArrayList<>();
        ControllerData controllerData = null;
        DeviceData deviceData = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device device = em.find(Device.class, deviceId);
            if (device != null) {
                totalSurveys = device.getDeviceSurveys().size();
                final List<DeviceSurvey> l = device.getDeviceSurveys().stream()
                        .sorted((s1, s2) -> {
                            if (s1.getTimestamp().equals(s2.getTimestamp())) {
                                return s1.getId() > s2.getId() ? -1 : 1;
                            } else return s1.getTimestamp() > s2.getTimestamp() ? -1 : 1;
                        })
                        .limit(historyLimit)
                        .collect(Collectors.toList());
                for (DeviceSurvey ds : l) {
                    selectedSurveys.add(new DeviceSurveyData(ds));
                }
                controllerData = new ControllerData(device.getController());
                deviceData = new DeviceData(device);
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        if (deviceData == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "no such device");
            return;
        }

        request.setAttribute(ATTR_CONTROLLER, controllerData);
        request.setAttribute(ATTR_DEVICE, deviceData);
        request.setAttribute(ATTR_SELECTED_SURVEYS, selectedSurveys);
        request.setAttribute(ATTR_TOTAL_SURVEYS, totalSurveys);
        request.setAttribute(ATTR_HISTORY_LIMIT, historyLimit);
        request.getRequestDispatcher("WEB-INF/view/Device.jsp").include(request, response);
    }
}
