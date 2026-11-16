CREATE TABLE equipamentos (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES clientes (id),
    nome VARCHAR (150),
    tipo VARCHAR (80),
    marca VARCHAR (80),
    modelo VARCHAR (80),
    numero_serie VARCHAR (80),
    localizacao VARCHAR (150),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_equipamentos_cliente_id ON equipamentos (cliente_id);
CREATE UNIQUE INDEX uk_equipamentos_numero_serie ON equipamentos (numero_serie);

