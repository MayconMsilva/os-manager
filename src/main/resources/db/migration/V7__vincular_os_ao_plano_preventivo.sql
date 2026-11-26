ALTER TABLE ordens_servico
    ADD COLUMN plano_preventivo_id BIGINT REFERENCES planos_preventivos (id);

CREATE INDEX idx_os_plano_preventivo_id ON ordens_servico (plano_preventivo_id);



CREATE UNIQUE INDEX uk_os_plano_em_aberto
    ON ordens_servico (plano_preventivo_id)
    WHERE status IN ('ABERTA', 'EM_ANDAMENTO');