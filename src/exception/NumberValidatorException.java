package exception;

public class NumberValidatorException extends Exception{
    public NumberValidatorException(){
        super();
    }
    public NumberValidatorException(String message){
        super(message);
    }
}
