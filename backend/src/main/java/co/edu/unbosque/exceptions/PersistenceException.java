package co.edu.unbosque.exceptions;
 

public class PersistenceException extends  RuntimeException{
    private static final long serialVersionUID=1L;
    public PersistenceException(String mensage, Throwable cause){
        super (mensage, cause);
    }

}
