package zesp03.servlet;

import zesp03.common.Database;
import zesp03.entity.Link_unit_building;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/building", name = "LinkUnitBuildingServlet")
public class LinkUnitBuildingServlet extends HttpServlet {

    // mapuje do ArrayList<NazwaKlasy>
    public static final String ATTR_LIST = "zesp03.servlet.LinkUnitBuildingServlet.ATTR_LIST";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = null;
        EntityTransaction tran = null;


        List<Link_unit_building> BuildingList;

        try {

            em = Database.createEntityManager();
            tran = em.getTransaction();

            tran.begin();

            BuildingList = em.createQuery("SELECT lub FROM Link_unit_building lub", Link_unit_building.class).getResultList();

            request.setAttribute( ATTR_LIST, BuildingList);
            request.getRequestDispatcher( "/WEB-INF/view/LinkUnitBuilding.jsp" ).include( request, response );
            tran.commit();

        } catch (RuntimeException exc) {
            if( tran != null && tran.isActive() )
                tran.rollback();
            throw exc;
        } finally {
            if (em != null)
                em.close();
        }
    }
}