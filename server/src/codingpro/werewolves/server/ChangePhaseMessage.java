package codingpro.werewolves.server;

import java.util.List;

import lombok.Getter;

/**
 * ChangePhaseMessage (server -> client)
 * - phase enum name
 */
public class ChangePhaseMessage extends ServerToClientMessage {
	
	@Getter
	protected final PlayerPhase phase;
	
	public ChangePhaseMessage(PlayerPhase phase) {
		super(ServerToClientMessageType.CHANGE_PHASE);
		this.phase = phase;
	}
	
	@Override
	public List<Object> getData() {
		List<Object> data = super.getData();
		data.add(phase.name());
		return data;
	}
	
}
