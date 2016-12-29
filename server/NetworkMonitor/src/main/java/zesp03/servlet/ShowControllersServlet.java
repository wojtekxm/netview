package zesp03.servlet;

import zesp03.core.App;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;


public class ShowControllersServlet  extends HttpServlet {
    //==================================================================================================================
    // SERVLET METHODS
    //==================================================================================================================
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Powinno usuwać wszystkie kontrolery jednym zapytaniem do bazy, zamiast robienia zapytania per id kontrolera
        for(final String controllerId : Arrays.asList(request.getParameterValues("ids-to-delete"))) {
            try {
                App.removeController(Long.parseLong(controllerId));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        // TODO: Wyświetl informacje o kontrolerach które udało się usunąć, których nie itp, itd.
        handle(request, response); // Po prostu wyrenderuj pozostałe urządzenia
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    protected void handle( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        try {
            request.getSession().setAttribute("controllers", App.checkControllers() );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");

        request.getRequestDispatcher("/WEB-INF/view/ShowControllersJSP.jsp").include( request, response );
    }
}