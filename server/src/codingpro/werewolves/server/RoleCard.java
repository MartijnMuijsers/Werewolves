package codingpro.werewolves.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class RoleCard {
	
	@Getter
	protected final Role role;
	@Getter
	protected final int imageID;
	
	public String getImageFilename() {
		return role.getIdentifier()+imageID+".png";
	}
	
	public RoleCard(Role role) {
		this(role, (int) (1+Math.random()*role.getNumberOfCardImages()));
	}
	
}
