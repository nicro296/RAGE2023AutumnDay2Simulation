package player;

import java.util.ArrayList;
import java.util.List;

import a.Match;

public class Player {

	/**
	 * NORMAL 通常のプレイヤー
	 * BYE 不戦勝者の相手プレイヤー用
	 */
	public enum TYPE{
		NORMAL
		,BYE
	}
	/**
	 * プレイヤー識別子
	 */
	public final int playerHash;
	public final TYPE type;
	/**
	 * 試合データ、試合順
	 */
	public final List<Match> matches;
	/**
	 * 初戦の相手がリタイアしたかどうか
	 */
	public boolean firstRetired = false;
	/**
	 * 2回戦の相手がリタイアしたかどうか
	 */
	public boolean secondRetired = false;

	public Player(final int playerHash) {
		this.playerHash = playerHash;
		this.type = TYPE.NORMAL;
		this.matches = new ArrayList<>();
	}
	/**
	 * 不戦勝者の相手プレイヤーデータとして
	 */
	public Player() {
		this.playerHash = -100;
		this.type = TYPE.BYE;
		this.matches = new ArrayList<>();
	}
	/**
	 * @return match勝利数
	 */
	public int getMatchWin() {
		if(!this.matches.stream().anyMatch(match -> match.gameWin == 2)) return 0;
		return (int)this.matches.stream().filter(match -> match.gameWin == 2).count();
	}
	/**
	 * @return match敗北数
	 */
	public int getMatchLose() {
		if(!this.matches.stream().anyMatch(match -> match.gameLose == 2)) return 0;
		return (int)this.matches.stream().filter(match -> match.gameLose == 2).count();
	}
	/**
	 * 試合結果追加
	 * @param match 試合結果
	 */
	public void addMatch(final Match match) {
		this.matches.add(match);
	}
	/**
	 * MatchWinPer 最小値 0.333
	 * @param round
	 * @return
	 */
	public double getMWper(final int round) {
		if(this.type.equals(TYPE.BYE)) return 0.333D;
		return Math.max(0.333D,  (double)this.getMatchWin()/(double)round );
	}
	/**
	 * GameWinPer
	 * @param round
	 * @return
	 */
	public double getGWper(final int round) {
		if(this.type.equals(TYPE.BYE)) return 0D;
		int gw = 0;
		int gl = 0;
		for(Match match: this.matches) {
			gw += match.gameWin;
			gl += match.gameLose;
		}
		//試合をしていない分は0-2換算
		gl += 2 * (round - this.matches.size());
		return (double)gw / (double)(gw + gl);
	}
	/**
	 * 1回目の敗北の回戦数
	 * @return
	 */
	public int firstLoseMatch() {
		if(this.getMatchLose() == 0) return -1;
		for(int i = 0; i<this.matches.size(); i++) {
			if(this.matches.get(i).gameLose == 2) return (i+1);
		}
		throw new UnsupportedOperationException("敗北マッチ特定エラー");
	}
	@Override
	public String toString() {
		return 
				"{" + "\"hash\":" + this.playerHash
				+ "," + "\"mw\":" + this.getMatchWin()
				+ "," + "\"ml\":" + this.getMatchLose()
				+ "," + "\"first-lose\":" + this.firstLoseMatch() + "}";
	}
}
