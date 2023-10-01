package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository repository;
    @Autowired
    private TestEntityManager em;
    @Test
    @DisplayName("debería retornar nulo cuando el médico se encuentre" +
            " en consulta con otro paciente en ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario1() {
        //Given
        var proximoLunes = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);
        var medico = registrarMedico("Jose", "j@mail.com", "123456", Especialidad.CARDIOLOGIA);
        var paciente = registrarPaciente("Antonio", "a@mail.com", "654321");
        registrarConsulta(medico, paciente, proximoLunes);
        //When
        var medicoLibre = repository.seleccionarMedicoConEspecialidadEnFecha(
                Especialidad.CARDIOLOGIA, proximoLunes);
        //Then
        assertThat(medicoLibre).isNull();
    }
    @Test
    @DisplayName("debería retornar un medico cuando realice la consulta en la bd " +
            "para ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario2() {
        //Given
        var proximoLunes = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);
        var medico = registrarMedico("Jose", "j@mail.com", "123456", Especialidad.CARDIOLOGIA);
        //When
        var medicoLibre = repository.seleccionarMedicoConEspecialidadEnFecha(
                Especialidad.CARDIOLOGIA, proximoLunes);
        //Then
        assertThat(medicoLibre).isEqualTo(medico);
    }
    private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha){
        em.persist(new Consulta(null, medico, paciente, fecha, null));
    }
    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad){
        var medico = new Medico(datosMedico(nombre, email, documento, especialidad));
        em.persist(medico);
        return medico;
    }
    private Paciente registrarPaciente(String nombre, String email, String documento){
        var paciente = new Paciente(datosPaciente(nombre, email, documento));
        em.persist(paciente);
        return paciente;
    }

    private  DatosRegistroMedico datosMedico(String nombre, String email, String documento, Especialidad e){
        return new DatosRegistroMedico(
                nombre,
                email,
                "3447584",
                documento,
                e,
                datosDireccion()
        );
    }

    private DatosRegistroPaciente datosPaciente(String nombre, String email, String documento) {
        return new DatosRegistroPaciente(
                nombre,
                email,
                "786876975",
                documento,
                datosDireccion()
        );
    }

    private DatosDireccion datosDireccion() {
        return new DatosDireccion(
                "loca",
                "azul",
                "acapulco",
                "321",
                "12"
        );
    }
}