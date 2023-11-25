package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import entity.User;

/**
 * Servlet implementation class ServletIndex
 *
 * This servlet serves as the main entry point for the web application. It handles both GET and POST requests and includes utility methods for user authentication and file handling.
 */
@WebServlet("/Index")
public class ServletIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Handles the HTTP GET method.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException If the servlet encounters a servlet-specific problem.
     * @throws IOException      If an I/O error occurs.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/index.jsp").include(request, response);
	}

    /**
     * Handles the HTTP POST method.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException If the servlet encounters a servlet-specific problem.
     * @throws IOException      If an I/O error occurs.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

    /**
     * Retrieves the logged-in user from the session.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return The User object representing the logged-in user, or null if not logged in.
     */
	public static User loginUser(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("user");
		return loginUser;
	}

    /**
     * Checks if a user is logged in.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return True if a user is logged in, false otherwise.
     */
	public static boolean isLogged(HttpServletRequest request, HttpServletResponse response) {
		return (loginUser(request, response) != null && loginUser(request, response).getId() != 0);
	}

    /**
     * Extracts the submitted file name from a Part object.
     *
     * @param part The Part object representing the uploaded file.
     * @return The submitted file name, or null if not found.
     */
	public static String getSubmittedFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
	            if (!fileName.isEmpty()) {
	                return fileName;
	            }
	        }
	    }
        return null;
    }
}
