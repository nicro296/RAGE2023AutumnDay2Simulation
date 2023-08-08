package tournament;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import a.Match;
import a.PlayerPoint;
import player.Player;


/**
 * 同一回戦のPlayerをまとめたclass
 *
 */
public class RoundPlayers {
	/**
	 * 中身のSameWinPlayersは勝ち数が高い順
	 */
	public final List<SameWinPlayers> groupPlayers;
	final int round;
	
	public RoundPlayers(final int round) {
		this.groupPlayers = new ArrayList<>();
		this.round = round;
		for(int i = round; i >= 0; i--) {
			this.groupPlayers.add(new SameWinPlayers(i, round-i));
		}
	}
	/**
	 * 試合前の初期データ作成用
	 * @param round
	 * @param numPlayers
	 */
	public RoundPlayers(final int round, final int numPlayers) {
		if(round != 0) throw new UnsupportedOperationException("round["+round+"]が不整合です");
		this.round = round;
		this.groupPlayers = new ArrayList<>();
		this.groupPlayers.add(new SameWinPlayers(numPlayers));
	}
	/**
	 * match勝敗をもとにgroupPlayers内の対象のSameWinPlayersに追加
	 * @param player
	 */
	public void addPlayer(final Player player) {
		this.groupPlayers.stream().filter(players -> players.equalResult(player)).findFirst().get().addPlayer(player);
	}
	public boolean existPlayer() {
		return this.groupPlayers.stream().anyMatch(players -> players.existPlayer());
	}
	public boolean existPlayer(final int playerHash) {
		return this.groupPlayers.stream().anyMatch(players -> players.existPlayer(playerHash) );
	}
	/**
	 * maxLose以下の敗数のプレイヤーがまだlist内にいるならtrue
	 * @param nonUse
	 * @param maxLose
	 * @return
	 */
	public boolean existPlayer(final int nonUse, final int maxLose) {
		return this.groupPlayers.stream().anyMatch(players -> (players.matchLose <= maxLose && players.existPlayer()) );
	}
	/**
	 * 勝ち数が最も高いSameWinPlayersからプレイヤーをランダムに取得 & listから削除
	 * @return
	 * @throws Exception
	 */
	public Player getRandomPlayer() throws Exception {
		if(!this.existPlayer()) throw new IllegalArgumentException("プレイヤーがいません。");
		for(SameWinPlayers players: this.groupPlayers) {
			if(players.existPlayer()) return players.removeRandomPlayer();
		}
		throw new UnsupportedOperationException("プレイヤー抽出に不整合");
	}
	/**
	 * 勝ち数が最も高いSameWinPlayersからプレイヤーをランダムに取得 & listから削除、maxLoseより負け数が多いプレイヤーは無視
	 * @param maxLose
	 * @return
	 * @throws Exception
	 */
	public Player getRandomPlayer(final int maxLose) {
		if(!this.existPlayer(0, maxLose)) throw new IllegalArgumentException("プレイヤーがいません。");
		for(SameWinPlayers players: this.groupPlayers) {
			if(players.matchLose <= maxLose && players.existPlayer()) return players.removeRandomPlayer();
		}
		throw new UnsupportedOperationException("プレイヤー抽出に不整合");
	}
	/**
	 * SecureRandomバージョン,使っていないけどこっちでも可
	 * 勝ち数が最も高いSameWinPlayersからプレイヤーをランダムに取得 & listから削除、maxLoseより負け数が多いプレイヤーは無視
	 * @param maxLose
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public Player getRandomPlayer2(final int maxLose) throws NoSuchAlgorithmException {
		if(!this.existPlayer(0, maxLose)) throw new IllegalArgumentException("プレイヤーがいません。");
		for(SameWinPlayers players: this.groupPlayers) {
			if(players.matchLose <= maxLose && players.existPlayer()) return players.removeRandomPlayer2();
		}
		throw new UnsupportedOperationException("プレイヤー抽出に不整合");
	}
	/**
	 * 対象hashのPlayer取得
	 * @param playerHash
	 * @return
	 */
	public Player getPlayer(final int playerHash) {
		return this.groupPlayers.stream().filter(players -> players.existPlayer(playerHash)).findFirst().get().getPlayer(playerHash);
	}
	public double matchWinPer(final int playerHash, final int round) {
		return this.getPlayer(playerHash).getMWper(round);
	}
	public double gameWinPer(final int playerHash, final int round) {
		return this.getPlayer(playerHash).getGWper(round);
	}
	public List<PlayerPoint> makePlayerPoint(final int round) {
		final List<PlayerPoint> pps = new ArrayList<>();
		for(SameWinPlayers players: this.groupPlayers) {
			for(Player player: players.players) {
				final double mwp = player.getMWper(round);
				final double gwp = player.getGWper(round);
				double omwp = 0D;
				double ogwp = 0D;
				for(Match match: player.matches) {
					omwp += this.matchWinPer(match.opponentHash, round);
					ogwp += this.gameWinPer(match.opponentHash, round);
				}
				omwp = omwp / (double)round;
				ogwp = ogwp / (double)round;
				pps.add(new PlayerPoint(player, mwp, gwp, omwp, ogwp));
			}
		}
		return pps;
	}
	
	
	
}
