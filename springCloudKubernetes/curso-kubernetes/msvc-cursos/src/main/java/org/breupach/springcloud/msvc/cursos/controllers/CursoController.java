package org.breupach.springcloud.msvc.cursos.controllers;

import jakarta.validation.Valid;
import org.breupach.springcloud.msvc.cursos.entity.Curso;
import org.breupach.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping("/")
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> optionalCurso = cursoService.porId(id);
        if(optionalCurso.isPresent()) {
            return ResponseEntity.ok(optionalCurso.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso usuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        Optional<Curso> o = cursoService.porId(id);
        if (o.isPresent()) {
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> o = cursoService.porId(id);
        if (o.isPresent()) {
            cursoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validate(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
