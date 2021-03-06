/* 
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package main.java.com.djrapitops.plan.api.exceptions;

/**
 * Thrown when something goes wrong with creating tables with Table#createTable.
 *
 * @author Rsl1122
 */
public class DBCreateTableException extends DatabaseInitException {

    public DBCreateTableException(String tableName, String message, Throwable cause) {
        super(tableName + ": " + message, cause);
    }

    public DBCreateTableException(Throwable cause) {
        super(cause);
    }
}