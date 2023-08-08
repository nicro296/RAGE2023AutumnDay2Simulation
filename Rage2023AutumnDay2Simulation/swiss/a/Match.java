package a;

import java.util.Random;

public class Match {

	public final int opponentHash;
	public final int gameWin;
	public final int gameLose;
	/**
	 * 
	 * @param opponentHash
	 * @param win
	 * @param lose
	 */
	public Match(final int opponentHash, final int win, final int lose) {
		this.opponentHash = opponentHash;
		this.gameWin = win;
		this.gameLose = lose;
	}
	/**
	 * 対戦相手のMatchデータ作成
	 * @param playerHash
	 * @return
	 */
	public Match reverseMatch(final int playerHash) {
		return new Match(playerHash, this.gameLose, this.gameWin);
	}
	/**
	 * ランダムに勝敗を決定する用
	 * @param playerHash
	 * @return
	 */
	public static Match random(final int playerHash) {
		final int random = new Random().nextInt(4);
		if(random == 0) return new Match(playerHash, 2, 0);
		if(random == 1) return new Match(playerHash, 2, 1);
		if(random == 2) return new Match(playerHash, 1, 2);
		if(random == 3) return new Match(playerHash, 0, 2);
		throw new UnsupportedOperationException("random["+random+"]が不整合です。");
	}
	@Override
	public String toString() {
		return this.opponentHash+","+this.gameWin+","+this.gameLose;
	}
}
