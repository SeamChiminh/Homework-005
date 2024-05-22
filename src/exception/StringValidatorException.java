package exception;

public class StringValidatorException extends Exception{
    public StringValidatorException(){
        super();
    }

    public StringValidatorException(String message){
        super(message);
    }
}
