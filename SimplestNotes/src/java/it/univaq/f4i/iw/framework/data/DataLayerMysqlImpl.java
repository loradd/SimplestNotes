package it.univaq.f4i.iw.framework.data;

import java.sql.Connection;
import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Giuseppe Della Penna
 */
public class DataLayerMysqlImpl implements DataLayer {

    protected DataSource datasource;
    protected Connection connection;

    public DataLayerMysqlImpl(DataSource datasource) throws SQLException, NamingException {
        this.datasource = datasource;
        this.connection = null;
    }

    @Override
    public void init() throws DataLayerException {
        try {            
            //connessione al database locale
            //database connection
            connection = datasource.getConnection();
            connection.setTransactionIsolation(TRANSACTION_READ_UNCOMMITTED);
        } catch (SQLException ex) {
            throw new DataLayerException("Error initializing data layer", ex);
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null) {
                //connection.setTransactionIsolation(TRANSACTION_READ_COMMITTED); does it really matter?! No, I suppose
                connection.close();
            }
        } catch (SQLException ex) {
            //
        }
    }
}
