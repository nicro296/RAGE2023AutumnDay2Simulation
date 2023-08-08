package a;

import player.Player;

public class PlayerPoint implements Comparable<PlayerPoint>{
	
	public final Player player;
	final double matchWinPer;
	final double gameWinper;
	final double oppoMWper;
	final double oppoGWper;
	/**
	 * 1回戦の相手がリタイアしたかどうか
	 */
	public boolean firstOpponentRetired = false;
	/**
	 * 2回戦の相手がリタイアしたかどうか
	 */
	public boolean SecondOpponentRetired = false;
	
	public PlayerPoint(final Player player, final double mwp, final double gwp
			, final double omwp, final double ogwp) {
		this.player = player;
		this.matchWinPer = mwp;
		this.gameWinper = gwp;
		this.oppoMWper = omwp;
		this.oppoGWper = ogwp;
	}
	public PlayerPoint(final Player player, final double mwp, final double gwp
			, final double omwp, final double ogwp, final boolean retired) {
		this.player = player;
		this.matchWinPer = mwp;
		this.gameWinper = gwp;
		this.oppoMWper = omwp;
		this.oppoGWper = ogwp;
		this.firstOpponentRetired = retired;
	}
	public PlayerPoint(final Player player, final double mwp, final double gwp
			, final double omwp, final double ogwp, final boolean retired, final boolean retired2) {
		this.player = player;
		this.matchWinPer = mwp;
		this.gameWinper = gwp;
		this.oppoMWper = omwp;
		this.oppoGWper = ogwp;
		this.firstOpponentRetired = retired;
		this.SecondOpponentRetired = retired2;
	}
	/**
	 * 成績が良い順
	 */
	@Override
	public int compareTo(PlayerPoint o) {
		if(this.player.getMatchWin() > o.player.getMatchWin()) return -1;
		if(this.player.getMatchWin() < o.player.getMatchWin()) return 1;
		if(this.matchWinPer > o.matchWinPer) return -1;
		if(this.matchWinPer < o.matchWinPer) return 1;
		if(this.oppoMWper > o.oppoMWper) return -1;
		if(this.oppoMWper < o.oppoMWper) return 1;
		if(this.gameWinper > o.gameWinper) return -1;
		if(this.gameWinper < o.gameWinper) return 1;
		if(this.oppoGWper > o.oppoGWper) return -1;
		if(this.oppoGWper < o.oppoGWper) return 1;
		return 0;
	}
	@Override
	public String toString() {
		return "{" + "\"player\":" + this.player
				+ "," + "\"mwp\":" + String.format("%.3f", this.matchWinPer)
				+ "," + "\"gwp\":" + String.format("%.3f", this.gameWinper)
				+ "," + "\"ogmp\":" + String.format("%.3f", this.oppoMWper)
				+ "," + "\"ogwp\":" + String.format("%.3f", this.oppoGWper) + "}";
	}
}
