package question2;
import java.io.*;
import java.util.Comparator;

public class Card implements Serializable, Comparable<Card> {

    static final long serialVersionID = 100237819;
    private Rank rank;
    private Suit suit;

    public enum Rank
    {
        TWO(2), //lowest
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(10),
        QUEEN(10),
        KING(10),
        ACE(11); //highest

        private final int value;
        private static Rank[] allValues = values();

        //Associates the int value with each number
        Rank(int x){
            this.value = x;
        }
        public int getValue(){ return this.value; }

        public Rank[] getAllValues() { return this.allValues;}

        //returns previous enum value to one generated
        public Rank getPrevious(){
            return allValues[(this.ordinal() - 1) % allValues.length];
        }

        //getNext is for the cheat method in basic strategy
        public Rank getNext(){
            return allValues[(this.ordinal() + 1) % allValues.length];
        }
    }

    public enum Suit
    {
        CLUBS(1), //lowest
        DIAMONDS(2),
        HEARTS(3),
        SPADES(4); //highest

        public final int order;

        Suit(int o){ this.order = o; }
    }

    //constructor
    public Card(Rank r, Suit s) {
        this.rank = r;
        this.suit = s;
    }

    //accessors
    public Rank getRank() { return this.rank; }
    public Suit getSuit() { return this.suit; }

    //difference in ranks in terms of its position in enum
    public static int difference(Rank r1, Rank r2){
        if (r1.ordinal() > r2.ordinal()) {
            return r1.ordinal() - r2.ordinal();
        }
        else {
            return r2.ordinal() - r1.ordinal(); //reverses r1 and r2 as r2 is bigger, so no negative values
        }
    }

    public static int differenceValue(Rank r1, Rank r2){
        return Math.abs(r1.getValue() - r2.getValue());

    }
    //functional interface
    public static class CompareAscending implements Comparator<Card> {

        @Override
        public int compare(Card c1, Card c2) {
            int compare = c1.rank.ordinal() - c2.rank.ordinal(); //compares order of ranks by subtracting orders
            //if ranks are the same (0), then compare on suit
            if (compare == 0){
                compare = c1.suit.ordinal() - c2.suit.ordinal();
            }
            return compare;
        }
    }

    public static class CompareSuit implements Comparator<Card>{

        @Override
        public int compare(Card c1, Card c2) {

            return c1.suit.ordinal() - c2.suit.ordinal();
        }
    }
    //initialise list
    //iterate through list
    //lambda exp for (argument 1, argument 2) -> (body / rank comparison) ternary exp
    // perhaps use system.out in body
    public static void selectTest(Card D){
        //Correct order ascending from lowest -> highest:
        //Correct order comparing suits from lowest -> highest:
        //Correct order for lambda exp lowest -> highest:
        Card A = new Card(Rank.FOUR, Suit.SPADES);
        Card B = new Card(Rank.EIGHT, Suit.HEARTS);
        Card C = new Card(Rank.FOUR, Suit.CLUBS);
        Card[] cards = {A, B, C};

        CompareAscending compA = new CompareAscending();
        System.out.println("---Ascending Comparison---");
        for (int i = 0; i < cards.length; i++){
            int number = compA.compare(cards[i],D);

            if(number < 0){
                System.out.println(cards[i] + " is less than " + D);
            }else if(number > 0){
                System.out.println(cards[i] + " is bigger than " + D);
            }
            else {
                System.out.println(cards[i] + " is the same rank as " + D);
            }
        }

        CompareSuit compS = new CompareSuit();
        System.out.println("---Suit Comparison---");
        for (int i = 0; i < cards.length; i++){
            int number = compS.compare(cards[i], D);

            if(number < 0){
                System.out.println(cards[i] + " is less than " + D);
            }else if(number > 0){
                System.out.println(cards[i] + " is bigger than " + D);
            }
            else {
                System.out.println(cards[i] + " is the same suit as " + D);
            }
        }

        System.out.println("---Comparison Using Lambda expression---");
        //reference to a method
        Comparator<Card> lambdaCompare = (Card a, Card b) -> {
            int compare = a.rank.ordinal() - b.rank.ordinal();
            if (compare == 0){
                compare = b.suit.ordinal() - a.suit.ordinal(); //just like compareAscending but this line switches the suit subtraction so lower suit = higher card
            }
            return compare;
        };

            for (int i = 0; i < cards.length; i++) {
                if(lambdaCompare.compare(cards[i], D) < 0){
                    System.out.println(cards[i] + " is less than " + D);
                }else if(lambdaCompare.compare(cards[i], D) > 0){
                    System.out.println(cards[i] + " is bigger than " + D);
                }
                else {
                    System.out.println(cards[i] + " same as " + D);
                }
            }
    }

    @Override
    public String toString() {
        return getRank() + " of " + getSuit();
    }

    //sorts by rank first then suit, descending order
    @Override
    public int compareTo(final Card other){
        if (this.rank.ordinal() == other.rank.ordinal()){
            return this.suit.ordinal() - other.suit.ordinal(); //if rank is the same, sort order of suits
        }
        return other.rank.ordinal() - this.rank.ordinal(); //descending
    }

    public static void main(String[] args) {
        //Testing methods in Card
        Card testCard  = new Card(Rank.FOUR, Suit.HEARTS);
        Card testCard2 = new Card(Rank.KING, Suit.DIAMONDS);

        System.out.println("Rank of the test card is " + testCard.getRank());
        System.out.print("Suit of the test card is " + testCard.getSuit() + "\n");
        System.out.println("Value of test Card is " + testCard.rank.getValue());
        System.out.println("Previous rank of test card in the enum is " + testCard.rank.getPrevious());
        System.out.println("The next rank of the test card in the enum is " + testCard.rank.getNext());

        System.out.println("Test card 2 is " + testCard2);
        System.out.println("The difference in enum positions between the test cards is: " + difference(testCard.rank, testCard2.rank));
        System.out.println("The difference in values between test cards is: " + differenceValue(testCard.rank, testCard2.rank));

        //testing compare methods and lambda compare
        selectTest(testCard);

        //---HOW TO SERIALISE A CARD---
        System.out.println("-----------------How to Serialise a Card------------------------");
        //save to byte code
        String filename = "serialCard.ser";
        Card serialCard = new Card(Rank.QUEEN, Suit.SPADES);
        System.out.println("The card serialCard used for serial test is " + serialCard);

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(serialCard); //open stream, write to object, close stream
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
            Card newCard = (Card)in.readObject(); //creates a new card to compare. Read in the object, cast to Card, close stream
            in.close();
            System.out.println("A new card " + newCard + " was created and is the same as serialCard");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
