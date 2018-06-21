package Controller;

import Dao.TareasDao;
import Models.Tareas;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import lombok.Data;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Data
@Named(value = "tareasController")
@SessionScoped
public class TareasController implements Serializable {

    DateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private List<Tareas> LstTareas = new ArrayList();
    private Tareas tarea = new Tareas();

    private ScheduleModel Calendario = new DefaultScheduleModel();
    private ScheduleEvent event = new DefaultScheduleEvent();

    private TareasDao dao = new TareasDao();

    @PostConstruct
    public void start() {
        try {
            listar();
        } catch (Exception ex) {
            Logger.getLogger(TareasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void session() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", "user");
    }

    public void listar() throws Exception {
        try {
            LstTareas = dao.listar();
            Calendario = new DefaultScheduleModel();
            for (Tareas tareas : LstTareas) {
                ScheduleEvent evento = new DefaultScheduleEvent(tareas.getDescripcion(), DateFormat.parse(tareas.getFecha()), DateFormat.parse(tareas.getFecha()), tareas.getCodigo());
                Calendario.addEvent(evento);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void actualizarEvento() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        dao.actualizar(event.getStyleClass(), event.getTitle(), DateFormat.format(calendar.getTime()));
        listar();
        limpiar();
    }

    public void ingresarEvento() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        dao.ingresar(DateFormat.format(calendar.getTime()), event.getTitle());
        listar();
        limpiar();
    }
    
    public void eliminar() throws Exception{
        dao.eliminar(event.getStyleClass());
        listar();
        limpiar();
    }

    public void limpiar() {
        tarea = new Tareas();
    }

    public void onDateSelect(SelectEvent selectEvent) throws Exception {
        event = new DefaultScheduleEvent(null, (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
//        event = new DefaultScheduleEvent(null, (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    public void onEventMove(ScheduleEntryMoveEvent event) throws Exception {
        dao.actualizarFecha(event.getScheduleEvent().getStyleClass(), DateFormat.format(event.getScheduleEvent().getStartDate()));
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }

}
