package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import java.util.Date;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Role;

/**
 * the ID field is marked as secured
 * @author Ondřej Štumpf
 */

@Component
@Role(role = "role1")
@Role(role = "role2", params = {"name", "time"})
public class WrongC4WithSecurity {

	public String name;
	
	@Allow(role = "role1")
	@Allow(role = "role3")
	public Integer capacity;
	
	@Allow(role = "role2", params = {"name", "time"})
	public Date time;
	
	@Allow(role = "x")
	public String id;
}
