package zesp03.servlet;

import zesp03.core.Database;
import zesp03.data.ControllerRow;
import zesp03.data.DeviceRow;
import zesp03.data.SurveyRow;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Details extends HttpServlet {
    public static final String PARAM_ID = "id";
    // mapuje do zesp03.data.ControllerRow
    public static final String ATTR_CONTROLLER = "zesp03.servlet.Details.ATTR_CONTROLLER";
    // mapuje do zesp03.data.DeviceRow
    public static final String ATTR_DEVICE = "zesp03.servlet.Details.ATTR_DEVICE";
    // mapuje do ArrayList<SurveyRow> posortowanej malejąco po timestamp
    public static final String ATTR_SURVEYS = "zesp03.servlet.Details.ATTR_SURVEYS";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        final ControllerRow attrController = new ControllerRow();
        final DeviceRow attrDevice = new DeviceRow();
        final ArrayList<SurveyRow> attrSurveys = new ArrayList<>();

        int deviceId;
        try {
            String paramId = request.getParameter(PARAM_ID);
            if(paramId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "id required");
                return;
            }
            deviceId = Integer.parseInt(paramId);
        }
        catch(NumberFormatException exc) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid id");
            return;
        }

        final String sql1 = "SELECT device.id AS DeviceId, device.`name` AS DeviceName, " +
                "device.is_known AS DeviceIsKnown, device.description AS DeviceDescription, " +
                "device.controller_id AS DeviceControllerId, controller.id AS ControllerId, " +
                "controller.`name` AS ControllerName, controller.ipv4 AS ControllerIPv4, " +
                "controller.description AS ControllerDescription FROM device " +
                "LEFT JOIN controller ON device.controller_id = controller.id WHERE device.id = ?";
        final String sql2 = "SELECT * FROM device_survey WHERE device_id=? ORDER BY `timestamp` DESC, id DESC";
        try( Connection con = Database.connect();
             PreparedStatement p1 = con.prepareStatement(sql1);
             PreparedStatement p2 = con.prepareStatement(sql2) ) {
            p1.setInt(1, deviceId);
            try( ResultSet r1 = p1.executeQuery() ) {
                if( ! r1.next() ) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "no such device");
                    return;
                }
                attrDevice.setId( r1.getInt("DeviceId") );
                attrDevice.setName( r1.getString("DeviceName") );
                attrDevice.setKnown( r1.getBoolean("DeviceIsKnown") );
                attrDevice.setDescription( r1.getString("DeviceDescription") );
                attrDevice.setControllerId( r1.getInt("DeviceControllerId") );
                attrController.setId( r1.getInt("ControllerId") );
                attrController.setName( r1.getString("ControllerName") );
                attrController.setIPv4( r1.getString("ControllerIPv4") );
                attrController.setDescription( r1.getString("ControllerDescription") );
            }
            p2.setInt(1, deviceId);
            try( ResultSet r2 = p2.executeQuery() ) {
                while( r2.next() ) {
                    final SurveyRow survey = new SurveyRow();
                    survey.setId( r2.getInt("id") );
                    survey.setTimestamp( r2.getInt("timestamp") );
                    survey.setEnabled( r2.getBoolean("is_enabled") );
                    survey.setClientsSum( r2.getInt("clients_sum") );
                    survey.setDeviceId( r2.getInt("device_id") );
                    attrSurveys.add(survey);
                }
            }
        }
        catch(SQLException exc) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "database error");
            return;
        }

        request.setAttribute(ATTR_CONTROLLER, attrController);
        request.setAttribute(ATTR_DEVICE, attrDevice);
        request.setAttribute(ATTR_SURVEYS, attrSurveys);
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        request.getRequestDispatcher("WEB-INF/view/Details.jsp").include(request, response);
    }
}
