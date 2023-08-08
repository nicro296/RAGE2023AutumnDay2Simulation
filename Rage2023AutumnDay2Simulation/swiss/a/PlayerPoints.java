package a;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class PlayerPoints {
	final List<PlayerPoint> points;
	/**
	 * 5-1プレイヤー数
	 * <br>
	 * n5_1[i][j] (0<=i<=3, 0<=j<=ラウンド数)
	 * <br>
	 * n5_1[0][x] = (x+1)回戦目に敗北
	 * <br>
	 * n5_1[1][x] = (x+1)回戦目に敗北、1回戦の相手がリタイア
	 * <br>
	 * n5_1[2][x] = (x+1)回戦目に敗北、2回戦の相手がリタイア
	 * <br>
	 * n5_1[3][x] = (x+1)回戦目に敗北、1,2回戦の相手が共にリタイア
	 */
	public int[][] n5_1;
	/**
	 * top32以内の5-1プレイヤー数
	 * <br>
	 * n5_1top32[i][j] (0<=i<=3, 0<=j<=ラウンド数)
	 * <br>
	 * 
	 * n5_1top32[0][x] = (x+1)回戦目に敗北
	 * <br>
	 * n5_1top32[1][x] = (x+1)回戦目に敗北、1回戦の相手がリタイア
	 * <br>
	 * n5_1top32[2][x] = (x+1)回戦目に敗北、2回戦の相手がリタイア
	 * <br>
	 * n5_1top32[3][x] = (x+1)回戦目に敗北、1,2回戦の相手が共にリタイア
	 */
	public int[][] n5_1top32;
	
	public PlayerPoints(final List<PlayerPoint> points, final int maxRound) throws FileNotFoundException {
		final int pattern = 4;
		this.n5_1 = new int[pattern][maxRound];
		this.n5_1top32 = new int[pattern][maxRound];
		this.points = points;
		Collections.sort(this.points);
		this.count(maxRound);
	}
	private void write(final String file) throws FileNotFoundException {
		final PrintWriter pw = new PrintWriter(
				new OutputStreamWriter(
						new FileOutputStream(
								new File(file), false), StandardCharsets.UTF_8));
		pw.println("{\"points\":"+this.points.toString()+"}");
		pw.flush();
		pw.close();
	}
	private void count(final int maxRound) throws FileNotFoundException {
		this.removeUnder4win();
		for(int i=0; i<maxRound;i++) {
			int round = i+1;
			n5_1[0][i] = this.count5_1(round);
			n5_1[1][i] = this.count5_1Retired1(round);
			n5_1[2][i] = this.count5_1Retired2(round);
			n5_1[3][i] = this.count5_1Retired12(round);
		}
		this.removeOutOfTop32();
		for(int i=0; i<maxRound;i++) {
			int round = i+1;
			n5_1top32[0][i] = this.count5_1(round);
			n5_1top32[1][i] = this.count5_1Retired1(round);
			n5_1top32[2][i] = this.count5_1Retired2(round);
			n5_1top32[3][i] = this.count5_1Retired12(round);
		}
	}
	private void removeUnder4win() {
		while(this.points.get(this.points.size()-1).player.getMatchWin() < 5) {
			this.points.remove(this.points.size()-1);
		}
	}
	private void removeOutOfTop32() {
		while(this.points.size() > 32) {
			this.points.remove(this.points.size()-1);
		}
	}
	private int count5_1(final int firstLose) {
		return (int)this.points.stream().filter(point -> point.player.getMatchWin()==5)
		.filter(point -> point.player.firstLoseMatch() == firstLose)
		.count();
	}
	/**
	 * 敗北タイミングがfirstLose回戦の5-1のプレイヤーかつ
	 * <br>
	 * 1回戦の相手がリタイアしている人数
	 * @param firstLose
	 * @return
	 */
	private int count5_1Retired1(final int firstLose) {
		return (int)this.points.stream().filter(point -> point.player.getMatchWin()==5)
		.filter(point -> point.player.firstLoseMatch() == firstLose)
		.filter(point -> point.firstOpponentRetired)
		.count();
	}
	/**
	 * 敗北タイミングがfirstLose回戦の5-1のプレイヤーかつ
	 * <br>
	 * 2回戦の相手がリタイアしている人数
	 * @param firstLose
	 * @return
	 */
	private int count5_1Retired2(final int firstLose) {
		return (int)this.points.stream().filter(point -> point.player.getMatchWin()==5)
		.filter(point -> point.player.firstLoseMatch() == firstLose)
		.filter(point -> point.SecondOpponentRetired)
		.count();
	}
	/**
	 * 敗北タイミングがfirstLose回戦の5-1のプレイヤーかつ
	 * <br>
	 * 1回戦と2回戦の相手がリタイアしている人数
	 * @param firstLose
	 * @return
	 */
	private int count5_1Retired12(final int firstLose) {
		return (int)this.points.stream().filter(point -> point.player.getMatchWin()==5)
		.filter(point -> point.player.firstLoseMatch() == firstLose)
		.filter(point -> point.firstOpponentRetired)
		.filter(point -> point.SecondOpponentRetired)
		.count();
	}
	private int count6_0() {
		return (int)this.points.stream().filter(point -> point.player.getMatchWin() == 6).count();
	}
	
}
