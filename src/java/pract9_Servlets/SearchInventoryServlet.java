/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pract9_Servlets;

import inventory.Inventory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author common
 */
@WebServlet(name = "SearchInventoryServlet", urlPatterns = {"/SearchInventoryServlet"})
public class SearchInventoryServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HttpSession session = request.getSession();
            String searchTxt = request.getParameter("frmSearch");
            if (searchTxt != null) {
                int idTxt;
                String brandTxt;
                String modelTxt;

                Client client = ClientBuilder.newClient();
                WebTarget target = client
                        .target("http://localhost:8080/sep-pract9-1920s2/webresources/pract9WS/SearchInventory")  //**modify to point to your own web service
                        //.path("SearchInventory")
                        .queryParam("searchTxt", searchTxt);
                Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
                Response res = invocationBuilder.get();
                
                // 10: Check Response Object Status of OK
                if (res.getStatus() == Response.Status.OK.getStatusCode()) {
                    out.print("status: " + res.getStatus());
                    // 10a: Read entity
                    ArrayList<Inventory> al = res.readEntity(new GenericType<ArrayList<Inventory>>() {
                    });
                    out.print("======size of arraylist===>"+al.size());
                    // 11: Set Session Attribute
                    session.setAttribute("myInvListObj", al);   //write to session object 
                
                    response.sendRedirect("pract9/searchForm.jsp?msg=found");
                }
            }


//----------------------------------------------------
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
