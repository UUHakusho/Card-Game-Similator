package question2;
import java.io.*;
import java.util.*;

public class Hand implements Serializable, Iterable<Card>{

    private List<Card> cards;
    static final long serialVersionID = 100237829;
    private HashMap<Card.Rank, Integer> count; //key is rank, value is the count/number of ranks

    //default
    public Hand(){
        this.cards = new ArrayList<>(); //creates empty hand
        this.count = new HashMap<>();
    }

    //Adds array of cards to Hand
    public Hand(Card[] cards) {
        this.cards = new ArrayList<>(Arrays.asList(cards));
        this.count = new HashMap<Card.Rank, Integer>();

        //loop through cards in hand and add the ranks to the hashmap
        // if hashmap already contains a card's rank then get that rank and add 1 to its counter
        int counter;
        for (Card c : cards) {
            counter = count.containsKey(c.getRank()) ? count.get(c.getRank()) : 0;   //counter = 0 if rank does not already exist, so only 1 of that rank is added to hashmap
            count.put(c.getRank(), counter + 1);
        }
    }


    public List<Card> getHand(){return cards;}
    public HashMap<Card.Rank, Integer> getCount(){return count;}

    public void addToCount(Card.Rank r) {
        //if count does not contain the rank, add the rank, or else if it already contains the rank then the add another 1
        /*if (!count.containsKey(r)){
            count.put(r, 1);
        }
        else{
            count.put(r, count.get(r.getValue()) + 1);
        }   */
        int counter;
        counter = count.containsKey(r) ? count.get(r) : 0;
        count.put(r, counter + 1);  
    }

    public void removeFromCount(Card.Rank r){
        if (count.containsKey(r)) {
            int counter;
            counter = count.containsKey(r) ? count.get(r) : 0;
            if(counter == 1){
                count.remove(r);
            } else {
                count.put(r, counter - 1);
            }
        }
    }

    //add a single card
    public void addCard(Card card){
        this.cards.add(card);
        addToCount(card.getRank());
    }


    //add a collection typed to card
    public void addCollection(Collection<Card> cards) {
        for (Card card : cards){
            addCard(card);
        }
    }

    //add a hand
    public void addHand(Hand hand){
        for (Card card : hand.getHand()){
            addCard(card);
        }
    }

    //remove single card if present return BOOL true if cards successfully removed
    public boolean removeCard(Card card){
        if (count.containsKey(card.getRank())){
            removeFromCount(card.getRank());
            getHand().remove(card);
            return true;
        }
        else{
            return false;
        }
    }

    //remove all cards from another hand passed as an argument return BOOL
    public boolean removeHand(Hand hand){
        //this.cards.removeAll(hand.getHand());
        int counter = 0;
        for (int i = 0; i < hand.getHand().size(); i++){
            //if a card is not removed a is incremented
            if(!removeCard(hand.getHand().get(i))) {
                counter++;
            }
        }
        //if a stays zero then that means a card was removed so true
        if(counter == 0) {
            return true;
        }
        return false;
    }

    //remove a card at a specific position in the hand
    public Card removeIndexCard(int index) throws IndexOutOfBoundsException {
        try {
            return getHand().remove(index);
        } catch (Exception e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    @Override
    public String toString(){
        String output = "Current Hand: ";
        //Loop through cards in hand and return string representation of each
        for (int i = 0; i < cards.size(); i++){
            output += cards.get(i).toString() + ", ";
        }
        return output;
    }

    public static void sortDescending(Hand hand){
        Collections.sort(hand.getHand());
    }

    public static void sortAscending(Hand hand){
        Collections.sort(hand.getHand(), new Card.CompareAscending());
    }

    //return number of cards for a specific rank passed in
    public int countRank(Card.Rank rank){
        if (count.containsKey(rank)){
            return count.get(rank); //returns value
        }
        else {
            return 0;
        }
    }

    //return the total rank values of cards in hand
    public int handValue(){
        int count = 0;
        for (Card c : cards){
            count += c.getRank().getValue();
        }
        return count;
    }
    //returns true if all cards in the hand are the same suit
    public boolean isFlush(){
        //for all cards in hand, if the suit of the card DOES NOT equal the hand's first card suit value, then false
        for (Card c : cards){
            if (!c.getSuit().equals(cards.get(0).getSuit())){
                return false;
            }
        }
        return true;

    }

    //returns true if all cards are in consecutive order no duplicates, so BASED ON RANK
    public boolean isStraight() {
        Collections.sort(cards, new Card.CompareAscending()); //sort cards in ascending order
        for (int i = 0; i < cards.size(); i++) {
            int difference = Card.difference(cards.get(i).getRank(), cards.get(i+1).getRank());
            if (difference != 1) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args){
        Card c1 = new Card(Card.Rank.NINE, Card.Suit.CLUBS);
        Card c2 = new Card(Card.Rank.JACK, Card.Suit.CLUBS);
        Card c3 = new Card(Card.Rank.TEN, Card.Suit.DIAMONDS);
        Card c4 = new Card(Card.Rank.TEN, Card.Suit.HEARTS);
        Card c5 = new Card(Card.Rank.TEN, Card.Suit.SPADES);
        Card[] cardArray = new Card[]{c1, c2, c3};
        Card[] cardArray2 = new Card[]{c4,c5};

        Hand hand = new Hand(cardArray);
        System.out.println(hand);
        Hand hand2 = new Hand(cardArray2);
        System.out.println("Hand 2 is " + hand2);

        Card c6 = new Card(Card.Rank.ACE, Card.Suit.HEARTS);
        hand.addCard(c6);
        System.out.println("Adding Ace of Hearts to hand: \n" + hand);
        hand.removeCard(c6);
        System.out.println("Removing Ace of Hearts from hand \n"+ hand);

        hand.addHand(hand2);
        System.out.println("Adding hand 2 to hand \n" + hand);

        hand.addCollection(hand2.getHand());
        System.out.println("adding collection hand2 to hand  \n" + hand);

        System.out.println(hand.removeHand(hand2));
        System.out.println("Hand 2 has been removed from hand \n"+ hand);

        hand2.removeIndexCard(1);
        System.out.println("Card at index 1 (Ten of spades) has been removed from hand 2! \n" + hand2);


        sortDescending(hand);
        System.out.println("Hand has been sorted in descending order \n" + hand);
        sortAscending(hand);
        System.out.println("Hand has been sorted in ascending order \n" + hand);

        System.out.println("all the ranks and how many there are of each: " + hand.getCount());
        System.out.println("how many individual ranks in hand: " + hand.getCount().size());
        System.out.println("Number of cards for rank " + Card.Rank.TEN + " is " + hand.countRank(Card.Rank.TEN));

        System.out.println("Hand value is: " + hand.handValue());
        System.out.println("Is the hand flush?: " + hand.isFlush());
        System.out.println("Is the hand straight?: " + hand.isStraight());


        //Serialising and deserialising hand
        System.out.println("---- HOW TO SERIALISE A HAND------");
        //save to byte code
        String filename = "serialHand.ser";
        Hand serialHand = new Hand(cardArray);
        System.out.println("The card serialHand used for serial test is " + serialHand);

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(serialHand); //open stream and write to object
            out.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        //load object from byte code
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            Hand newHand = (Hand)in.readObject();
            in.close();
            System.out.println("A new hand \n" + newHand + "\n was created and is the same as serialHand");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e){
            e.printStackTrace();
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


}
