package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.DatosActualizarMedico;
import med.voll.api.medico.DatosListadoMedico;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("paciente")
public class PacienteController {
    @Autowired
    PacienteRepository repository;
    @PostMapping
    @Transactional
    public void registrar(@RequestBody @Valid DatosRegistroPaciente datos){
        repository.save(new Paciente(datos));
    }

    @GetMapping
    public Page<DatosListadoPacientes> listarPacientes(@PageableDefault(page = 0, size = 10, sort = {"nombre"}) Pageable paginacion){
        return repository.findAll(paginacion).map(DatosListadoPacientes::new);
    }

    @PutMapping
    @Transactional
    public void actualizar (@RequestBody @Valid DatosActualizarPaciente datos){
        var paciente = repository.getReferenceById(datos.id());
        paciente.actuallizarInformacion(datos);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void remover (@PathVariable Long id){
        var paciente = repository.getReferenceById(id);
        paciente.inactivar();
    }
}
