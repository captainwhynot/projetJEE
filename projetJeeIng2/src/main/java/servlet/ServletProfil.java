package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.mindrot.jbcrypt.BCrypt;

import conn.HibernateUtil;
import dao.UserDao;
import entity.User;

/**
 * Servlet implementation class ServletProfil
 *
 * This servlet manages user profile information, allowing users to view and update their profile details. It processes both GET and POST requests. The servlet handles actions such as updating the email, username, password, profile picture, and deleting the profile picture. The profil.jsp page is used to display and interact with the user's profile.
 */
@MultipartConfig
@WebServlet("/Profil")
public class ServletProfil extends HttpServlet {
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
   		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}
   		this.getServletContext().getRequestDispatcher("/profil.jsp").include(request, response);
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
   		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}
   		doGet(request, response);
   		
        // Get action and profile information parameters from the request
   		String action = request.getParameter("action");
		String profilInfo = request.getParameter("profilInfo");
		User loginUser = ServletIndex.loginUser(request, response);
		
   		if (action != null) {
   			if (action.equals("updateProfil")) {
                // Get the new value and the password
				String newValue = request.getParameter("newValueInput");
   				String password = request.getParameter("passwordInput");
   				UserDao userDao = new UserDao(HibernateUtil.getSessionFactory());
   				// Verify the password
   				if (userDao.checkUserLogin(loginUser.getEmail(), password) || profilInfo.equals("deleteProfilePicture") || profilInfo.equals("profilePicture")) {
   					// Update the information
   					boolean update = false;
			        String savePath = this.getServletContext().getRealPath("/img/Profil");
	   				switch (profilInfo) {
	   					case "email":
	   						String newEmail = newValue;
	   						loginUser.setEmail(newEmail);
	   						update = userDao.updateUser(loginUser, newEmail, loginUser.getUsername(), loginUser.getPassword());
	   						break;
	   					case "username":
	   						String newUsername = newValue;
	   						loginUser.setUsername(newUsername);
	   						update = userDao.updateUser(loginUser, loginUser.getEmail(), newUsername, loginUser.getPassword());
	   						break;
	   					case "password":
	   						String newPassword = newValue;
	   						loginUser.setPassword(newPassword);
	   						update = userDao.updateUser(loginUser, loginUser.getEmail(), loginUser.getUsername(), BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
	   						break;
	   					case "profilePicture":
	   						Part filePart = request.getPart("imgFile");
	   				        String profilePicture = ServletIndex.getSubmittedFileName(filePart);
	   				        profilePicture = loginUser.getId() + "_" + profilePicture;
	   				        loginUser.setProfilePicture("img/Profil/" + profilePicture);
	   				        update = userDao.updateProfilePicture(loginUser, filePart, profilePicture, savePath);
	   						break;
	   					case "deleteProfilePicture":
	   				        loginUser.setProfilePicture("img/profilePicture.png");
	   				        update = userDao.deleteProfilePicture(loginUser, savePath);
	   						break;
	   				}
	   				if (update) response.getWriter().println("<script>showAlert('Profil updated.', 'success', './Profil');</script>");
	   				else response.getWriter().println("<script>showAlert('Update failed.', 'error', './Profil');</script>");
   				} else {
   					response.getWriter().println("<script>showAlert('The password is incorrect.', 'error', './Profil');</script>");
   				}
   			}
		}
   	}
}
