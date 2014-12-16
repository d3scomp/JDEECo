package cz.cuni.mff.d3s.deeco.security;

import java.security.Key;
import java.util.List;

public interface SecurityKeyManager {

	Key getPublicKeyFor(String roleName, List<Object> arguments);

}
