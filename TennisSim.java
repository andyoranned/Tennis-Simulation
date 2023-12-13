
/* Name: Anne Davis
 * Date: April 20th, 2023
 * Assignment: Cosc 4331 
 * 				Simulate a tennis tournament 
 */
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TennisSim {

	public static void main(String[] args) {
	
		Tournament tourney=new Tournament();
		tourney.holdTennisTournament();

	}//end main method
}//end TennisSim class_____________________________________________________________________________]

class Tournament {
	private ArrayList<Player> participants; //all the players in tournament
	
	//constructor...............................................................
	public Tournament() {
		participants=new ArrayList<Player>();
		setBeginParticipants();
	}
	
	//methods...................................................................

	//Build initial roster of players. only for use inside constructor
	private void setBeginParticipants() {
		Scanner kbd = new Scanner(System.in);
		
		//gather the names of all players and add to the roster
		for(int i=0; i<8; i++) {
			System.out.printf("Input player %d's name: ", (i+1));
			String name= kbd.nextLine();
			Player temp= new Player(name);
			participants.add(temp);
		}
	}//end setBeginParticipants()
	
	public void holdTennisTournament() {
		Player[] results= new Player[2];
		
		while(!participants.isEmpty()) {
			ArrayList<Player> winners = new ArrayList<Player>();
			Player[][] teams = dividePlayersRandomly();
			
			//play a new game for each group of players, record the winner of each
			for (int i=0; i<teams.length;i++) {
				System.out.printf("%s vs. %s",teams[i][0].getName(), teams[i][1].getName());
				System.out.printf("\n---------------------\n");
				Game match=new Game(teams[i][0], teams[i][1]);
				Player matchWinner = match.playGame();
				winners.add(matchWinner);
			}
			
			if (participants.size()!=2) {		//not at the end, save the participants that made it to next round
				participants.clear();
				for(Player winner : winners) {
					participants.add(winner);
				}
			}
			else {								//at end. Get winners in correct order 1st/2nd
				results[0] = winners.remove(0);
				for (Player participant : participants) {
					if (!participant.getName().equals(results[0].getName())) {
						results[1]=participant;
					}
				}
				participants.clear();
			}
		}//end while loop
		
		printResults(results);
	}//end holdTennisTounament
	
	private void printResults(Player[] results) {
		System.out.printf("%-13s %s \n%-13s %s\n","First place:", results[0].getName(),"Second Place:", results[1].getName());
	}//end printResults()
	
	public Player[][] dividePlayersRandomly(){
		int groupNum=participants.size() / 2;
		Player[][] groups= new Player[groupNum][2];
		ArrayList<Player> pList = new ArrayList<Player>();
		Random rando = new Random();
		
		//copy participant roster
		for (Player person : participants) {
			pList.add(person);
		}
		
		//assign players to groups randomly
		for (int i=0; i<groupNum; i++) {
			int randomPick = rando.nextInt(pList.size());
			groups[i][0] =pList.remove(randomPick);
			randomPick = rando.nextInt(pList.size());
			groups[i][1]=pList.remove(randomPick);
		}
		return groups;
	}//end dividePlayersRandomly()
}//end tournament___________________________________________________________________________________]

class Player {
	private String name;
	
	//constructor...............................................
	Player(String name){
		this.name=name;
	}
	
	//method----------------------------------------------------
	public String getName() {
		return name;
	}
}//end Player class_________________________________________________________________________________]

class Score {
	private int p1Point;
	private int p2Point;
	protected Game round;
	
	//constructor.......................................................................
	public Score(Game round) {
		this.p1Point=0;
		this.p2Point=0;
		this.round=round;
	}
	
	//methods............................................................................
	//getters for fields
	public int getP1Point() {
		return p1Point;
		}
	
	public int getP2Point() {
		return p2Point;
		}
	
	//setters for fields
	public void setP1Point(int p1Point) {
		this.p1Point=p1Point;
	}
	
	public void setP2Point(int p2Point) {
		this.p2Point=p2Point;
	}
}//end Score class________________________________________________________________________________]

/* Keeps track of the score of one game. If we had to keep track of match scores as well, there
 * would be a MatchScore class that extends Score. 
 */
class GameScore extends Score {
	
	//constructor........................................................................
	public GameScore(Game round) {
		super(round);
	}
	
	//methods............................................................................	
	//if the original server scores a point, calculate new score based on tennis scoring rules
	public void p1ScoresPoint() {
		Player serverP1 = round.getServer(); 	//original server
		String p1Name = serverP1.getName();
		Player p2P = round.getReceiver();
		String p2Name = p2P.getName();
		String str;
		Scanner kbd=new Scanner(System.in);
		
		int p1= getP1Point();
		int p2= getP2Point();
		
		if (p1 <=15 ){
			p1+=15;
			setP1Point(p1);
			System.out.printf("%s scores.\n%s's score: %d \n%s's score: %d\n%s serves next\n",p1Name,p1Name, p1, p2Name, p2, p1Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}
		else if (p1==30 && p2!=40) {
			p1+=10;
			setP1Point(p1);
			System.out.printf("%s scores.\n%s's score: %d \n%s's score: %d\n%s serves next\n",p1Name,p1Name, p1, p2Name, p2, p1Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}	
		else if (p1==30 && p2 ==40) {
			p1+=10;
			setP1Point(p1);
			System.out.printf("%s scores: DEUCE\n%s serves next\n",p1Name, p1Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}
		else if (p1 >=40 && p1==p2) {
			p1+=10;
			setP1Point(p1);
			System.out.printf("%s scores: ADVANTAGE\n%s serves next\n",p1Name,p1Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}
		else if (p1 >= 40 && p1>p2) {
			System.out.printf("%s scores: GAME\n%s wins the game\n",p1Name, p1Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			round.setIsOver(true);
			return;
		}
		else if (p1>=40 && p1<p2) {
			p1+=10;
			setP1Point(p1);
			System.out.printf("%s scores: DEUCE\n%s serves next\n",p1Name, p1Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}	
	}//end p1ScoresPoint() method

	//if the original receiver scores a point, calculate new score based on tennis scoring rules
	public void p2ScoresPoint() {
		Player receiverP2 = round.getReceiver();
		String p2Name = receiverP2.getName();
		Player p1P = round.getServer();
		String p1Name = p1P.getName();
		String str;
		Scanner kbd=new Scanner(System.in);
		
		int p1= getP1Point();
		int p2= getP2Point();
		
		if (p2 <=15 ){
			p2+=15;
			setP2Point(p2);
			System.out.printf("%s scores.\n%s's score: %d \n%s's score: %d\n%s serves next\n",p2Name,p1Name, p1, p2Name,p2, p2Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}
		else if (p2==30 && p1!=40) {
			p2+=10;
			setP2Point(p2);
			System.out.printf("%s scores.\n%s's score: %d \n%s's score: %d\n%s serves next\n",p2Name,p1Name, p1, p2Name,p2, p2Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}
		else if (p2==30 && p1 ==40) {
			p2+=10;
			setP2Point(p2);
			System.out.printf("%s scores: DEUCE\n%s serves next\n",p2Name,p2Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}
		else if (p2 >=40 && p1==p2) {
			p2+=10;
			setP2Point(p2);
			System.out.printf("%s scores: ADVANTAGE\n%s serves next\n",p2Name, p2Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}
		else if (p2 >= 40 && p2>p1) {
			System.out.printf("%s scores: GAME\n%s wins the game\n",p2Name, p2Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			round.setIsOver(true);
			return;
		}
		else if (p2>=40 && p2<p1) {
			p2+=10;
			setP2Point(p2);
			System.out.printf("%s scores: DEUCE\n%s serves next\n",p2Name, p2Name);
			System.out.println("Press [ENTER] to continue...");
			str=kbd.nextLine();
			return;
		}	
	}//end p2ScoresPoint() method
}//end GameScore class____________________________________________________________________________]

class Game {
	private Player serverP1;
	private Player receiverP2;
	static Random rando;
	private Boolean isOver;
	private Score theScore;
	
	//constructor.......................................................................
	public Game (Player x, Player y) {
		rando = new Random();
		coinFlip(x, y);	//set the starting server & receiver
		this.isOver=false;
		this.theScore=new GameScore(this); //POLYMORPHISM --this is unnecessary, just illustrating principles of OOP 
	}
	
	//methods............................................................................
	//setters for fields
	public void setIsOver(boolean isOver) {
		this.isOver=isOver;
	}
	//getters for fields
	public Player getServer() {
		return this.serverP1;
	}
	public Player getReceiver() {
		return this.receiverP2;
	}

	//generate a random number 0-10 used to quantify how good of a serve 
	public int serve() {
		
		int serveQuality = rando.nextInt(11);
		
		return serveQuality;
	}
	
	//blocking a serve is successful if the random number, 0-10, is more than the serve quality
	public boolean isBlocked (int serveQuality) {
		
		int blockQuality = rando.nextInt(11);
		
		if (blockQuality>serveQuality)
			return true;
		else
			return false;
	}
	
	//Loop through serving and blocking until a block is unsuccessful. Continue until scoring point is achieved
	public Player playGame() {
		int serve= 0;
		
		int playCount=0;
		Player winner=null;
		while(!isOver) {				//continue the back and forth until someone wins
			serve=serve();				//here serve means hit it over the net. could just be a return serve or starting serve
			if (isBlocked(serve)) { 	//blocked point
				playCount++;
			}
			else if (playCount%2==0) {	//original server scores point
				((GameScore) theScore).p1ScoresPoint();
				if(isOver)
					winner=serverP1;
				playCount=0;			//original server, serves again next round
			}
			else {						//receiver scores point
				((GameScore) theScore).p2ScoresPoint();
				if(isOver)
					winner=receiverP2;
				playCount=1;			//original receiver starting serve next round	
			}//end if/else
		}//end while loop
		return winner;
	}//end playGame()
	
	//simulates a coin toss. 
	public void coinFlip(Player x, Player y) {
		int flip=rando.nextInt(1);
		int playXCallsIt=rando.nextInt(1);//First player calls head/tails(0/1)
		
		if (flip==playXCallsIt){ //if right, they're the server
			this.serverP1=x;
			this.receiverP2=y;
			System.out.printf("%s serves first\n",x.getName());
		}
		else
		{
			this.serverP1=y;
			this.receiverP2=x;
			System.out.printf("%s serves first\n",y.getName());
		}
	}//end coinFlip()
}//end game class__________________________________________________________________________________]
