CREATE TABLE planos_preventivos(
    id BIGSERIAL PRIMARY KEY,
    equipamento_id BIGINT NOT NULL REFERENCES equipamentos(id),
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    periodicidade_dias INTEGER NOT NULL,
    proxima_execucao DATE NOT NULL,
    prioridade VARCHAR(20) NOT NULL DEFAULT 'MEDIA',
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),


    CONSTRAINT ck_plano_periodicidade CHECK(periodicidade_dias > 0),
    CONSTRAINT ck_plano_prioridade CHECK(prioridade IN('BAIXA', 'MEDIA', 'ALTA', 'URGENTE'))
);

CREATE INDEX idx_planos_equipamento_id ON planos_preventivos(equipamento_id);
CREATE INDEX idx_planos_ativo_proxima ON planos_preventivos(ativo, proxima_execucao);