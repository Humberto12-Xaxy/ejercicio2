package Connection;


import Entities.Registro;
import Entities.Vehiculo;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class VehiculoDAO {

    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;

    public VehiculoDAO(){
        System.err.println("Iniciando");
        try {
            Configuration configuration = new Configuration();
            System.err.println("Leyendo configuracion.");
            configuration.configure();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            factory = configuration.buildSessionFactory(serviceRegistry);
            System.out.println("Se ha establecido la conexecion");
        } catch (Throwable ex) {
            System.err.println("No se puede crear la Sesion" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void altaVehiculoOficial(String placa){
        Session session = factory.openSession();

        session.beginTransaction();

        Vehiculo vehiculo = new Vehiculo();

        vehiculo.setPlaca(placa);
        vehiculo.setTipoVehiculo(1);
        vehiculo.setTiempoEstacionado(0.0F);
        session.save(vehiculo);

        session.getTransaction().commit();
        session.close();
    }
    public void altaVehiculoResidente(String placa){
        Session session = factory.openSession();

        session.beginTransaction();

        Vehiculo vehiculo = new Vehiculo();

        vehiculo.setPlaca(placa);
        vehiculo.setTipoVehiculo(2);
        vehiculo.setTiempoEstacionado(0.0F);
        session.save(vehiculo);

        session.getTransaction().commit();
        session.close();
    }

    public void registroEntrada(String placa){

        LocalTime horaActual = LocalTime.now();

        Session session = factory.openSession();

        session.beginTransaction();

        String query = "SELECT * FROM registro WHERE placa = :numeroPlaca";
        SQLQuery sessionSQLQuery = session.createSQLQuery(query);

        sessionSQLQuery.setParameter("numeroPlaca", placa);
        sessionSQLQuery.addEntity(Registro.class);

        Registro registro = (Registro) sessionSQLQuery.uniqueResult();

        String strHoraActual = String.valueOf(horaActual);
        if (registro == null){

            Vehiculo vehiculo = getVehiculo(placa, session);

            if (vehiculo == null){

                Vehiculo vehiculoNoResidente = new Vehiculo();
                vehiculoNoResidente.setPlaca(placa);
                vehiculoNoResidente.setTipoVehiculo(3);
                vehiculoNoResidente.setTiempoEstacionado(0.0F);

                session.save(vehiculoNoResidente);
            }

            Registro newregistro = new Registro();

            newregistro.setPlaca(placa);
            newregistro.setHoraEntrada(strHoraActual);
            newregistro.setHoraSalida("0");

            session.save(newregistro);

        }else {
            String queryUpdate = "UPDATE registro SET horaEntrada = :horaEntrada, horaSalida = 0 WHERE placa = :placa";

            SQLQuery sessionSQLQueryUpdate = session.createSQLQuery(queryUpdate);
            sessionSQLQueryUpdate.setParameter("horaEntrada", strHoraActual);
            sessionSQLQueryUpdate.setParameter("placa", placa);
            sessionSQLQueryUpdate.executeUpdate();
        }
        session.getTransaction().commit();
        session.close();

    }

    public void registroSalida(String placa){

        LocalTime horaActual = LocalTime.now();
        Session session = factory.openSession();
        session.beginTransaction();

        Vehiculo vehiculo = getVehiculo(placa, session);

        switch (vehiculo.getTipoVehiculo()) {
            case 1:
                updateHoraSalida(placa, session, horaActual);
                break;
            case 2:
                updateHoraSalida(placa, session, horaActual);

                Registro registro = getRegistro(placa, session);
                LocalTime horaEntrada = LocalTime.parse(registro.getHoraEntrada());
                LocalTime horaSalida = LocalTime.parse(registro.getHoraSalida());

                Long duration = Duration.between(horaEntrada, horaSalida).toMinutes();

                float newDuration = vehiculo.getTiempoEstacionado() + duration;

                changesInTableRegistroAndVehiculo(placa, session, newDuration);

                break;
            case 3:
                updateHoraSalida(placa, session, horaActual);
                Registro registroTemp = getRegistro(placa, session);

                LocalTime horaEntradaTemp = LocalTime.parse(registroTemp.getHoraEntrada());
                LocalTime horaSalidaTemp = LocalTime.parse(registroTemp.getHoraSalida());

                Long durationTemp = Duration.between(horaEntradaTemp, horaSalidaTemp).toMinutes();
                float newDurationTemp = vehiculo.getTiempoEstacionado() + durationTemp;

                changesInTableRegistroAndVehiculo(placa, session, newDurationTemp);
                float total = newDurationTemp * 0.50F;
                System.out.println("Importe: \n Duración en estacionamiento: " + newDurationTemp + "\n Total a pagar: " + total);
                break;
        }

        session.getTransaction().commit();
        session.close();


    }

    public void comienzaMes(){
        Session session = factory.openSession();

        session.beginTransaction();

        String query = "UPDATE vehiculo SET tiempoEstacionado =" + 0.0F + " WHERE tipoVehiculo = 2";
        SQLQuery sqlQuery = session.createSQLQuery(query);

        sqlQuery.executeUpdate();


        String queryDelete = "DELETE FROM registro";

        SQLQuery sqlQueryDelete = session.createSQLQuery(queryDelete);

        sqlQueryDelete.executeUpdate();
        session.getTransaction().commit();
        session.close();

        System.out.println();
    }

    public void generateReportVehiculosRegistrados(String nombreArchivo){
        Session session = factory.openSession();
        session.beginTransaction();

        String query = "SELECT * FROM vehiculo WHERE tipoVehiculo = 2";
        SQLQuery sessionSQLQuery = session.createSQLQuery(query);

        sessionSQLQuery.addEntity(Vehiculo.class);

        List<Vehiculo> vehiculos = (List<Vehiculo>) sessionSQLQuery.list();
        System.out.println("Nombre del archivo" + nombreArchivo);
        for(Vehiculo vehiculo: vehiculos ){
            System.out.println("Núm. placa\tTiempo Estacionado(min.)\t Catidad a pagar");

            System.out.println(vehiculo.getPlaca() + "\t\t" + vehiculo.getTiempoEstacionado() + "\t\t\t\t\t\t\t" + vehiculo.getTiempoEstacionado() * 0.05);
        }

    }

    private void updateHoraSalida(String placa, Session session, LocalTime horaActual){
        try {

        String queryUpdateHoraSalida = "UPDATE registro SET horaSalida = :horaSalida WHERE placa = :placa";

        SQLQuery sessionSQLQueryUpdate = session.createSQLQuery(queryUpdateHoraSalida);

        sessionSQLQueryUpdate.setParameter("horaSalida", String.valueOf(horaActual));
        sessionSQLQueryUpdate.setParameter("placa", placa);
        sessionSQLQueryUpdate.executeUpdate();
        }catch (Throwable e){
            System.out.println("Placa no existente");
        }
    }

    private Registro getRegistro(String placa, Session session){
        try {
            String querySelectRegistro = "SELECT * FROM registro WHERE placa = :placa";
            SQLQuery sessionSQLQuerySelect = session.createSQLQuery(querySelectRegistro);

            sessionSQLQuerySelect.setParameter("placa", placa);
            sessionSQLQuerySelect.addEntity(Registro.class);

            Registro registro = (Registro) sessionSQLQuerySelect.uniqueResult();

            return registro;
        }catch (Throwable e){
            System.out.println("Registro no encontrado");
        }
    }

    private Vehiculo getVehiculo(String placa, Session session){
        try {
            String query = "SELECT * FROM vehiculo WHERE placa = :numeroPlaca";
            SQLQuery sessionSQLQuery = session.createSQLQuery(query);

            sessionSQLQuery.setParameter("numeroPlaca", placa);
            sessionSQLQuery.addEntity(Vehiculo.class);

            Vehiculo vehiculo = (Vehiculo) sessionSQLQuery.uniqueResult();

            return vehiculo;
        }catch (Throwable e){
            System.out.println("Vehiculo no encontrado");
        }
    }

    private void changesInTableRegistroAndVehiculo(String placa, Session session, float duration){
        String queryUpdateTiempoEstacionado = "UPDATE vehiculo SET tiempoEstacionado = :tiempoEstacionado WHERE placa= :placa";

        SQLQuery sessionUpdateTiempoEStacionado = session.createSQLQuery(queryUpdateTiempoEstacionado);

        sessionUpdateTiempoEStacionado.setParameter("tiempoEstacionado", duration);
        sessionUpdateTiempoEStacionado.setParameter("placa", placa);

        sessionUpdateTiempoEStacionado.executeUpdate();

        String queryDeleteRegistro = "DELETE FROM registro WHERE placa = :placa";
        SQLQuery queryDelete = session.createSQLQuery(queryDeleteRegistro);
        queryDelete.setParameter("placa", placa);

        queryDelete.executeUpdate();
    }
}
