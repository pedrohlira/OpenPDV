/* Atualizacao para a versao 1.01 */

/* Adicionando o campo que identifica se o usuario pode logar no caixa */
ALTER TABLE sis_usuario ADD COLUMN sis_usuario_caixa bit(1) NOT NULL BEFORE sis_usuario_gerente;
UPDATE SIS_USUARIO SET sis_usuario_caixa = TRUE;

/* Adicionando o campo que identifica o vendedor da venda */
ALTER TABLE ecf_venda ADD COLUMN sis_vendedor_id int(11) DEFAULT NULL BEFORE ecf_z_id;
ALTER TABLE ecf_venda ADD CONSTRAINT FK_ecf_venda_4 FOREIGN KEY (sis_vendedor_id) REFERENCES sis_usuario (sis_usuario_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

/* Adicionando o campo que identifica o gerente do desconto */
ALTER TABLE ecf_venda ADD COLUMN sis_gerente_id int(11) DEFAULT NULL BEFORE ecf_z_id;
ALTER TABLE ecf_venda ADD CONSTRAINT FK_ecf_venda_5 FOREIGN KEY (sis_gerente_id) REFERENCES sis_usuario (sis_usuario_id) ON DELETE NO ACTION ON UPDATE NO ACTION;