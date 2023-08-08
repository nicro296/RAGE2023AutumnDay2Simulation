package main;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;

import a.PlayerPoints;
import tournament.SwissTournament;

public class Loop {

	final int pattern = 4;
	final int round = 6;
	
	int[][] n5_1 = new int[pattern][round];
	int[][] n5_1top32 = new int[pattern][round];
	
	public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException {
		final int loop = 500;
		final int players = 600;
		new Loop(loop, players);
	}
	
	public Loop(final int loop, final int playerNumber) throws FileNotFoundException, NoSuchAlgorithmException {
		for(int i=0; i<loop; i++) {
			if(i%100 == 0) System.out.println("loop[" + i + "]");
			final SwissTournament tour = new SwissTournament(playerNumber, this.round);
//			final PlayerPoints pps = tour.simulation1();
			final PlayerPoints pps = tour.simulation2();
			this.addCount(pps);
		}
		this.view();
	}
	
	private void addCount(final PlayerPoints pps) {
		for(int i=0;i<this.pattern; i++) {
			for(int j=0;j<this.round;j++) {
				this.n5_1[i][j] += pps.n5_1[i][j];
				this.n5_1top32[i][j] += pps.n5_1top32[i][j];
			}
		}
	}
	private void view() {
		System.out.println("集計結果");
		for(int i=0;i<pattern;i++) {
			System.out.print("i["+i+"]5-1母数"+"	");
			for(int j=0;j<round;j++) {
				System.out.print(this.n5_1[i][j]+"	");
			}
			System.out.println();
			System.out.print("i["+i+"]top32"+"	");
			for(int j=0;j<round;j++) {
				System.out.print(this.n5_1top32[i][j]+"	");
			}
			System.out.println();
		}
	}
}
