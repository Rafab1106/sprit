package mg.itu.prom16;

import java.io.*;

import jakarta.servlet.*; 
import jakarta.servlet.http.*; 
/**
 * FrontController
 */
public class FrontController extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            procesRequest(req,res);    
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.getMessage());
        }
        
    }
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            procesRequest(req,res);    
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    public void procesRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        try {

            PrintWriter out = res.getWriter();
            out.println("<h2> the request "+ req.getContextPath()+"</h2>");

        } catch (Exception e) {
            //TODO: handle exception
            throw e;
        }    
    }
    
}