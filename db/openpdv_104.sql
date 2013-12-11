/* tabelas de sistema */

DROP TABLE IF EXISTS sis_usuario;
CREATE TABLE sis_usuario (
  sis_usuario_id int(11) NOT NULL AUTO_INCREMENT,
  sis_usuario_login varchar(40) NOT NULL,
  sis_usuario_senha varchar(40) NOT NULL,
  sis_usuario_desconto int(11) NOT NULL,
  sis_usuario_ativo bit(1) NOT NULL,
  sis_usuario_caixa bit(1) NOT NULL,
  sis_usuario_gerente bit(1) NOT NULL,
  PRIMARY KEY (sis_usuario_id),
  CONSTRAINT UK_sis_usuario_1 UNIQUE (sis_usuario_login)
);

DROP TABLE IF EXISTS sis_estado;
CREATE TABLE sis_estado (
  sis_estado_id int(11) NOT NULL AUTO_INCREMENT,
  sis_estado_ibge int(11) NOT NULL,
  sis_estado_descricao varchar(100) NOT NULL,
  sis_estado_sigla char(2) NOT NULL,
  PRIMARY KEY (sis_estado_id),
  CONSTRAINT UK_sis_estado_1 UNIQUE (sis_estado_ibge)
);

DROP TABLE IF EXISTS sis_municipio;
CREATE TABLE sis_municipio (
  sis_municipio_id int(11) NOT NULL AUTO_INCREMENT,
  sis_estado_id int(11) NOT NULL,
  sis_municipio_ibge int(11) NOT NULL,
  sis_municipio_descricao varchar(100) NOT NULL,
  PRIMARY KEY (sis_municipio_id),
  CONSTRAINT UK_sis_municipio_1 UNIQUE (sis_municipio_ibge),
  CONSTRAINT FK_sis_municipio_1 FOREIGN KEY (sis_estado_id) REFERENCES sis_estado (sis_estado_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS sis_cliente;
CREATE TABLE sis_cliente (
  sis_cliente_id int(11) NOT NULL AUTO_INCREMENT,
  sis_municipio_id int(11) NOT NULL,
  sis_cliente_doc varchar(20) NOT NULL,
  sis_cliente_doc1 varchar(20) NOT NULL,
  sis_cliente_nome varchar(100) NOT NULL,
  sis_cliente_endereco varchar(100) NOT NULL,
  sis_cliente_numero int(11) NOT NULL,
  sis_cliente_complemento varchar(100) NOT NULL,
  sis_cliente_bairro varchar(100) NOT NULL,
  sis_cliente_cep varchar(9) NOT NULL,
  sis_cliente_telefone varchar(100) NOT NULL,
  sis_cliente_email varchar(100) NOT NULL,
  sis_cliente_data datetime NOT NULL,
  PRIMARY KEY (sis_cliente_id),
  CONSTRAINT FK_sis_cliente_1 FOREIGN KEY (sis_municipio_id) REFERENCES sis_municipio (sis_municipio_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS sis_empresa;
CREATE TABLE sis_empresa (
  sis_empresa_id int(11) NOT NULL AUTO_INCREMENT,
  sis_municipio_id int(11) NOT NULL,
  sis_empresa_razao varchar(100) NOT NULL,
  sis_empresa_fantasia varchar(100) NOT NULL,
  sis_empresa_cnpj varchar(14) NOT NULL,
  sis_empresa_ie varchar(14) NOT NULL,
  sis_empresa_im varchar(14) NOT NULL,
  sis_empresa_logradouro varchar(100) NOT NULL,
  sis_empresa_numero int(11) NOT NULL,
  sis_empresa_complemento varchar(50) NOT NULL,
  sis_empresa_bairro varchar(50) NOT NULL,
  sis_empresa_cep varchar(8) NOT NULL,
  sis_empresa_responsavel varchar(50) NOT NULL,
  sis_empresa_fone varchar(10) NOT NULL,
  sis_empresa_email varchar(100) NOT NULL,
  sis_empresa_contador bit(1) NOT NULL,
  PRIMARY KEY (sis_empresa_id),
  CONSTRAINT FK_sis_empresa_1 FOREIGN KEY (sis_municipio_id) REFERENCES sis_municipio (sis_municipio_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

/* tabelas de produtos */

DROP TABLE IF EXISTS prod_embalagem;
CREATE TABLE prod_embalagem (
  prod_embalagem_id int(11) NOT NULL AUTO_INCREMENT,
  prod_embalagem_nome varchar(6) NOT NULL,
  prod_embalagem_descricao varchar(100) NOT NULL,
  prod_embalagem_unidade int(11) NOT NULL,
  PRIMARY KEY (prod_embalagem_id),
  CONSTRAINT UK_prod_embalagem_1 UNIQUE (prod_embalagem_nome)
);

DROP TABLE IF EXISTS prod_produto;
CREATE TABLE prod_produto (
  prod_produto_id int(11) NOT NULL AUTO_INCREMENT,
  prod_embalagem_id int(11) NOT NULL,
  prod_produto_ncm varchar(8) NOT NULL,
  prod_produto_barra varchar(14) DEFAULT NULL,
  prod_produto_descricao varchar(100) NOT NULL,
  prod_produto_referencia varchar(20) NOT NULL,
  prod_produto_preco decimal(10,2) NOT NULL,
  prod_produto_estoque decimal(10,4) NOT NULL,
  prod_produto_tipo varchar(2) NOT NULL,
  prod_produto_origem char(1) NOT NULL,
  prod_produto_cst_cson varchar(3) NOT NULL,
  prod_produto_tributacao char(1) NOT NULL,
  prod_produto_icms decimal(4,2) NOT NULL,
  prod_produto_issqn decimal(4,2) NOT NULL,
  prod_produto_iat char(1) NOT NULL,
  prod_produto_ippt char(1) NOT NULL,
  prod_produto_cadastrado datetime NOT NULL,
  prod_produto_alterado datetime DEFAULT NULL,
  prod_produto_ativo bit(1) NOT NULL,
  PRIMARY KEY (prod_produto_id),
  CONSTRAINT FK_prod_produto_1 FOREIGN KEY (prod_embalagem_id) REFERENCES prod_embalagem (prod_embalagem_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP INDEX IF EXISTS IK_prod_produto_1;
DROP INDEX IF EXISTS IK_prod_produto_2;
DROP INDEX IF EXISTS IK_prod_produto_3;
DROP INDEX IF EXISTS IK_prod_produto_4;
DROP INDEX IF EXISTS IK_prod_produto_5;
DROP INDEX IF EXISTS IK_prod_produto_6;
DROP INDEX IF EXISTS IK_prod_produto_7;
DROP INDEX IF EXISTS IK_prod_produto_8;
CREATE INDEX IK_prod_produto_1 ON prod_produto (prod_produto_barra);
CREATE INDEX IK_prod_produto_2 ON prod_produto (prod_produto_descricao);
CREATE INDEX IK_prod_produto_3 ON prod_produto (prod_produto_referencia);
CREATE INDEX IK_prod_produto_4 ON prod_produto (prod_produto_preco);
CREATE INDEX IK_prod_produto_5 ON prod_produto (prod_produto_estoque);
CREATE INDEX IK_prod_produto_6 ON prod_produto (prod_produto_cadastrado);
CREATE INDEX IK_prod_produto_7 ON prod_produto (prod_produto_alterado);
CREATE INDEX IK_prod_produto_8 ON prod_produto (prod_produto_ativo);

DROP TABLE IF EXISTS prod_preco;
CREATE TABLE prod_preco (
  prod_preco_id int(11) NOT NULL AUTO_INCREMENT,
  prod_produto_id int(11) NOT NULL,
  prod_embalagem_id int(11) NOT NULL,
  prod_preco_valor decimal(10,2) NOT NULL,
  prod_preco_barra varchar(14) DEFAULT NULL,
  PRIMARY KEY (prod_preco_id),
  CONSTRAINT FK_prod_preco_1 FOREIGN KEY (prod_produto_id) REFERENCES prod_produto (prod_produto_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_prod_preco_2 FOREIGN KEY (prod_embalagem_id) REFERENCES prod_embalagem (prod_embalagem_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS prod_composicao;
CREATE TABLE prod_composicao (
  prod_composicao_id int(11) NOT NULL AUTO_INCREMENT,
  prod_produto_principal int(11) NOT NULL,
  prod_produto_id int(11) NOT NULL,
  prod_embalagem_id int(11) NOT NULL,
  prod_composicao_quantidade decimal(10,4) NOT NULL,
  prod_composicao_valor decimal(10,2) NOT NULL,
  PRIMARY KEY (prod_composicao_id),
  CONSTRAINT FK_prod_composicao_1 FOREIGN KEY (prod_produto_principal) REFERENCES prod_produto (prod_produto_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_prod_composicao_2 FOREIGN KEY (prod_produto_id) REFERENCES prod_produto (prod_produto_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_prod_composicao_3 FOREIGN KEY (prod_embalagem_id) REFERENCES prod_embalagem (prod_embalagem_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS prod_grade_tipo;
CREATE TABLE prod_grade_tipo (
  prod_grade_tipo_id int NOT NULL AUTO_INCREMENT,
  prod_grade_tipo_nome varchar(50) NOT NULL,
  prod_grade_tipo_opcao char(1) NOT NULL,
  PRIMARY KEY (prod_grade_tipo_id) 
);

DROP TABLE IF EXISTS prod_grade;
CREATE  TABLE prod_grade (
  prod_grade_id int NOT NULL AUTO_INCREMENT,
  prod_produto_id int NOT NULL,
  prod_grade_barra varchar(14) NOT NULL,
  prod_grade_tamanho varchar(50) NOT NULL,
  prod_grade_cor varchar(50) NOT NULL,
  prod_grade_opcao varchar(50) NOT NULL,
  prod_grade_estoque decimal(10,4) NOT NULL,
  PRIMARY KEY (prod_grade_id),
  CONSTRAINT FK_prod_grade_1 FOREIGN KEY (prod_produto_id) REFERENCES prod_produto (prod_produto_id) ON DELETE CASCADE ON UPDATE CASCADE
);

/* tabelas do ecf */

DROP TABLE IF EXISTS ecf_impressora;
CREATE TABLE ecf_impressora (
  ecf_impressora_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_impressora_codigo varchar(2) NOT NULL,
  ecf_impressora_mfadicional varchar(1) NOT NULL,
  ecf_impressora_identificacao varchar(6) NOT NULL,
  ecf_impressora_serie varchar(20) NOT NULL,
  ecf_impressora_tipo varchar(7) NOT NULL,
  ecf_impressora_marca varchar(20) NOT NULL,
  ecf_impressora_modelo varchar(20) NOT NULL,
  ecf_impressora_caixa int(3) NOT NULL,
  ecf_impressora_ativo bit(1) NOT NULL,
  PRIMARY KEY (ecf_impressora_id)
);

DROP TABLE IF EXISTS ecf_nota_eletronica;
CREATE TABLE ecf_nota_eletronica (
  ecf_nota_eletronica_id int(11) NOT NULL AUTO_INCREMENT,
  sis_cliente_id int(11) DEFAULT NULL,
  ecf_nota_eletronica_status varchar(15) NOT NULL,
  ecf_nota_eletronica_numero int(11) NOT NULL,
  ecf_nota_eletronica_data datetime NOT NULL,
  ecf_nota_eletronica_valor decimal(10,2) NOT NULL,
  ecf_nota_eletronica_chave varchar(44) NOT NULL,
  ecf_nota_eletronica_protocolo varchar(15) NOT NULL,
  ecf_nota_eletronica_icms decimal(10,2) NOT NULL,
  ecf_nota_eletronica_ipi decimal(10,2) NOT NULL,
  ecf_nota_eletronica_pis decimal(10,2) NOT NULL,
  ecf_nota_eletronica_cofins decimal(10,2) NOT NULL,
  ecf_nota_eletronica_xml longtext NOT NULL,
  ecf_nota_eletronica_protocolo_cancelado varchar(15) NOT NULL,
  ecf_nota_eletronica_xml_cancelado text NOT NULL,
  ecf_nota_eletronica_recibo varchar(15) NOT NULL,
  PRIMARY KEY (ecf_nota_eletronica_id),
  CONSTRAINT UK_ecf_nota_eletronica_1 UNIQUE (ecf_nota_eletronica_chave),
  CONSTRAINT FK_ecf_nota_eletronica_1 FOREIGN KEY (sis_cliente_id) REFERENCES sis_cliente (sis_cliente_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_nota;
CREATE TABLE ecf_nota (
  ecf_nota_id int(11) NOT NULL AUTO_INCREMENT,
  sis_cliente_id int(11) DEFAULT NULL,
  ecf_nota_serie varchar(3) NOT NULL,
  ecf_nota_subserie varchar(3) NOT NULL,
  ecf_nota_numero int(11) NOT NULL,
  ecf_nota_data date NOT NULL,
  ecf_nota_bruto decimal(10,2) NOT NULL,
  ecf_nota_desconto decimal(10,2) NOT NULL,
  ecf_nota_liquido decimal(10,2) NOT NULL,
  ecf_nota_pis decimal(10,2) NOT NULL,
  ecf_nota_cofins decimal(10,2) NOT NULL,
  ecf_nota_cancelada bit(1) NOT NULL,
  PRIMARY KEY (ecf_nota_id),
  CONSTRAINT UK_ecf_nota_1 UNIQUE (ecf_nota_serie,ecf_nota_subserie,ecf_nota_numero),
  CONSTRAINT FK_ecf_nota_1 FOREIGN KEY (sis_cliente_id) REFERENCES sis_cliente (sis_cliente_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_nota_produto;
CREATE TABLE ecf_nota_produto (
  ecf_nota_produto_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_nota_id int(11) NOT NULL,
  prod_produto_id int(11) NOT NULL,
  prod_embalagem_id int(11) NOT NULL,
  ecf_nota_produto_barra varchar(14) NULL DEFAULT NULL,
  ecf_nota_produto_quantidade decimal(10,4) NOT NULL,
  ecf_nota_produto_bruto decimal(10,2) NOT NULL,
  ecf_nota_produto_desconto decimal(10,2) NOT NULL,
  ecf_nota_produto_liquido decimal(10,2) NOT NULL,
  ecf_nota_produto_icms decimal(4,2) NOT NULL,
  ecf_nota_produto_ipi decimal(4,2) NOT NULL,
  ecf_nota_produto_ordem int(11) NOT NULL,
  PRIMARY KEY (ecf_nota_produto_id),
  CONSTRAINT FK_ecf_nota_produto_1 FOREIGN KEY (ecf_nota_id) REFERENCES ecf_nota (ecf_nota_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_nota_produto_2 FOREIGN KEY (prod_produto_id) REFERENCES prod_produto (prod_produto_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_nota_produto_3 FOREIGN KEY (prod_embalagem_id) REFERENCES prod_embalagem (prod_embalagem_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_troca;
CREATE TABLE ecf_troca (
  ecf_troca_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_venda_id int(11) DEFAULT NULL,
  ecf_troca_cliente varchar(18) NOT NULL,
  ecf_troca_data datetime NOT NULL,
  ecf_troca_valor decimal(10,2) NOT NULL,
  ecf_troca_ecf int(11) NOT NULL,
  ecf_troca_coo int(11) NOT NULL,
  ecf_troca_ativo bit(1) NOT NULL,
  PRIMARY KEY (ecf_troca_id),
  CONSTRAINT FK_ecf_troca_1 FOREIGN KEY (ecf_venda_id) REFERENCES ecf_venda (ecf_venda_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_troca_produto;
CREATE TABLE ecf_troca_produto (
  ecf_troca_produto_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_troca_id int(11) NOT NULL,
  prod_produto_id int(11) NOT NULL,
  prod_embalagem_id int(11) NOT NULL,
  ecf_troca_produto_barra varchar(14) NULL DEFAULT NULL,
  ecf_troca_produto_quantidade decimal(10,4) NOT NULL,
  ecf_troca_produto_valor decimal(10,2) NOT NULL,
  ecf_troca_produto_total decimal(10,2) NOT NULL,
  ecf_troca_produto_ordem int(11) NOT NULL,
  PRIMARY KEY (ecf_troca_produto_id),
  CONSTRAINT FK_ecf_troca_produto_1 FOREIGN KEY (ecf_troca_id) REFERENCES ecf_troca (ecf_troca_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_ecf_troca_produto_2 FOREIGN KEY (prod_produto_id) REFERENCES prod_produto (prod_produto_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_troca_produto_3 FOREIGN KEY (prod_embalagem_id) REFERENCES prod_embalagem (prod_embalagem_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_z;
CREATE TABLE ecf_z (
  ecf_z_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_impressora_id int(11) NOT NULL,
  ecf_z_usuario int(11) NOT NULL,
  ecf_z_crz int(11) NOT NULL,
  ecf_z_coo_ini int(11) NOT NULL,
  ecf_z_coo_fin int(11) NOT NULL,
  ecf_z_cro int(11) NOT NULL,
  ecf_z_movimento date NOT NULL,
  ecf_z_emissao datetime NOT NULL,
  ecf_z_bruto decimal(14,2) NOT NULL,
  ecf_z_gt decimal(14,2) NOT NULL,
  ecf_z_issqn bit(1) NOT NULL,
  PRIMARY KEY (ecf_z_id),
  CONSTRAINT FK_ecf_z_1 FOREIGN KEY (ecf_impressora_id) REFERENCES ecf_impressora (ecf_impressora_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_z_totais;
CREATE TABLE ecf_z_totais (
  ecf_z_totais_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_z_id int(11) NOT NULL,
  ecf_z_totais_codigo varchar(7) NOT NULL,
  ecf_z_totais_valor decimal(13,2) NOT NULL,
  PRIMARY KEY (ecf_z_totais_id),
  CONSTRAINT UK_ecf_z_totais_1 UNIQUE (ecf_z_id, ecf_z_totais_codigo),
  CONSTRAINT FK_z_totais_1 FOREIGN KEY (ecf_z_id) REFERENCES ecf_z (ecf_z_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_documento;
CREATE TABLE ecf_documento (
  ecf_documento_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_impressora_id int(11) NOT NULL,
  ecf_z_id int(11) DEFAULT NULL,
  ecf_documento_usuario int(11) NOT NULL,
  ecf_documento_coo int(11) NOT NULL,
  ecf_documento_gnf int(11) NOT NULL,
  ecf_documento_grg int(11) NOT NULL,
  ecf_documento_cdc int(11) NOT NULL,
  ecf_documento_tipo varchar(2) NOT NULL,
  ecf_documento_data datetime NOT NULL,
  PRIMARY KEY (ecf_documento_id),
  CONSTRAINT FK_ecf_documento_1 FOREIGN KEY (ecf_impressora_id) REFERENCES ecf_impressora (ecf_impressora_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_documento_2 FOREIGN KEY (ecf_z_id) REFERENCES ecf_z (ecf_z_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_venda;
CREATE TABLE ecf_venda (
  ecf_venda_id int(11) NOT NULL AUTO_INCREMENT,
  sis_usuario_id int(11) NOT NULL,
  sis_vendedor_id int(11) DEFAULT NULL,
  sis_gerente_id int(11) DEFAULT NULL,
  sis_cliente_id int(11) DEFAULT NULL,
  ecf_impressora_id int(11) NOT NULL,
  ecf_z_id int(11) DEFAULT NULL,
  ecf_venda_ccf int(6) NOT NULL,
  ecf_venda_coo int(6) NOT NULL,
  ecf_venda_data datetime NOT NULL,
  ecf_venda_bruto decimal(10,2) NOT NULL,
  ecf_venda_desconto decimal(10,2) NOT NULL,
  ecf_venda_acrescimo decimal(10,2) NOT NULL,
  ecf_venda_liquido decimal(10,2) NOT NULL,
  ecf_venda_fechada bit(1) NOT NULL,
  ecf_venda_cancelada bit(1) NOT NULL,
  ecf_venda_sinc bit(1) NOT NULL,
  ecf_venda_observacao varchar(255) NULL,
  PRIMARY KEY (ecf_venda_id),
  CONSTRAINT FK_ecf_venda_1 FOREIGN KEY (sis_usuario_id) REFERENCES sis_usuario (sis_usuario_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_venda_2 FOREIGN KEY (ecf_z_id) REFERENCES ecf_z (ecf_z_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_venda_3 FOREIGN KEY (sis_cliente_id) REFERENCES sis_cliente (sis_cliente_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_venda_4 FOREIGN KEY (sis_vendedor_id) REFERENCES sis_usuario (sis_usuario_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_venda_5 FOREIGN KEY (sis_gerente_id) REFERENCES sis_usuario (sis_usuario_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_venda_6 FOREIGN KEY (ecf_impressora_id) REFERENCES ecf_impressora (ecf_impressora_id) ON DELETE NO ACTION ON UPDATE NO ACTION
 );

DROP TABLE IF EXISTS ecf_venda_produto;
CREATE TABLE ecf_venda_produto (
  ecf_venda_produto_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_venda_id int(11) NOT NULL,
  prod_embalagem_id int(11) NOT NULL,
  prod_produto_id int(11) NOT NULL,
  ecf_venda_produto_barra varchar(14) NULL DEFAULT NULL,
  ecf_venda_produto_cst_cson varchar(3) NOT NULL,
  ecf_venda_produto_tributacao char(1) NOT NULL,
  ecf_venda_produto_icms decimal(4,2) NOT NULL,
  ecf_venda_produto_issqn decimal(4,2) NOT NULL,
  ecf_venda_produto_quantidade decimal(10,4) NOT NULL,
  ecf_venda_produto_bruto decimal(10,2) NOT NULL,
  ecf_venda_produto_desconto decimal(10,2) NOT NULL,
  ecf_venda_produto_acrescimo decimal(10,2) NOT NULL,
  ecf_venda_produto_liquido decimal(10,2) NOT NULL,
  ecf_venda_produto_total decimal(10,2) NOT NULL,
  ecf_venda_produto_cancelado bit(1) NOT NULL,
  ecf_venda_produto_ordem int(11) NOT NULL,
  PRIMARY KEY (ecf_venda_produto_id),
  CONSTRAINT FK_ecf_venda_produto_1 FOREIGN KEY (ecf_venda_id) REFERENCES ecf_venda (ecf_venda_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_venda_produto_2 FOREIGN KEY (prod_embalagem_id) REFERENCES prod_embalagem (prod_embalagem_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_venda_produto_3 FOREIGN KEY (prod_produto_id) REFERENCES prod_produto (prod_produto_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_pagamento_tipo;
CREATE TABLE ecf_pagamento_tipo (
  ecf_pagamento_tipo_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_pagamento_tipo_codigo varchar(2) NOT NULL,
  ecf_pagamento_tipo_descricao varchar(20) NOT NULL,
  ecf_pagamento_tipo_tef bit(1) NOT NULL,
  ecf_pagamento_tipo_vinculado bit(1) NOT NULL,
  ecf_pagamento_tipo_debito bit(1) NOT NULL,
  ecf_pagamento_tipo_rede varchar(20) NOT NULL,
  PRIMARY KEY (ecf_pagamento_tipo_id)
);

DROP TABLE IF EXISTS ecf_pagamento;
CREATE TABLE ecf_pagamento (
  ecf_pagamento_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_venda_id int(11) NOT NULL,
  ecf_pagamento_tipo_id int(11) NOT NULL,
  ecf_pagamento_gnf int(11) NOT NULL,
  ecf_pagamento_data datetime NOT NULL,
  ecf_pagamento_valor decimal(12,2) NOT NULL,
  ecf_pagamento_nsu varchar(32) NOT NULL,
  ecf_pagamento_estorno char(1) NOT NULL,
  ecf_pagamento_estorno_gnf int(11) DEFAULT NULL,
  ecf_pagamento_estorno_data datetime DEFAULT NULL,
  ecf_pagamento_estorno_valor decimal(12,2) DEFAULT NULL,
  ecf_pagamento_estorno_nsu varchar(12) DEFAULT NULL,
  PRIMARY KEY (ecf_pagamento_id),
  CONSTRAINT FK_ecf_pagamento_1 FOREIGN KEY (ecf_venda_id) REFERENCES ecf_venda (ecf_venda_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_ecf_pagamento_2 FOREIGN KEY (ecf_pagamento_tipo_id) REFERENCES ecf_pagamento_tipo (ecf_pagamento_tipo_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_pagamento_parcela;
CREATE TABLE ecf_pagamento_parcela (
  ecf_pagamento_parcela_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_pagamento_id int(11) NOT NULL,
  ecf_pagamento_parcela_data date NOT NULL,
  ecf_pagamento_parcela_valor decimal(12,2) NOT NULL,
  ecf_pagamento_parcela_nsu varchar(32) NOT NULL,
  PRIMARY KEY (ecf_pagamento_parcela_id),
  CONSTRAINT FK_ecf_pagamento_parcela_1 FOREIGN KEY (ecf_pagamento_id) REFERENCES ecf_pagamento (ecf_pagamento_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ecf_pagamento_totais;
CREATE TABLE ecf_pagamento_totais (
  ecf_pagamento_totais_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_pagamento_tipo_id int(11) NOT NULL,
  ecf_pagamento_totais_data date NOT NULL,
  ecf_pagamento_totais_documento varchar(20) NOT NULL,
  ecf_pagamento_totais_valor decimal(12,2) NOT NULL,
  PRIMARY KEY (ecf_pagamento_totais_id),
  CONSTRAINT FK_ecf_pagamento_totais_1 FOREIGN KEY (ecf_pagamento_tipo_id) REFERENCES ecf_pagamento_tipo (ecf_pagamento_tipo_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS ibpt;
CREATE TABLE ibpt (
  ibpt_codigo varchar(10) NOT NULL,
  ibpt_ex varchar(100) NULL,
  ibpt_tabela int(11) NOT NULL,
  ibpt_descricao varchar(500) NOT NULL,
  ibpt_aliqNac decimal(10,2) NOT NULL,
  ibpt_aliqImp decimal(10,2) NOT NULL,
  ibpt_versao varchar(5) NULL
);

/* inserindo dados padroes deve-se colocar o path completo dos arquivos

INSERT INTO SIS_ESTADO DIRECT SELECT * FROM CSVREAD('db/estados.csv',null,'charset=UTF-8');
INSERT INTO SIS_MUNICIPIO DIRECT SELECT * FROM CSVREAD('db/municipios.csv',null,'charset=UTF-8');
INSERT INTO IBPT DIRECT SELECT * FROM CSVREAD('db/ibpt.csv',null,'charset=ISO-8859-1 fieldSeparator=;');

*/