package question2;
import java.io.Serializable;
import java.lang.Exception;

public class deckNoNext extends Exception implements Serializable {
    public deckNoNext(){
        super ("There are no other card in Deck!");
    }

}
