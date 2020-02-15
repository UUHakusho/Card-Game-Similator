package question2;

//import question1.Card;
//import question1.Hand;

import java.util.Iterator;
import java.util.Random;

public class BasicStrategy implements Strategy {

    //player decides whether to cheat or not
    @Override
    public boolean cheat(Bid b, Hand h) {
        boolean willCheat = false; //local var defaulted at false as you never cheat unless you have to
        Card.Rank priorRank = b.getRank();

        Card.Rank nextRank  = priorRank.getNext();

        //if players current hand DOES NOT contain the consecutive rank in the enum AND the rank of the prior play, then cheat.
        if (!h.getCount().containsKey(nextRank) && (!h.getCount().containsKey(priorRank))){
            willCheat = true;
        }

        return willCheat;
    }
    //returns your bid, your play, based on whether or not you are cheating
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        Random rand = new Random();
        Card.Rank bidRank = b.getRank(); //initialising it as same rank as the previous bid's rank
        Hand bidHand = new Hand(); //initialised as an empty hand
        System.out.println(h);
        int amountOfBidRank = h.countRank(bidRank); //count the amount of ranks in hand that are the same as bidrank
        int amountOfNext = h.countRank(bidRank.getNext());
        Iterator<Card> it = h.iterator();

            if (amountOfBidRank < amountOfNext){
                while (it.hasNext()){
                    //if card rank in hand is same as bidrank + 1 and there are more of these cards than if just rank = bidrank, then play
                    bidRank = bidRank.getNext();
                    Card card = it.next();
                    if (card.getRank() == bidRank) {
                        bidHand.addCard(card);
                        it.remove(); //remove card
                        System.out.println("card added is--- " + card);
                    }

            }}
            else  {
                if (amountOfBidRank == 0){
                    cheat = true;
                }
                else {
                    //as amountOfBidRank is more than you play the max amount of cards of lowest rank
                    while(it.hasNext()) {
                        Card card = it.next();
                        if (card.getRank() == bidRank) {
                            bidHand.addCard(card);
                            it.remove();
                            h.removeCard(card);
                            System.out.println("card added is--- " + card);
                        }
                    }
                }
            }


        //if you decide to cheat, play a single card randomly
        if (cheat){
            int index = rand.nextInt(h.getHand().size());
            Card randomCard = h.getHand().get(index);
            bidHand.getHand().add(randomCard); //plays a randomly generated card in a new hand
            h.removeCard(randomCard);
            bidRank = bidRank.getNext(); //if cheating say next card
            System.out.println("(This is a CHEAT) " + randomCard);
            return new Bid(bidHand, bidRank);
        }
        return new Bid(bidHand, bidRank);

    }

    //calls cheat on a current bid only if certain it is a cheat - based on players hand h
    @Override
    public boolean callCheat(Hand h, Bid b) {
        int total = 0;

        for (Card c : h){
            if (c.getRank().equals(b.getRank())){
                total++;
            }

        }
        return total > 4; //if you have all 4 of the possible ranks, then that player is cheating, return whether or not total is true or false
    }
}
