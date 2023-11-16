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
 */

@WebServlet("/Index")
public class ServletIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/index.jsp").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public static User loginUser(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("user");
		return loginUser;
	}

	public static boolean isLogged(HttpServletRequest request, HttpServletResponse response) {
		return (loginUser(request, response) != null && loginUser(request, response).getId() != 0);
	}
	
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
