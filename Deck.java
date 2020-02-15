package question2;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class Deck implements Serializable, Iterable<Card> {

    static final long serialVersionID = 100237809;
    private Card[] deck;
    private Iterator<Card> deckIt;
    private Iterator<Card> deckItOddEven;
    private int countDealt; //To keep track of number of cards dealt

    //deck initialisation already written in newDeck
    public Deck() { newDeck(); }

    //return size of current deck - how many cards
    public int size() { return this.deck.length - countDealt; }

    //reinitialise the deck SHOULD HAVE ALL POSSIBLE VALUES
    public void newDeck() {
        this.deck = new Card[52];

        int i = 0;
        for (Card.Rank r : Card.Rank.values()) {
            for (Card.Suit s : Card.Suit.values()) {
                Card c = new Card(r, s);
                deck[i] = c;
                i++;
            }
        }

        deckIt = new deckIterator(); //this gives ERRORS when trying to serialise
    }

    public Card[] getDeck() { return this.deck; }

    //remove top card from deck and return it
    public Card deal() throws deckNoNext {
        if (deckIt.hasNext()) {
            countDealt++;
            return deckIt.next();
        }

        else{
            throw new deckNoNext();
        }
    }

    //traverse deck in order of cards dealt
    public class deckIterator implements Serializable, Iterator<Card> {
        int count = 51; //start at top of deck, index 51

        //if index is in 0-51 range of deck
        @Override
        public boolean hasNext() {
            if (count >= 0) {
                return true;
            } else {
                return false;
            }
        }
        //decrement the index pointer and get the value at deck which that is
        @Override
        public Card next() {
            return deck[count--]; }
    }

    //traverse deck in order the cards are to be dealt. dont use built in list iterators .iterator, keep track of pos yourself
    @Override
    public Iterator<Card> iterator() {
        return new deckIterator();
    }
    //traverses with odd index positions then even positions
    public class OddEvenIterator implements Serializable, Iterator<Card> {
        int index = 1; //starts at odd index
        int count = 51; //end of deck

        @Override
        public boolean hasNext() {
            if (count >= 0) {
                count--; //decrement count so hasNext can become false
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Card next() {
            if (index > 52) {
                index = 0; //reset it when it traverses over the size of the array
            }
            Card cardAtIndex = deck[index];
            index = index + 2; //skip over even index positions

            return cardAtIndex;
        }
    }

    public Iterator<Card> oddEven() {
        return new OddEvenIterator();
    }

    //randomise deck
    public static void shuffle(Deck d) {
        Random random = new Random();

        for (int i = 0; i < d.getDeck().length; i++) {
            int currentCard = i + random.nextInt(d.getDeck().length - i); //card at i and a random card in the deck excluding i

            //swap elements
            Card temp = d.getDeck()[currentCard];
            d.getDeck()[currentCard] = d.getDeck()[i];
            d.getDeck()[i] = temp;
        }
    }

        public static void main(String[] args) throws deckNoNext {
            Deck d = new Deck();
            System.out.println("The complete deck of cards: \n" + Arrays.toString(d.getDeck()));
            System.out.println("The size of the deck of cards is " + d.size());

            shuffle(d);
            System.out.println("The Shuffled deck is: \n" + Arrays.toString(d.getDeck()));

            try { System.out.println("Dealing the top card from shuffled deck: \n" + d.deal());}
            catch(Exception e){ throw new deckNoNext();}
            try {
                System.out.println("Dealing next top card from shuffled deck \n" + d.deal());}
            catch (Exception e) {throw new deckNoNext();}
            System.out.println("The size of the deck of cards is now " + d.size());

            //testing odd even iterator
            System.out.println("---- Outputting the shuffled deck by only traversing the cards at odd indexes -----");
            Iterator<Card> deckOddEven = d.oddEven();
            while (deckOddEven.hasNext()){
                System.out.println(deckOddEven.next().toString());
            }

            //---HOW TO SERIALISE A DECK---
            System.out.println("-----------------How to Serialise a Deck------------------------");
            //save to byte code
            String filename = "serialDeck.ser";
            Deck serialDeck = new Deck();
            System.out.println("The deck serialDeck used for serial test is \n" + Arrays.toString(serialDeck.getDeck()));

            try {
                FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(serialDeck);
                out.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //load object from byte code
            try {
                FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream in = new ObjectInputStream(fis);
                Deck newDeck2 = (Deck) in.readObject(); //creates a new card to compare. Read in the object, cast to Card, close stream
                in.close();
                System.out.println("A new Deck \n " + Arrays.toString(newDeck2.getDeck()) + " \n was created and is the same as serialDeck");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

}

