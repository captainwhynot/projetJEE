<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
<%
if (isLogged && (loginUser.getTypeUser().equals("Administrator") || loginUser.getTypeUser().equals("Moderator")) ) {
%>
<body>

    <div class="d-flex justify-content-center align-items-center page-container" id="addPD">
        <div class="interieurAddProduct">
            <h1 class="text-center">Add a product</h1>
            <form method="post" class="text-center" action="AddProduct">
				
                <div class="labName">
                    <label for="name" class="form-label">Name</label>
                    <input type="text" class="form-control" id="name" name="name"
                        placeholder="Enter the name of the product">
                </div>

                <div class="labName">
                    <label for="price" class="form-label">Price</label>
                    <input type="number" step="0.01" class="form-control" id="price" name="price"
                        placeholder="Enter price"></div>

                <div class="labName">
                    <label for="stock" class="form-label">Stock</label>
                    <input type="number" step="1" class="form-control" id="stock" name="stock"
                        placeholder="Enter stock"></div>

                <div class="labName">
                    <label for="img" class="form-label">Image</label>
                    <input type="file" id="img" name="img" accept="image/png, image/jpeg" required>
                </div>

                <button type="submit"
                    class="btn mt-5 rounded-pill btn-lg btn-custom2 btn-block text-uppercase">Submit</button>
            </form>
        </div>
    </div>


    <%@ include file="footer.jsp"%>
</body>
<%
} else {
%><script type="text/javascript">showAlert();</script>
<%
}
%>
</html>