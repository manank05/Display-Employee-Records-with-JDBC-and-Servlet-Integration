import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class EmployeeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String empId = request.getParameter("empId");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            if (empId != null && !empId.trim().isEmpty()) {
                ps = conn.prepareStatement("SELECT * FROM employee WHERE EmpID = ?");
                ps.setInt(1, Integer.parseInt(empId));
            } else {
                ps = conn.prepareStatement("SELECT * FROM employee");
            }

            rs = ps.executeQuery();

            out.println("<html><body>");
            out.println("<h3>Employee Records</h3>");
            out.println("<table border='1' cellpadding='10'>");
            out.println("<tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");

            boolean found = false;
            while (rs.next()) {
                found = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("EmpID") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getDouble("Salary") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            if (!found) {
                out.println("<p>No record found for the given ID.</p>");
            }

            out.println("<br><a href='index.html'>Back to Search</a>");
            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
