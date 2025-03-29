/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author juand
 */
@Entity
@Table(name = "reserva")
public class Reserve implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;
   
    @Column(name = "fecha")
    private LocalDate fecha;
    @Column(name = "horaInicio")
    private LocalTime horaInicio;
    @Column(name = "horaFin")
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    // Enum para los estados de la reserva
    public enum EstadoReserva {
        PENDIENTE, CONFIRMADA, ACTIVA, CANCELADA, TERMINADA
    }

    public Reserve() {
    }

    public Reserve(int id, User user, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, EstadoReserva estado) {
        this.id = id;
        this.user = user;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reserve)) {
            return false;
        }
        Reserve other = (Reserve) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Reserve[ id=" + id + " ]";
    }
    
}
