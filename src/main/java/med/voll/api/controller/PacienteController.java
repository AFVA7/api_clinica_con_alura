package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("paciente")
public class PacienteController {
    @Autowired
    PacienteRepository repository;
    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaPaciente> registrar(@RequestBody @Valid DatosRegistroPaciente datos,
                                                          UriComponentsBuilder uriBuilder){
        Paciente paciente = repository.save(new Paciente(datos));
        var uri = uriBuilder.path("/paciente/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosRespuestaPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoPacientes>> listar(@PageableDefault(page = 0, size = 10, sort = {"nombre"}) Pageable paginacion){
        var page =repository.findAllByActivoTrue(paginacion).map(DatosListadoPacientes::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaPaciente> actualizar (@RequestBody @Valid DatosActualizarPaciente datos){
        var paciente = repository.getReferenceById(datos.id());
        paciente.actuallizarInformacion(datos);
        return ResponseEntity.ok(new DatosRespuestaPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover (@PathVariable Long id){
        var paciente = repository.getReferenceById(id);
        paciente.inactivar();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaPaciente> obtenerPaciente (@PathVariable Long id){
        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRespuestaPaciente(paciente));
    }
}
