/**
 * Provides the interface for all scheduler classes along with few particular implementations
 * <p>
 * This package contains the base class of all schedulers(see the {@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler Scheduler} class)
 * as well as some implementations listed below
 * <ul>
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.LocalTimeScheduler LocalTimeScheduler}</li>
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.LocalTimeScheduler SingleThreadedScheduler}</li>
 * </ul>
 * Obviously these are not the only options and one is free to implement any other type of 
 * scheduler according to specific needs of the system(a MultiThreadedScheduler for example).
 * 
 * @author 	Andranik Muradyan 	<muradian@d3s.mff.cuni.cz>
 * @author 	Jaroslav Keznikl 	<keznikl@d3s.mff.cuni.cz>
 * 
 */

package cz.cuni.mff.d3s.deeco.scheduler;
