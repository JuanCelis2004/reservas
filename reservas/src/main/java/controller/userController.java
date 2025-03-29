/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import persistence.UserJpaController;

/**
 *
 * @author juand
 */
@WebServlet(name = "userController", urlPatterns = {"/userController"})
public class userController extends HttpServlet {

    UserJpaController userJpa = new UserJpaController();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String correo = request.getParameter("correo");

        switch (action) {
            case "1":
                // Obtener los valores del formulario

                // Validar que los campos no sean nulos o vacíos
                if (correo == null || correo.trim().isEmpty()) {
                    request.getSession().setAttribute("error", "El correo es obligatorio.");
                    response.sendRedirect("index.jsp");
                    break;
                }

                User usuario = comprobarAcceso(correo);

                if (usuario != null) {
                    HttpSession misession = request.getSession(true);
                    misession.setAttribute("user", usuario); // ✅ Guarda el usuario completo en la sesión
                    response.sendRedirect("reservas.jsp");
                } else {
                    request.getSession().setAttribute("error", "El correo ingresado no está registrado.");
                    response.sendRedirect("index.jsp");
                }
                break;
            case "2":
                String nombre = request.getParameter("nombre");
                String telefono = request.getParameter("telefono");

                // Validar que ningún campo sea vacío
                if (nombre == null || nombre.trim().isEmpty()
                        || correo == null || correo.trim().isEmpty()
                        || telefono == null || telefono.trim().isEmpty()) {

                    HttpSession session = request.getSession();
                    session.setAttribute("error", "Todos los campos son obligatorios");
                    response.sendRedirect("signup.jsp");
                    return;
                }

                List<User> usuarios = userJpa.findUserEntities();
                for (User u : usuarios) {
                    if (u.getCorreo().equals(correo)) {
                        HttpSession session = request.getSession();
                        session.setAttribute("error", "El correo ya está registrado");
                        response.sendRedirect("signup.jsp");
                        return;
                    }
                }

                // Crear usuario
                User user = new User();
                user.setNombre(nombre);
                user.setCorreo(correo);
                user.setTelefono(telefono);

                try {
                    userJpa.create(user);
                    HttpSession session = request.getSession();
                    session.setAttribute("msg", "Usuario registrado con éxito");
                    response.sendRedirect("signup.jsp"); // Redirige al login
                } catch (Exception e) {
                    e.printStackTrace();
                    HttpSession session = request.getSession();
                    session.setAttribute("error", "Error al registrar usuario");
                    response.sendRedirect("signup.jsp");
                }
                break;

            default:
                throw new AssertionError();
        }

    }

    protected void crearUser(User user) {
        userJpa.create(user);
    }

    public User comprobarAcceso(String correo) {
        for (User usu : userJpa.findUserEntities()) {
            if (usu.getCorreo().equals(correo)) {
                return usu; // ✅ Devuelve el objeto User completo
            }
        }
        return null;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
