package org.breupach.springcloud.msvc.cursos.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cursos_usuarios")
@Getter
@Setter
public class CursoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", unique = true)
    private Long usuarioId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CursoUsuario)) {
            return false;
        }
        return usuarioId != null && usuarioId.equals(((CursoUsuario) obj).getUsuarioId());
    }
}
