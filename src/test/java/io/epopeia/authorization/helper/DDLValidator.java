package io.epopeia.authorization.helper;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

/**
 * Classe para validar a estrutura de tabelas geradas pelo hibernate afim de
 * garantir que os mapeamentos das entidades estao corretos.
 * 
 * @author Fernando Amaral
 */
public class DDLValidator {

	private DataSource dataSource;

	public DDLValidator() {
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void validateAll(String tab, String[] colE, String[] pksE,
			String[] fksE) throws SQLException {

		// Verifica a existencia da tabela
		assertTrue(
				"Tabela " + tab
						+ " nao existe. As tabelas que foram criadas sao: "
						+ Arrays.toString(listExistingTables().toArray()),
				tableExists(tab));

		// Verifica a existencia das colunas
		Object[] colA = listColumnsForTable(tab).toArray();
		assertTrue(
				"Esperava as colunas da tabela " + tab + ": "
						+ Arrays.toString(colE) + " mas existem: "
						+ Arrays.toString(colA),
				columnsExist(tab, asList(colE)));
		assertEquals("Divergencia no numero de colunas da tabela " + tab
				+ ". Esperava(" + Arrays.toString(colE) + ") mas existem("
				+ Arrays.toString(colA), colE.length, colA.length);

		// Verifica a existencia da(s) chave(s) primaria(s)
		Object[] pksA = listPrimaryKeys(tab).toArray();
		assertTrue(
				"Esperava as pks da tabela " + tab + ": "
						+ Arrays.toString(pksE) + " mas existem: "
						+ Arrays.toString(pksA),
				primaryKeyExist(tab, asList(pksE)));
		assertEquals("Divergencia no numero de pks da tabela " + tab
				+ ". Esperava(" + Arrays.toString(pksE) + ") mas existem("
				+ Arrays.toString(pksA), pksE.length, pksA.length);

		// Verifica a existencia da(s) chave(s) estrangeira(s)
		Object[] fksA = listforeignKeys(tab).toArray();
		assertTrue(
				"Esperava as fks da tabela " + tab + ": "
						+ Arrays.toString(fksE) + " mas existem: "
						+ Arrays.toString(fksA),
				foreignKeyExist(tab, asList(fksE)));
		assertEquals("Divergencia no numero de fks da tabela " + tab
				+ ". Esperava(" + Arrays.toString(fksE) + ") mas existem("
				+ Arrays.toString(fksA), fksE.length, fksA.length);
	}

	private DatabaseMetaData getMetaData() throws SQLException {
		return dataSource.getConnection().getMetaData();
	}

	private List<String> listExistingTables() throws SQLException {
		ResultSet resultSet = getMetaData().getTables(null, null, null,
				new String[] { "TABLE" });
		List<String> tables = new ArrayList<String>();
		while (resultSet.next())
			tables.add(resultSet.getString("TABLE_NAME").toLowerCase());
		resultSet.close();
		return tables;
	}

	private List<String> listColumnsForTable(String tableName)
			throws SQLException {
		List<String> columns = new ArrayList<String>();
		ResultSet resultSet = getMetaData().getColumns(null, null,
				tableName.toUpperCase(), null);
		while (resultSet.next())
			columns.add(resultSet.getString("COLUMN_NAME").toLowerCase());
		return columns;
	}

	private boolean columnsExist(String tableName, List<String> columnNames)
			throws SQLException {
		List<String> availableColumns = listColumnsForTable(tableName
				.toLowerCase());
		return availableColumns.containsAll(columnNames);
	}

	private boolean tableExists(String tableName) throws SQLException {
		List<String> tables = listExistingTables();
		return tables.contains(tableName.toLowerCase());
	}

	private List<String> listPrimaryKeys(String tableName) throws SQLException {
		ResultSet resultSet = getMetaData().getPrimaryKeys(null, null,
				tableName.toUpperCase());
		List<String> availablePks = new ArrayList<String>();
		while (resultSet.next())
			availablePks.add(resultSet.getString("COLUMN_NAME").toLowerCase());
		resultSet.close();
		return availablePks;
	}

	private boolean primaryKeyExist(String tableName, List<String> columnNames)
			throws SQLException {
		List<String> availableColumns = listPrimaryKeys(tableName.toLowerCase());
		return availableColumns.containsAll(columnNames);
	}

	private List<String> listforeignKeys(String tableName) throws SQLException {
		ResultSet resultSet = getMetaData().getImportedKeys(null, null,
				tableName.toUpperCase());
		List<String> availableFks = new ArrayList<String>();
		while (resultSet.next())
			availableFks.add(resultSet.getString("PKTABLE_NAME").toLowerCase()
					+ "." + resultSet.getString("PKCOLUMN_NAME").toLowerCase());
		resultSet.close();
		return availableFks;
	}

	private boolean foreignKeyExist(String tableName, List<String> columnNames)
			throws SQLException {
		List<String> availableColumns = listforeignKeys(tableName.toLowerCase());
		return availableColumns.containsAll(columnNames);
	}
}
