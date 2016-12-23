package zesp03.servlet;

import zesp03.core.App;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by Berent on 2016-12-11.
 */
public class showDevices extends HttpServlet {
    //==================================================================================================================
    // SERVLET METHODS
    //==================================================================================================================
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    protected void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        String html;
        try {
            html = getHTML();
        }
        catch(SQLException propagate) {
            throw new RuntimeException(propagate);
        }

        try(PrintWriter w = resp.getWriter() ) {
            w.println(html);
        }
    }

    //==================================================================================================================
    // STRUCTURE OF HTML
    //==================================================================================================================
    private String getHTML() throws SQLException {

        StringBuilder sb = new StringBuilder();

        sb.append( "<!DOCTYPE html>" );
        sb.append( "<html lang=\"pl\">" );

        sb.append( getHead() );
        sb.append( getBody() );

        sb.append( "</html>" );

        return sb.toString();
    }

    private String getHead() {

        StringBuilder sb = new StringBuilder();

        sb.append( "<head>" );

        sb.append( "<meta charset=\"utf-8\">" );
        sb.append( "<title>showDevices</title>" );

        sb.append( getStyles() );

        sb.append( "</head>" );

        return sb.toString();
    }
    private String getBody() throws SQLException {

        StringBuilder sb = new StringBuilder();

        sb.append( "<body>" );

        sb.append( getTable() );

        sb.append( "</body>" );

        return sb.toString();
    }

    //==================================================================================================================
    // TABLE
    //==================================================================================================================
    private String getTable() throws SQLException {

        StringBuilder sb = new StringBuilder();

        sb.append( "<table>" );

        sb.append( getTableHeader() );
        sb.append( getTableRows() );

        sb.append( "</table>" );

        return sb.toString();
    }

    private String getTableHeader() {

        StringBuilder sb = new StringBuilder();

        sb.append( "<tr>" );

        sb.append( "<th>ID</th>" );
        sb.append( "<th>Nazwa</th>" );
        sb.append( "<th>Ilość</th>" );
        sb.append( "<th>Stan</th>" );
        sb.append( "<th>Opis</th>" );


        sb.append( "</tr>" );

        return sb.toString();
    }
    private String getTableRows() throws SQLException {

        return App.checkDevices().stream()
                .sorted( (elem1, elem2)->Integer.compare(elem1.controller().getId(), elem2.controller().getId()) )
                .map( checkInfo -> {

                    StringBuilder sb = new StringBuilder();

                    sb.append( "<tr>" );

                    sb.append( "<td>" ).append( checkInfo.device().getId() ).append( "</td>" );
                    sb.append( "<td>" ).append( checkInfo.device().getName() ).append( "</td> " );
                    sb.append( "<td>" ).append( checkInfo.survey().getClientsSum()).append( "</td>" );
                    sb.append( "<td>" ).append( checkInfo.survey().isEnabled() ? "Włączone" : "Wyłączone" ).append( "</td>" );
                    sb.append( "<td>" ).append( checkInfo.device().getDescription() ).append( "</td>" );


                    sb.append( "</tr>" );

                    return sb.toString();
                } )
                .reduce((s1, s2) -> s1 + s2)
                .orElse("There was 0 records in the database");
    }

    //==================================================================================================================
    // STYLES
    //==================================================================================================================
    private String getStyles() {

        StringBuilder sb = new StringBuilder();

        sb.append( "<style type=\"text/css\">" );

        sb.append( getBodyStyle() );

        sb.append( getTableStyle() );
        sb.append( getTableHeaderStyle() );
        sb.append( getTableRowStyle() );

        sb.append( "</style>" );

        return sb.toString();
    }

    private String getBodyStyle() {

        StringBuilder sb = new StringBuilder();

        sb.append( "body {" );



        sb.append( "}" );

        return sb.toString();
    }

    private String getTableStyle() {

        StringBuilder sb = new StringBuilder();

        sb.append( " table { " +
                "margin:0 auto;\n" +
                "width:50%;\n" +
                "text-align: center;}" );

        sb.append( "font: 1em arial, sans-serif;" );

        sb.append( "}" );

        return sb.toString();
    }
    private String getTableHeaderStyle() {

        StringBuilder sb = new StringBuilder();

        sb.append( "th {" );

        sb.append( "margin: 2px;" );
        sb.append( "font: 1em arial, sans-serif;" );

        sb.append( "}" );

        return sb.toString();
    }
    private String getTableRowStyle() {

        StringBuilder sb = new StringBuilder();

        sb.append( "td {" );

        sb.append( "margin: 2px;" );
        sb.append( "font: 1em arial, sans-serif;" );

        sb.append( "}" );

        return sb.toString();
    }
}