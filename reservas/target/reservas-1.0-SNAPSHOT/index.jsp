<%-- 
    Document   : index
    Created on : 28/03/2025, 10:06:42 p. m.
    Author     : juand
--%>

<%@page import="persistence.UserJpaController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="index.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <title>JSP Page</title>
    </head>
    <body>

        <%
            UserJpaController userJPA = new UserJpaController();
        %>
        <div class="login-form">
            <div class="login-form">
                <div class="text">
                    LOGIN
                </div>
                <form action="userController" method="POST">
                    <div class="field">
                        <div class="fas fa-envelope"></div>
                        <input name="correo" type="text" placeholder="Email">
                    </div>
                    <button name="action" value="1">LOGIN</button>
                    <div class="link">
                        Not a member?
                        <a href="signup.jsp">Signup now</a>
                    </div>
                </form>
            </div>
    </body>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
        <% if (session.getAttribute("error") != null) {%>
            Swal.fire({
                icon: 'error',
                title: '¡Error!',
               text: '<%= session.getAttribute("error")%>',
                confirmButtonColor: '#d33'
            });
        <% session.removeAttribute("error"); %><!-- Eliminar mensaje de sesión después de mostrarlo -->
        <% }%>
        });
    </script>
</html>
