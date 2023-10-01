package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.desafio.ValidadorCancelamientoConsultas;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errors.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    List<ValidadorDeConsultas> validadores;

    @Autowired
    List<ValidadorCancelamientoConsultas> validadoresCancelamiento;
    public DatosDetalleConsulta agendar(DatosAgendarConsulta datos){
        if (!pacienteRepository.findById(datos.idPaciente()).isPresent()){
            throw new ValidacionDeIntegridad("Este id para el paciente no fue encontrado");
        }
        if (datos.idMedico()!=null && !medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionDeIntegridad("Este id para el médico no fue encontrado");
        }
        //validaciones
        validadores.forEach(v->v.validar(datos));
        var paciente = pacienteRepository.getReferenceById(datos.idPaciente());
        var medico = seleccionarMedico(datos);
        if (medico==null){
            throw new ValidacionDeIntegridad("No existen médicos disponibles para este " +
                    "horario y especialidad");
        }
        var consulta  = new Consulta(medico, paciente, datos.fecha());//la bd pasa el ID
        consultaRepository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }

    public void cancelar(DatosCancelamientoConsulta datos){
        if (!consultaRepository.existsById(datos.idConsulta())){
            throw new ValidacionDeIntegridad("Id de la consulta informado no existe!");
        }
        validadoresCancelamiento.forEach(v->v.validar(datos));
        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelar(datos.motivo());
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datos) {
        if (datos.idMedico()!=null){
            return medicoRepository.getReferenceById(datos.idMedico());
        }
        if (datos.especialidad()==null){
            throw new ValidacionDeIntegridad("debe seleccionar una especialidad para el médico");
        }
        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datos.especialidad(),
                datos.fecha());
    }

    public Page<DatosDetalleConsulta> consultar(Pageable paginacion){
        return consultaRepository.findAll(paginacion).map(DatosDetalleConsulta::new);
    }

}
