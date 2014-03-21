/* Atualizacao para a versao 1.04 */

/* Adicionando o campo de venda na troca, para que a venda possa ter mais de uma troca */
ALTER TABLE ecf_troca ADD COLUMN ecf_venda_id int(11) DEFAULT NULL BEFORE ecf_troca_cliente;
ALTER TABLE ecf_troca ADD CONSTRAINT FK_ecf_troca_1 FOREIGN KEY (ecf_venda_id) REFERENCES ecf_venda (ecf_venda_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

/* Setando as vendas nas trocas */
UPDATE ecf_troca AS t SET t.ecf_venda_id = (SELECT v.ecf_venda_id FROM ecf_venda AS v WHERE NOT v.ecf_troca_id IS NULL AND v.ecf_troca_id = t.ecf_troca_id);

/* Deletando o campo de troca da venda */
ALTER TABLE ecf_venda DROP CONSTRAINT FK_ecf_venda_6;
ALTER TABLE ecf_venda DROP COLUMN ecf_troca_id;

/* Adicionando o campo para identificar a venda ao ECF usado */
ALTER TABLE ecf_venda ADD COLUMN ecf_impressora_id int(11) NOT NULL BEFORE ecf_z_id;
UPDATE ecf_venda SET ecf_impressora_id = (SELECT ecf_impressora_id FROM ecf_impressora);
ALTER TABLE ecf_venda ADD CONSTRAINT FK_ecf_venda_6 FOREIGN KEY (ecf_impressora_id) REFERENCES ecf_impressora (ecf_impressora_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

/* Adicionando o campo de sinc na venda, pra que possa fazr o sincronismo isolado de cada venda */
ALTER TABLE ecf_venda ADD COLUMN ecf_venda_sinc bit(1) NOT NULL BEFORE ecf_venda_observacao;
UPDATE ecf_venda SET ecf_venda_sinc = 1;

/* Adicionando o campo da leituraZ no documento, para que possa ficar vinculado */
ALTER TABLE ecf_documento ADD COLUMN ecf_z_id int(11) DEFAULT NULL BEFORE ecf_documento_usuario;
ALTER TABLE ecf_documento ADD CONSTRAINT FK_ecf_documento_2 FOREIGN KEY (ecf_z_id) REFERENCES ecf_z (ecf_z_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
UPDATE ecf_documento AS d SET d.ecf_z_id = (SELECT z.ecf_z_id FROM ecf_z AS z WHERE d.ecf_documento_data >= z.ecf_z_movimento AND d.ecf_documento_data <= z.ecf_z_emissao);

/* Adicionando o campo de observacao e sinc nos clientes, para poder sincronizar isolado e aumentando o tamanho do telefone */
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_observacao varchar(255) NOT NULL;
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_sinc bit(1) NOT NULL;
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_telefone varchar(100) NOT NULL;
UPDATE sis_cliente SET sis_cliente_observacao = '', sis_cliente_sinc = 0;