package co.edu.unbosque.exceptions;

import java.sql.SQLException;

public class PersistenceException extends  RuntimeException{
    private final long serialVersionUID=1L;
    public PersistenceException(String mensage, Throwable cause){
        super (mensage, cause);
    }

}
