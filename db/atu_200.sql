/* Atualizacao para a versao 2.00 */

/* Adicionando o campo de ead em todas as tabelas */
ALTER TABLE ecf_documento ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_impressora ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_nota ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_nota_eletronica ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_nota_produto ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_pagamento ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_pagamento_parcela ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_pagamento_tipo ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_pagamento_totais ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_troca ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_troca_produto ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_venda ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_venda_produto ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_z ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ecf_z_totais ADD COLUMN ead varchar(260) NULL;
ALTER TABLE prod_composicao ADD COLUMN ead varchar(260) NULL;
ALTER TABLE prod_embalagem ADD COLUMN ead varchar(260) NULL;
ALTER TABLE prod_grade ADD COLUMN ead varchar(260) NULL;
ALTER TABLE prod_grade_tipo ADD COLUMN ead varchar(260) NULL;
ALTER TABLE prod_preco ADD COLUMN ead varchar(260) NULL;
ALTER TABLE prod_produto ADD COLUMN ead varchar(260) NULL;
ALTER TABLE sis_cliente ADD COLUMN ead varchar(260) NULL;
ALTER TABLE sis_empresa ADD COLUMN ead varchar(260) NULL;
ALTER TABLE sis_estado ADD COLUMN ead varchar(260) NULL;
ALTER TABLE sis_municipio ADD COLUMN ead varchar(260) NULL;
ALTER TABLE sis_usuario ADD COLUMN ead varchar(260) NULL;
ALTER TABLE ibpt ADD COLUMN ead varchar(260) NULL;
