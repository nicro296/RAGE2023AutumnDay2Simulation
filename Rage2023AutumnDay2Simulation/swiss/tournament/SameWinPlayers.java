package tournament;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import player.Player;
/**
 * Match戦績が同じプレイヤーをまとめたclass
 */
public class SameWinPlayers {

	public final List<Player> players;
	public final int matchWin;
	public final int matchLose;
	/**
	 * 
	 * @param win 勝ち数
	 * @param lose 負け数
	 */
	public SameWinPlayers(final int win, final int lose) {
		this.players = new ArrayList<>();
		this.matchWin = win;
		this.matchLose = lose;
	}
	/**
	 * 0勝0敗の初期データ作成用
	 * @param numPlayers
	 */
	public SameWinPlayers(final int numPlayers) {
		this.matchWin = 0;
		this.matchLose = 0;
		this.players = new ArrayList<>();
		for(int i = 0; i<numPlayers; i++) {
			this.players.add(new Player(i));
		}
	}
	/**
	 * SecureRandomバージョン
	 * Listからランダムなプレイヤーを取得、そしてListから削除
	 * @return 
	 */
	public Player removeRandomPlayer() {
		if(!this.existPlayer()) throw new IllegalArgumentException("プレイヤーがいません。");
		return this.players.remove( new Random().nextInt(this.players.size()) );
	}
	/**
	 * Listからランダムなプレイヤーを取得、そしてListから削除
	 * @return 
	 * @throws NoSuchAlgorithmException 
	 */
	public Player removeRandomPlayer2() throws NoSuchAlgorithmException {
		if(!this.existPlayer()) throw new IllegalArgumentException("プレイヤーがいません。");
		SecureRandom randomNumber = SecureRandom.getInstance("SHA1PRNG");
		
		return this.players.remove( (int)(randomNumber.nextDouble() * (double) this.players.size()) );
	}
	/**
	 * List内にプレイヤーが残っているか
	 * @return 
	 */
	public boolean existPlayer() {
		return this.players.size() > 0;
	}
	/**
	 * List内に対象のhashを持ったプレイヤーが存在するか返す
	 * @param playerHash
	 * @return
	 */
	public boolean existPlayer(final int playerHash) {
		return this.players.stream().anyMatch(player -> player.playerHash == playerHash);
	}
	/**
	 * 引数のPlayerと同戦績のclassかどうか返す
	 * @param player
	 * @return
	 */
	public boolean equalResult(final Player player) {
		if(this.matchWin != player.getMatchWin()) return false;
		if(this.matchLose != player.getMatchLose()) return false;
		return true;
	}
	/**
	 * Player追加
	 * @param player
	 */
	public void addPlayer(final Player player) {
		this.players.add(player);
	}
	/**
	 * 対象hashのPlayer取得
	 * @param playerHash
	 * @return
	 */
	public Player getPlayer(final int playerHash) {
		return this.players.stream().filter(targetPlayer -> targetPlayer.playerHash == playerHash).findFirst().get();
	}
	@Override
	public String toString() {
		return this.players.size() + "," + this.matchWin + "," + this.matchLose;
	}
	
}
