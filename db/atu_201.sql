/* Atualizacao para a versao 2.01 */

/* Removendo todos os caracetes nao numericos do campo de doc do cadastro de cliente */
UPDATE sis_cliente SET sis_cliente_doc = REGEXP_REPLACE(sis_cliente_doc, '\D', '');
/* Alterando o tamanho do campo de doc para 14 */
ALTER TABLE sis_cliente ALTER COLUMN sis_cliente_doc varchar(14) NOT NULL;
/* Remove a tabela prod_grade_tipo, nao sera mais usada */
DROP TABLE prod_grade_tipo;
