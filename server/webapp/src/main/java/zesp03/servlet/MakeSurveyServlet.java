package zesp03.servlet;

import zesp03.common.App;
import zesp03.common.Database;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/make-survey", name = "MakeSurveyServlet")
public class MakeSurveyServlet extends HttpServlet {
    // opcjonalny, typ boolean (1/0)
    public static final String POST_UPDATE = "update";

    // mapowany do typu Double, opcjonalny
    public static final String ATTR_TIME = "zesp03.servlet.MakeSurveyServlet.ATTR_TIME";
    // mapowany do typu Long, opcjonalny
    public static final String ATTR_ROWS = "zesp03.servlet.MakeSurveyServlet.ATTR_ROWS";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Double time = null;
        Long rows = null;

        String paramUpdate = request.getParameter(POST_UPDATE);
        if (paramUpdate != null && paramUpdate.equals("1")) {
            long t0 = System.nanoTime();
            App.examineAll();
            time = (System.nanoTime() - t0) * 0.000000001;

            EntityManager em = null;
            EntityTransaction tran = null;
            try {
                em = Database.createEntityManager();
                tran = em.getTransaction();
                tran.begin();

                rows = em.createQuery("SELECT COUNT(id) FROM DeviceSurvey", Long.class).getSingleResult();

                tran.commit();
            } catch (RuntimeException exc) {
                if (tran != null && tran.isActive()) tran.rollback();
                throw exc;
            } finally {
                if (em != null) em.close();
            }
        }


        request.setAttribute(ATTR_TIME, time);
        request.setAttribute(ATTR_ROWS, rows);
        request.getRequestDispatcher("/WEB-INF/view/MakeSurvey.jsp").include(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ATTR_TIME, null);
        request.setAttribute(ATTR_ROWS, null);
        request.getRequestDispatcher("/WEB-INF/view/MakeSurvey.jsp").include(request, response);
    }
}
