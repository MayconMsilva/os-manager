CREATE TABLE ordens_servico (
    id              BIGSERIAL PRIMARY KEY,
    equipamento_id  BIGINT       NOT NULL REFERENCES equipamentos (id),
    titulo          VARCHAR(150) NOT NULL,
    descricao       TEXT,
    tipo            VARCHAR(20)  NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'ABERTA',
    prioridade      VARCHAR(20)  NOT NULL DEFAULT 'MEDIA',
    aberta_em       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    concluida_em    TIMESTAMP WITH TIME ZONE,

    CONSTRAINT ck_os_tipo CHECK (tipo IN ('CORRETIVA', 'PREVENTIVA')),
    CONSTRAINT ck_os_status CHECK (status IN ('ABERTA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA')),
    CONSTRAINT ck_os_prioridade CHECK (prioridade IN ('BAIXA', 'MEDIA', 'ALTA', 'URGENTE'))
);

CREATE INDEX idx_os_equipamento_id ON ordens_servico (equipamento_id);
CREATE INDEX idx_os_status ON ordens_servico (status);