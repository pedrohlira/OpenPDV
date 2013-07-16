/* Atualizacao para a versao 1.02 */

/* Deletando a constrain que proibi duplicar o codigo de barras no produto e nos precos */
ALTER TABLE prod_produto DROP CONSTRAINT UK_prod_produto_1;
ALTER TABLE prod_preco DROP CONSTRAINT UK_prod_preco_1;

/* Adicionando uma constrain que proibi duplica o codigo de sub-totais da leitura Z */
ALTER TABLE ecf_z_totais ADD CONSTRAINT UK_ecf_z_totais_1 UNIQUE (ecf_z_id, ecf_z_totais_codigo);

/* Adicionando os campos novos para a tabela de clientes */
ALTER TABLE sis_cliente ADD COLUMN sis_municipio_id int(11) NOT NULL BEFORE sis_cliente_doc;
UPDATE sis_cliente SET sis_municipio_id = 1695;
ALTER TABLE sis_cliente ADD CONSTRAINT FK_sis_cliente_1 FOREIGN KEY (sis_municipio_id) REFERENCES sis_municipio (sis_municipio_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_doc1 varchar(20) NOT NULL BEFORE sis_cliente_nome;
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_cadastrado RENAME TO sis_cliente_data;
ALTER TABLE sis_cliente DROP COLUMN sis_cliente_endereco;
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_endereco varchar(100) NOT NULL BEFORE sis_cliente_data;
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_numero int(11) NOT NULL BEFORE sis_cliente_data;
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_complemento varchar(100) NOT NULL BEFORE sis_cliente_data;
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_bairro varchar(100) NOT NULL BEFORE sis_cliente_data;
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_cep varchar(9) NOT NULL BEFORE sis_cliente_data;
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_telefone varchar(100) NOT NULL BEFORE sis_cliente_data;
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_email varchar(100) NOT NULL BEFORE sis_cliente_data;
UPDATE sis_cliente SET sis_cliente_doc = REPLACE(sis_cliente_doc,'.','');
UPDATE sis_cliente SET sis_cliente_doc = REPLACE(sis_cliente_doc,'-','');

/* Adicionando as tabelas para controle da grade dos produtos */
CREATE TABLE prod_grade_tipo (prod_grade_tipo_id int NOT NULL AUTO_INCREMENT, prod_grade_tipo_nome varchar(50) NOT NULL, prod_grade_tipo_opcao char(1) NOT NULL, PRIMARY KEY (prod_grade_tipo_id));
CREATE TABLE prod_grade (prod_grade_id int NOT NULL AUTO_INCREMENT,prod_produto_id int NOT NULL,prod_grade_barra varchar(14) NOT NULL,prod_grade_tamanho varchar(50) NOT NULL,prod_grade_cor varchar(50) NOT NULL,prod_grade_opcao varchar(50) NOT NULL,prod_grade_estoque decimal(10,4) NOT NULL,PRIMARY KEY (prod_grade_id),CONSTRAINT FK_prod_grade_1 FOREIGN KEY (prod_produto_id) REFERENCES prod_produto (prod_produto_id) ON DELETE CASCADE ON UPDATE CASCADE);

/* Removendo os campos de empresa_id que nao sao necessarios */
ALTER TABLE ecf_nota_eletronica DROP COLUMN sis_empresa_id;
ALTER TABLE ecf_nota_eletronica DROP CONSTRAINT FK_ecf_nota_eletronica_2;
ALTER TABLE ecf_nota DROP COLUMN sis_empresa_id;
ALTER TABLE ecf_nota DROP CONSTRAINT FK_ecf_nota_2;

/* Adicionando o novo campo para venda_produto saber sobre a grade */
ALTER TABLE ecf_venda_produto ADD COLUMN ecf_venda_produto_barra varchar(14) NULL DEFAULT NULL BEFORE ecf_venda_produto_cst_cson;
UPDATE ecf_venda_produto evp SET ecf_venda_produto_barra = (SELECT pp.prod_produto_barra FROM prod_produto pp WHERE evp.prod_produto_id = pp.prod_produto_id);

/* Adicionando o novo campo para nota_produto saber sobre a grade */
ALTER TABLE ecf_nota_produto ADD COLUMN ecf_nota_produto_barra varchar(14) NULL DEFAULT NULL BEFORE ecf_nota_produto_quantidade;
UPDATE ecf_nota_produto enp SET ecf_nota_produto_barra = (SELECT pp.prod_produto_barra FROM prod_produto pp WHERE enp.prod_produto_id = pp.prod_produto_id);

/* Adicionando o campo de observacao da venda da ecf */
ALTER TABLE ecf_venda ADD COLUMN ecf_venda_observacao varchar(255) NULL;
