package org.breupach.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import org.breupach.springcloud.msvc.usuarios.models.entity.Usuario;
import org.breupach.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> optionalUsuario = usuarioService.porId(id);
        if(optionalUsuario.isPresent()) {
            return ResponseEntity.ok(optionalUsuario.get());
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/usuarios")
    public ResponseEntity<?> obtenerUsuarios(@RequestParam List<Long> ids) {
        List<Usuario> usuarios = usuarioService.porIds(ids);
        if (!usuarios.isEmpty()) {
            return ResponseEntity.ok(usuarios);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (usuarioService.porEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electronico!"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        Optional<Usuario> o = usuarioService.porId(id);
        if (o.isPresent()) {
            Usuario usuarioDb = o.get();
            if (!usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail())
                    && usuarioService.porEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electronico!"));
            }
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> o = usuarioService.porId(id);
        if (o.isPresent()) {
            usuarioService.eliminar(id);
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
