package zesp03.servlet;

import zesp03.core.App;
import zesp03.core.Database;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class MakeSurvey extends HttpServlet {
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_ACTION_UPDATE = "update";
    // mapowany do typu Double
    public static final String ATTR_TIME = "zesp03.servlet.MakeSurvey.ATTR_TIME";
    // mapowany do typu Integer
    public static final String ATTR_ROWS = "zesp03.servlet.MakeSurvey.ATTR_ROWS";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Double attrTime = null;
        Integer attrRows = null;

        String action = request.getParameter(PARAM_ACTION);
        try {
            if( action.equals(PARAM_ACTION_UPDATE) ) {
                long t0 = System.nanoTime();
                App.examineAll();
                long t1 = System.nanoTime();
                attrTime = (t1 - t0) * 0.000000001;

                final EntityManager em = Database.createEntityManager();
                final EntityTransaction tran = em.getTransaction();
                tran.begin();

                attrRows = ((Number)em.createQuery("SELECT COUNT(id) FROM DeviceSurvey").getSingleResult()).intValue();

                tran.commit();
                em.close();
            }
        }
        catch(SQLException exc) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "database error");
            return;
        }


        request.setAttribute(ATTR_TIME, attrTime);
        request.setAttribute(ATTR_ROWS, attrRows);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        request.getRequestDispatcher("/WEB-INF/view/MakeSurvey.jsp").include(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute(ATTR_TIME, null);
        request.setAttribute(ATTR_ROWS, null);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        request.getRequestDispatcher("/WEB-INF/view/MakeSurvey.jsp").include(request, response);
    }
}
