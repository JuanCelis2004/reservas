/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Reserve;
import model.Reserve.EstadoReserva;
import model.User;
import persistence.ReserveJpaController;
import persistence.UserJpaController;
import persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author juand
 */
@WebServlet(name = "reserveController", urlPatterns = {"/reserveController"})
public class reserveController extends HttpServlet {

    UserJpaController UserJPA = new UserJpaController();
    ReserveJpaController ReserveJPA = new ReserveJpaController();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("reservas.jsp?error=Acción no válida.");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp?error=La sesión ha expirado. Inicia sesión nuevamente.");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Debes iniciar sesión.");
            return;
        }

// Si el usuario está presente, establece el userId en el request
        request.setAttribute("userId", user.getId());

        try {
            switch (action) {
                case "create":
                    crearReserva(request, response, user);
                    break;
                case "edit":
                    editarReserva(request, response, user);
                    break;
                case "delete":
                    eliminarReserva(request, response, user);
                    break;
                case "cambiarEstado":
                    cambiarEstadoReserva(request, response);

                    break;
                default:
                    response.sendRedirect("reservas.jsp?error=Acción desconocida.");
            }
        } catch (Exception e) {
            response.sendRedirect("reservas.jsp?error=Ocurrió un error: " + e.getMessage());
        }
        
    }

    private void crearReserva(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        LocalTime horaInicio = LocalTime.parse(request.getParameter("horaInicio"));
        LocalTime horaFin = LocalTime.parse(request.getParameter("horaFin"));

        // Validaciones
        /*if (horaFin.isAfter(horaInicio.plusHours(2))) {
            response.sendRedirect("reservas.jsp?error=La duración no puede superar las 2 horas.");
            return;
        }*/
        // Validación: La duración no puede superar las 2 horas
        if (Duration.between(horaInicio, horaFin).toMinutes() > 120) {
            response.sendRedirect("reservas.jsp?error=Las reservas no pueden durar más de 2 horas.");
            return;
        }

        // Validación: No permitir reservas con hora de fin antes de la de inicio
        if (horaFin.isBefore(horaInicio)) {
            response.sendRedirect("reservas.jsp?error=La hora de finalización no puede ser antes que la de inicio.");
            return;
        }

        // Validación: Horario permitido de 6 AM a 10 PM
        if (horaInicio.isBefore(LocalTime.of(6, 0)) || horaFin.isAfter(LocalTime.of(22, 0))) {
            response.sendRedirect("reservas.jsp?error=Las reservas solo están permitidas entre 6 AM y 10 PM.");
            return;
        }

        List<Reserve> reservasUsuario = ReserveJPA.findReservasByUser(user.getId());
        if (reservasUsuario.stream().anyMatch(r -> r.getEstado() == EstadoReserva.ACTIVA)) {
            response.sendRedirect("reservas.jsp?error=Ya tienes una reserva activa.");
            return;
        }

        for (Reserve r : reservasUsuario) {
            if (r.getFecha().equals(fecha)
                    && r.getEstado() != EstadoReserva.CANCELADA
                    && // Ignorar reservas canceladas
                    !(horaFin.isBefore(r.getHoraInicio()) || horaInicio.isAfter(r.getHoraFin()))) {
                response.sendRedirect("reservas.jsp?error=Ya tienes una reserva en este horario.");
                return;
            }
        }

        // Crear reserva
        Reserve reserva = new Reserve();
        reserva.setUser(user);
        reserva.setFecha(fecha);
        reserva.setHoraInicio(horaInicio);
        reserva.setHoraFin(horaFin);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        ReserveJPA.create(reserva);

        response.sendRedirect("reservas.jsp?success=Reserva creada con éxito.");
    }

    private void editarReserva(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Reserve reserva = ReserveJPA.findReserve(id);

        if (reserva == null || reserva.getUser().getId() != user.getId()) {
            response.sendRedirect("reservas.jsp?error=No tienes permisos para editar esta reserva.");
            return;
        }

        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        LocalTime horaInicio = LocalTime.parse(request.getParameter("horaInicio"));
        LocalTime horaFin = LocalTime.parse(request.getParameter("horaFin"));

        if (horaFin.isAfter(horaInicio.plusHours(2))) {
            response.sendRedirect("reservas.jsp?error=La duración no puede superar las 2 horas.");
            return;
        }
        if (horaInicio.isBefore(LocalTime.of(6, 0)) || horaFin.isAfter(LocalTime.of(22, 0))) {
            response.sendRedirect("reservas.jsp?error=Las reservas solo están permitidas entre 6 AM y 10 PM.");
            return;
        }

        reserva.setFecha(fecha);
        reserva.setHoraInicio(horaInicio);
        reserva.setHoraFin(horaFin);
        try {
            ReserveJPA.edit(reserva);
        } catch (Exception ex) {
            Logger.getLogger(reserveController.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect("reservas.jsp?success=Reserva editada con éxito.");
    }

    private void eliminarReserva(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Reserve reserva = ReserveJPA.findReserve(id);

        if (reserva == null || reserva.getUser().getId() != user.getId()) {
            response.sendRedirect("reservas.jsp?error=No tienes permisos para eliminar esta reserva.");
            return;
        }

        try {
            ReserveJPA.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(reserveController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.sendRedirect("reservas.jsp?success=Reserva eliminada con éxito.");
    }

    /*private void listarReservas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Reserve> reservas = ReserveJPA.findReserveEntities();
        request.setAttribute("reservas", reservas);
        request.getRequestDispatcher("reservas.jsp").forward(request, response);
    }*/
    public void traerReservas(HttpServletRequest request) {
    List<Reserve> listarR = listar();
    HttpSession session = request.getSession();
    session.setAttribute("listaReservas", listarR);
}

    public List<Reserve> listar() {
        return ReserveJPA.findReserveEntities();
    }

    private void cambiarEstadoReserva(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    int idReserva = Integer.parseInt(request.getParameter("id"));
    EstadoReserva nuevoEstado = EstadoReserva.valueOf(request.getParameter("estado"));

    HttpSession session = request.getSession(false);
    if (session == null) {
        response.sendRedirect("login.jsp?error=La sesión ha expirado. Inicia sesión nuevamente.");
        return;
    }

    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp?error=Debes iniciar sesión.");
        return;
    }

    Reserve reserva = ReserveJPA.findReserve(idReserva);

    if (reserva == null) {
        response.sendRedirect("reservas.jsp?error=La reserva no existe.");
        return;
    }

    if (reserva.getUser().getId() != user.getId()) {
        response.sendRedirect("reservas.jsp?error=No tienes permisos para cambiar el estado de esta reserva.");
        return;
    }

    try {
        ReserveJPA.actualizarEstado(idReserva, nuevoEstado);
        response.sendRedirect("reservas.jsp?success=Estado actualizado con éxito.");
    } catch (Exception e) {
        response.sendRedirect("reservas.jsp?error=Ocurrió un error al actualizar el estado: " + e.getMessage());
    }
}

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
