package question2;

//import question1.Hand;
//import question1.Card;
//import question1.Deck;
//import question1.deckNoNext;

public class BasicPlayer implements Player{
    //references
    public Hand     playerHand;
    public Strategy strategy;
    public CardGame cardGame;

    //constructor for BasicCheat
    public BasicPlayer(BasicStrategy s, BasicCheat c){
        this.playerHand = new Hand();
        this.strategy   = s;
        this.cardGame   = c;
    }

    //add card to players hand
    @Override
    public void addCard(Card c) {
        playerHand.addCard(c);
    }

    @Override
    public void addHand(Hand h) {
        playerHand.addHand(h);
    }

    //return number of cards left in players hand
    @Override
    public int cardsLeft() {
        return playerHand.getHand().size();
    }

    @Override
    public void setGame(CardGame g) {
        this.cardGame = g;
    }

    @Override
    public void setStrategy(Strategy s) {
        this.strategy = s;
    }
    /*Constructs a bid when asked to by the game.
 * @param b: the last bid accepted by the game. .
            * @return the players bid*/
    @Override
    public Bid playHand(Bid b) {
        boolean cheatOrNot = strategy.cheat(b, playerHand); //checks to see if the player will cheat or not by using the cheat strategy and taking in previous bid.
        return strategy.chooseBid(b, playerHand, cheatOrNot);
    }

    //if call cheat method in BasicStrategy is true, then a cheat will be called!
    @Override
    public boolean callCheat(Bid b) {
        return strategy.callCheat(playerHand, b);
    }

}
