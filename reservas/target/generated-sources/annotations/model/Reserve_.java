package model;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Reserve.EstadoReserva;
import model.User;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2025-03-29T05:08:28")
@StaticMetamodel(Reserve.class)
public class Reserve_ { 

    public static volatile SingularAttribute<Reserve, LocalTime> horaFin;
    public static volatile SingularAttribute<Reserve, LocalDate> fecha;
    public static volatile SingularAttribute<Reserve, EstadoReserva> estado;
    public static volatile SingularAttribute<Reserve, Integer> id;
    public static volatile SingularAttribute<Reserve, User> user;
    public static volatile SingularAttribute<Reserve, LocalTime> horaInicio;

}