<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="entity.*" %>
    
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
<body>

    <div class="d-flex justify-content-center align-items-center page-container">
        <div class="interieurAddProduct">
            <div class="centered">
                <h1>Customer List</h1>
                <form method="POST" action="ManageFidelityPoint">
	                <table class="table">
	                    <thead>
	                        <tr>
	                            <th>ID</th>
	                            <th>Email</th>
	                            <th>Username</th>
	                            <th>Fidelity Points</th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                        <% List<Customer> customerList = (List<Customer>) request.getAttribute("customerList");
	                            for (Customer customer : customerList) {
	                        %>
	                            <tr>
	                                <td><%= customer.getId() %></td>
	                                <td><%= customer.getEmail() %>
	                                <input type="hidden" name="customerList" value="<%= customer.getEmail() %>"></td>
	                                <td><%= customer.getUsername() %></td>
	                                <td><input type="number" name="fidelityPointList" value="<%= customer.getFidelityPoint() %>"></td>
	                            </tr>
	                        <%
	                            }
	                        %>
	                    </tbody>
	                </table>
	            	<button type="submit">Confirm</button>
	          </form>
        </div>
       
    </div>
    </div>


    <%@ include file="footer.jsp"%>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous">
    </script>

</body>

</html>