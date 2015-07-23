/* Atualizacao para a versao 2.01 */

/* Removendo todos os caracetes nao numericos de alguns campos */
UPDATE sis_cliente SET sis_cliente_doc = REGEXP_REPLACE(sis_cliente_doc, '\D', '');
UPDATE sis_cliente SET sis_cliente_cep = REGEXP_REPLACE(sis_cliente_cep, '\D', '');
UPDATE sis_cliente SET sis_cliente_telefone = REGEXP_REPLACE(sis_cliente_telefone, '\D', '');
UPDATE ecf_troca SET ecf_troca_cliente = REGEXP_REPLACE(ecf_troca_cliente, '\D', '');
/* Alterando o tamanho dos campos de algunas tabelas */
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_doc varchar(14) NOT NULL;
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_doc1 varchar(14) NOT NULL;
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_endereco varchar(60) NOT NULL;
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_complemento varchar(60) NOT NULL;
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_bairro varchar(60) NOT NULL;
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_cep varchar(8) NOT NULL;
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_telefone varchar(14) NOT NULL;
ALTER TABLE ecf_troca ALTER COLUMN ecf_troca_cliente varchar(14) NOT NULL;
ALTER TABLE prod_preco ALTER COLUMN prod_preco_barra varchar(30) NOT NULL;
ALTER TABLE prod_grade ALTER COLUMN prod_grade_barra varchar(30) NOT NULL;
ALTER TABLE ecf_nota_produto ALTER COLUMN ecf_nota_produto_barra varchar(30) NULL;
ALTER TABLE ecf_troca_produto ALTER COLUMN ecf_troca_produto_barra varchar(30) NULL;
ALTER TABLE ecf_venda_produto ALTER COLUMN ecf_venda_produto_barra varchar(30) NULL;
ALTER TABLE prod_produto ALTER COLUMN prod_produto_descricao varchar(120) NOT NULL;
ALTER TABLE prod_produto ALTER COLUMN prod_produto_referencia varchar(60) NOT NULL;
/* Adicionando alguns campos nas tabelas */
ALTER TABLE sis_cliente ADD COLUMN sis_cliente_observacao varchar(255) NOT NULL BEFORE ead;
/* Remove a tabela prod_grade_tipo, nao sera mais usada */
DROP TABLE prod_grade_tipo;
