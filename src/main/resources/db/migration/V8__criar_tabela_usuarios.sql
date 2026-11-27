CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha_hash VARCHAR(100) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),

    CONSTRAINT ck_usuario_perfil CHECK (perfil IN ('ADMIN', 'TECNICO'))
);

CREATE UNIQUE INDEX uk_usuarios_email ON usuarios (email);