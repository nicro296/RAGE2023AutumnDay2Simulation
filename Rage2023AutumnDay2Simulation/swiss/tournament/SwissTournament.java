package tournament;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import a.Match;
import a.PlayerPoint;
import a.PlayerPoints;
import player.Player;
import player.Player.TYPE;

public class SwissTournament {

	final int maxRound;
	public final List<RoundPlayers> rounds;

	public static void main(String args[]) throws FileNotFoundException, NoSuchAlgorithmException {

		final int round = 6;
		final SwissTournament tour = new SwissTournament(600, round);
		tour.nextRound(0D);
		tour.nextRound(0.4D);
		tour.nextRound(0.2D);
		tour.nextRound(0D);
		tour.nextRound(0D);
		tour.nextRound(0D);
		final List<PlayerPoint> points = tour.makePlayerPoint();
		PlayerPoints pps = new PlayerPoints(points, round);
		System.out.println(tour.view(pps.n5_1[0]));
		System.out.println(tour.view(pps.n5_1top32[0]));
		System.out.println(tour.view(pps.n5_1[1]));
		System.out.println(tour.view(pps.n5_1top32[1]));
		System.out.println(tour.view(pps.n5_1[2]));
		System.out.println(tour.view(pps.n5_1top32[2]));
		System.out.println(tour.view(pps.n5_1[3]));
		System.out.println(tour.view(pps.n5_1top32[3]));
	}
	private String view(final int[] ni) {
		String output = "[";
		for(int n:ni) {
			output += n + ",";
		}
		output += "]";
		return output;
	}
	
	/**
	 * スイス開始前データ作成
	 * @param numPlayers 参加プレイヤー数
	 * @throws Exception
	 */
	public SwissTournament(final int numPlayers, final int maxRound) {
		this.maxRound = maxRound;
		this.rounds = new ArrayList<>();
		this.rounds.add(new RoundPlayers(0, numPlayers));
	}
	public PlayerPoints simulation1() throws FileNotFoundException, NoSuchAlgorithmException {
		for(int i=0;i<6;i++) {
			this.nextRound(0D);
		}
		final List<PlayerPoint> points = this.makePlayerPoint();
		PlayerPoints pps = new PlayerPoints(points, this.maxRound);
		return pps;
	}
	public PlayerPoints simulation2() throws FileNotFoundException, NoSuchAlgorithmException {
		this.nextRound(0D);
		this.nextRound(0D);
		this.nextRound(0D);
		this.nextRound(0D);
		this.nextRound(0D);
		this.nextRound(0D);
		final List<PlayerPoint> points = this.makePlayerPoint();
		PlayerPoints pps = new PlayerPoints(points, this.maxRound);
		return pps;
	}
	public PlayerPoints simulation3() throws FileNotFoundException, NoSuchAlgorithmException {
		this.nextRound(0D);
		this.nextRound(0.4D);
		this.nextRound(0.2D);
		this.nextRound(0D);
		this.nextRound(0D);
		this.nextRound(0D);
		final List<PlayerPoint> points = this.makePlayerPoint();
		PlayerPoints pps = new PlayerPoints(points, this.maxRound);
		return pps;
	}
	/**
	 * 
	 * @param retiredPer 1敗プレイヤーのリタイア比率
	 * @throws NoSuchAlgorithmException 
	 */
	public void nextRound(final double retiredPer) throws NoSuchAlgorithmException {
		final int maxLose = 1;//2敗したら強制ドロップ
		final int roundNum = this.rounds.size();
		this.rounds.add(new RoundPlayers(roundNum));
		final RoundPlayers currentRound = this.rounds.get(roundNum-1);
		final int maxPlayers = currentRound.groupPlayers.size()>1 ? currentRound.groupPlayers.get(1).players.size(): 0;
		final int borderPlayers = maxPlayers!=0 ? (int)((double)maxPlayers * retiredPer): -1;//リタイアしないプレイヤーの数
		while(currentRound.existPlayer(0, maxLose) 
				&& ( currentRound.groupPlayers.size()==1 || borderPlayers < currentRound.groupPlayers.get(1).players.size() ) ) {
			final Player player1 = currentRound.getRandomPlayer(maxLose);
			final Player player2 = this.getOpponentPlayer(maxLose, currentRound);
			final Match match = this.makeMatch(player2);

			player1.addMatch(match);
			this.addPlayer(player1);
			if(player2.type.equals(TYPE.BYE)) continue;
			//不戦勝じゃないなら
			player2.addMatch(match.reverseMatch(player1.playerHash));
			this.addPlayer(player2);
		}
		if(roundNum == 2) {
			currentRound.groupPlayers.get(1).players.stream().forEach(player -> player.firstRetired = true);
		}else if(roundNum == 3) {
			currentRound.groupPlayers.get(1).players.stream().forEach(player -> player.secondRetired = true);
		}
	}
	private Player getOpponentPlayer(final int maxLose, final RoundPlayers currentRound) throws NoSuchAlgorithmException {
		if(currentRound.existPlayer(0, maxLose)) {
			return currentRound.getRandomPlayer(maxLose);
		}
		//不戦勝
		return new Player();
	}
	private Match makeMatch(final Player opponentPlayer) {
		if(opponentPlayer.type.equals(Player.TYPE.BYE)) {//不戦勝対面
			return new Match(opponentPlayer.playerHash, 2, 0);
		}
		return Match.random(opponentPlayer.playerHash);
	}
	/**
	 * プレイヤー追加
	 * @param player
	 */
	private void addPlayer(final Player player) {
		this.rounds.get(this.rounds.size()-1).addPlayer(player);
	}
	public List<PlayerPoint> makePlayerPoint() {
		final List<PlayerPoint> pps = new ArrayList<>();
		for(RoundPlayers roundPlayers: this.rounds) {
//			System.out.println("roundNum["+roundPlayers.round+"]");
			for(SameWinPlayers players: roundPlayers.groupPlayers) {
				for(Player player: players.players) {
					final double mwp = player.getMWper(this.maxRound);
					final double gwp = player.getGWper(this.maxRound);
					double omwp = 0D;
					double ogwp = 0D;
					for(Match match: player.matches) {
						omwp += this.getPlayer(match.opponentHash).getMWper(this.maxRound);
						ogwp += this.getPlayer(match.opponentHash).getGWper(this.maxRound);
					}
					final boolean retired = this.getPlayer(player.matches.get(0).opponentHash).firstRetired;
					final boolean retired2;
					if(player.matches.size() > 1) {
						retired2 = this.getPlayer(player.matches.get(1).opponentHash).secondRetired;
					}else {
						retired2 = false;
					}
					omwp = omwp / (double)this.maxRound;
					ogwp = ogwp / (double)this.maxRound;
					pps.add(new PlayerPoint(player, mwp, gwp, omwp, ogwp, retired, retired2));
					
				}
			}
		}
		return pps;
	}
	public Player getPlayer(final int playerHash) {
		if(playerHash == -100) return new Player();
		if(!this.rounds.stream().anyMatch(players -> players.existPlayer(playerHash))) {
			throw new UnsupportedOperationException("プレイヤーが見つかりません[" + playerHash + "]");
		}
		return this.rounds.stream().filter(players -> players.existPlayer(playerHash)).findFirst().get().getPlayer(playerHash);
	}
}
