CREATE TABLE os_historico_status(
    id BIGSERIAL PRIMARY KEY,
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id),
    status_anterior VARCHAR(20),
    status_novo VARCHAR(20) NOT NULL,
    alterado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),

    CONSTRAINT ck_hist_status_anterior
        CHECK (status_anterior IN ('ABERTA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA')),

    CONSTRAINT ck_hist_status_novo
        CHECK (status_novo IN ('ABERTA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA'))
);


CREATE INDEX idx_hist_os_alterado_em ON os_historico_status (ordem_servico_id, alterado_em);

INSERT INTO os_historico_status (ordem_servico_id, status_anterior, status_novo, alterado_em)
SELECT id, NULL, status, COALESCE(concluida_em, aberta_em)
FROM ordens_servico;