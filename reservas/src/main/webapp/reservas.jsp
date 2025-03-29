<%@page import="model.Reserve.EstadoReserva"%>
<%@page import="controller.reserveController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Reserve"%>
<%@page import="model.User"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reservas</title>
        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>

    </head>
    <body>
        <% 
    // Obtener el usuario desde la sesión
    User user = (User) session.getAttribute("user");

    if (user == null) {
%>
        <p>¡Error! No has iniciado sesión correctamente.</p>
<% } else { %>
        <p>Bienvenido, <%= user.getNombre() %></p>
        <!-- Aquí puedes agregar el contenido de reservas para el usuario -->
<% } %>
        <div class="container mt-5">
            <h2 class="text-center">Gestión de Reservas</h2>
            <button class="btn btn-success mb-3" data-bs-toggle="modal" data-bs-target="#addReservaModal">Agregar Reserva</button>

            <table id="reservasTable" class="display table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Fecha</th>
                        <th>Hora Inicio</th>
                        <th>Hora Fin</th>
                        <th>Estado</th>
                        <th>Manejo de estados</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        // Llamar al método para cargar los usuarios
                        reserveController listServlet = new reserveController();
                        listServlet.traerReservas(request);
                        // Recuperar la lista de usuarios de la sesión
                        List<Reserve> listaReservas = (List<Reserve>) session.getAttribute("listaReservas");
                        int cont = 1;
                        if (listaReservas != null && !listaReservas.isEmpty()) {
                            for (Reserve reserva : listaReservas) {
                                boolean esPropietario = reserva.getUser().getId() == user.getId();
                    %>
                    <tr>
                        <td><%=cont%></td>
                        <td><%=reserva.getUser().getNombre()%></td>
                        <td><%=reserva.getFecha()%></td>
                        <td><%=reserva.getHoraInicio()%></td>
                        <td><%=reserva.getHoraFin()%></td>
                        <td><%=reserva.getEstado()%></td>
                        <td>
                            <%
                                if (esPropietario) {
                            %>
                            <form method="POST" action="reserveController">
                                <input type="hidden" name="id" value="<%= reserva.getId()%>">

                                <select name="estado" <%= (reserva.getEstado().toString().equals("TERMINADA") || reserva.getEstado().toString().equals("CANCELADA")) ? "disabled" : ""%> >
                                    <option>..</option>
                                    <option value="PENDIENTE" <%= reserva.getEstado().toString().equals("PENDIENTE") ? "selected" : ""%>>PENDIENTE</option>
                                    <option value="CONFIRMADA" <%= reserva.getEstado().toString().equals("CONFIRMADA") ? "selected" : ""%>>CONFIRMADA</option>
                                    <option value="ACTIVA" <%= reserva.getEstado().toString().equals("ACTIVA") ? "selected" : ""%>>ACTIVA</option>
                                    <option value="TERMINADA" <%= reserva.getEstado().toString().equals("TERMINADA") ? "selected" : ""%>>TERMINADA</option>
                                    <option value="CANCELADA" <%= reserva.getEstado().toString().equals("CANCELADA") ? "selected" : ""%>>CANCELADA</option>
                                </select>

                                <button 
                                    type="submit" 
                                    class="btn btn-primary" 
                                    name="action" value="cambiarEstado"
                                    <%= (reserva.getEstado().toString().equals("TERMINADA") || reserva.getEstado().toString().equals("CANCELADA")) ? "disabled" : ""%>>
                                    Actualizar
                                </button>

                            </form>
                            <%
                            } else {
                            %>
                            <!-- Si no es el propietario, mostrar solo el estado y deshabilitar la opción de cambiarlo -->
                            <span>Estado: <%= reserva.getEstado()%></span>
                            <%
                                }
                            %>
                        </td>
                        <td >
                            <a href="#" class="editU" data-toggle="modal" data-target="#editEmployeeModal"
                               data-id="<%=reserva.getId()%>"
                               data-nombre="<%=reserva.getUser().getNombre()%>"
                               data-fecha="<%=reserva.getFecha()%>"
                               data-horaInicio="<%=reserva.getHoraInicio()%>"
                               data-horaFin="<%=reserva.getHoraFin()%>"
                               data-Estado="<%=reserva.getEstado()%>">
                                <i class="material-icons" data-toggle="tooltip" title="Edit" style="color: greenyellow;">&#xE254;</i>
                            </a >
                            <!-- <a href="#deleteEmployeeModal" class="deleteU" data-toggle="modal" data-id="<%=reserva.getId()%>">
                                <i class="material-icons" data-toggle="tooltip" title="Delete" style="color: red;">&#xE872;</i>
                            </a> -->
                        </td> 
                    </tr>
                    <%
                                cont++;

                            }
                        } else {
                        }
                    %>

                </tbody>
            </table>
        </div>

        <!-- Modal Agregar Reserva -->
        <div class="modal fade" id="addReservaModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Agregar Reserva</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form action="reserveController" method="POST">
                            <input type="hidden" name="action" value="create">
                            <div class="mb-3">
                                <label class="form-label">Fecha</label>
                                <input type="date" class="form-control" name="fecha" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Hora Inicio</label>
                                <input type="time" class="form-control" name="horaInicio" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Hora Fin</label>
                                <input type="time" class="form-control" name="horaFin" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Editar Reserva -->
        <div class="modal fade" id="editReservaModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Editar Reserva</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form action="reserveController" method="post">
                            <input type="hidden" name="action" value="editar">
                            <input type="hidden" id="editId" name="id">
                            <div class="mb-3">
                                <label class="form-label">Fecha</label>
                                <input type="date" class="form-control" id="editFecha" name="fecha" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Hora Inicio</label>
                                <input type="time" class="form-control" id="editHoraInicio" name="horaInicio" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Hora Fin</label>
                                <input type="time" class="form-control" id="editHoraFin" name="horaFin" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Estado</label>
                                <select class="form-control" id="editEstado" name="estado">
                                    <option value="PENDIENTE">Pendiente</option>
                                    <option value="ACTIVA">Activa</option>
                                    <option value="CANCELADA">Cancelada</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary">Actualizar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            $(document).ready(function () {
                $('#reservasTable').DataTable();

                // Cargar datos en el modal de edición
                $('.edit-btn').click(function () {
                    $('#editId').val($(this).data('id'));
                    $('#editFecha').val($(this).data('fecha'));
                    $('#editHoraInicio').val($(this).data('horainicio'));
                    $('#editHoraFin').val($(this).data('horafin'));
                    $('#editEstado').val($(this).data('estado'));
                });
            });
        </script>
    </body>
    <%
        String success = request.getParameter("success");
        String error = request.getParameter("error");
    %>

    <script>
        // Función para obtener parámetros de la URL
        function getQueryParam(param) {
            let urlParams = new URLSearchParams(window.location.search);
            return urlParams.get(param);
        }

        // Capturar mensajes de éxito o error
        let successMessage = getQueryParam("success");
        let errorMessage = getQueryParam("error");

        // Mostrar alertas si hay mensajes
        if (successMessage) {
            Swal.fire({
                title: "Éxito",
                text: successMessage,
                icon: "success",
                confirmButtonText: "OK"
            });
        } else if (errorMessage) {
            Swal.fire({
                title: "Error",
                text: errorMessage,
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    </script>
</html>
