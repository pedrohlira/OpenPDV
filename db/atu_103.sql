/* Atualizacao para a versao 1.03 */

/* Criando a tabela do ibpt */
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

/* Comandos para atualizacao da tabela do ibpt */
DELETE FROM IBPT;
INSERT INTO IBPT DIRECT SELECT * FROM CSVREAD('db/ibpt.csv',null,'charset=ISO-8859-1 fieldSeparator=;');

/* Criando a tabela de troca e seus produtos */
DROP TABLE IF EXISTS ecf_troca;
CREATE TABLE ecf_troca (
  ecf_troca_id int(11) NOT NULL AUTO_INCREMENT,
  ecf_troca_cliente varchar(18) NOT NULL,
  ecf_troca_data datetime NOT NULL,
  ecf_troca_valor decimal(10,2) NOT NULL,
  ecf_troca_ecf int(11) NOT NULL,
  ecf_troca_coo int(11) NOT NULL,
  ecf_troca_ativo bit(1) NOT NULL,
  PRIMARY KEY (ecf_troca_id)
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

/* Adicionando o campo de troca a venda, para caso a mesma tenha alguma vinculada */
ALTER TABLE ecf_venda ADD COLUMN ecf_troca_id int(11) DEFAULT NULL BEFORE ecf_venda_ccf;
ALTER TABLE ecf_venda ADD CONSTRAINT FK_ecf_venda_6 FOREIGN KEY (ecf_troca_id) REFERENCES ecf_troca (ecf_troca_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

/* Alterando o campo de quantidade para decimal com 4 casas */
ALTER TABLE ecf_nota_produto ALTER COLUMN ecf_nota_produto_quantidade decimal(10,4) NOT NULL;

/* Alterando o tamanho dos campos de nsu, para suportar a barra dos cheques */
ALTER TABLE ecf_pagamento ALTER COLUMN ecf_pagamento_nsu varchar(32) NOT NULL;
ALTER TABLE ecf_pagamento_parcela ALTER COLUMN ecf_pagamento_parcela_nsu varchar(32) NOT NULL;