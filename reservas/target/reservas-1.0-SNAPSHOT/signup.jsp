<%-- 
    Document   : singup
    Created on : 28/03/2025, 10:07:06 p. m.
    Author     : juand
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="signup.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <title>JSP Page</title>
    </head>
    <body>
        <div class="content">
            <div class="text">
                SIGN UP
            </div>
            <form action="userController" method="POST">
                <div class="field">
                    <input type="text" name="nombre" id="correo" placeholder="nombre">
                    <span class="fas fa-user"></span>
                    <label>Email or Phone</label>
                </div>
                <div class="field">
                    <input type="text" name="correo" id="correo" placeholder="Correo">
                    <span class="fas fa-lock"></span>
                    <label>Password</label>
                </div>
                <div class="field">
                    <input type="text" name="telefono" id="telefono" placeholder="telefono">
                    <span class="fas fa-lock"></span>
                    <label>Password</label>
                </div>
                <button name="action" value="2">Sign in</button>
                <div class="sign-up">
                    <a href="index.jsp">Login</a>
                </div>
            </form>
        </div>
    </body>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        <% if (session.getAttribute("msg") != null) { %>
            Swal.fire({
                icon: 'success',
                title: '¡Éxito!',
                text: '<%= session.getAttribute("msg") %>',
                confirmButtonColor: '#28a745'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "index.jsp"; // Redirige a index.jsp después de OK
                }
            });
            <% session.removeAttribute("msg"); %> <!-- Eliminar mensaje de sesión -->
        <% } %>

        <% if (session.getAttribute("error") != null) { %>
            Swal.fire({
                icon: 'error',
                title: '¡Error!',
                text: '<%= session.getAttribute("error") %>',
                confirmButtonColor: '#d33'
            });
            <% session.removeAttribute("error"); %> <!-- Eliminar mensaje de sesión -->
        <% } %>
    });
</script>

</html>
