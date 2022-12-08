package org.breupach.springcloud.msvc.usuarios.controllers;

import org.breupach.springcloud.msvc.usuarios.models.entity.Usuario;
import org.breupach.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
}
