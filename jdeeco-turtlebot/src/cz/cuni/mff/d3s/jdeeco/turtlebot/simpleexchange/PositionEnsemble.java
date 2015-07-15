package cz.cuni.mff.d3s.jdeeco.turtlebot.simpleexchange;

import geometry_msgs.Point;
import geometry_msgs.PoseWithCovariance;
import sensor_msgs.NavSatFix;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@PeriodicScheduling(period = 500, offset = 75)
public class PositionEnsemble {

	@Membership
	public static boolean membership(@In("coord.receiverId") String receiverId,
			@In("member.sensingId") String sensingId) {
		return true;
	}

	@KnowledgeExchange
	public static void bumperExchange(
			@In("member.gps") NavSatFix memberGps,
			@In("member.gpsTime") Long memberGpsTime,
			@In("member.odometry") Point memberOdometry,
			@In("member.pose") PoseWithCovariance memberPose,
			@Out("coord.gps") ParamHolder<NavSatFix> coordGps,
			@Out("coord.gpsTime") ParamHolder<Long> coordGpsTime,
			@Out("coord.odometry") ParamHolder<Point> coordOdometry,
			@Out("coord.pose") ParamHolder<PoseWithCovariance> coordPose) {
		coordGps.value = memberGps;
		coordGpsTime.value = memberGpsTime;
		coordOdometry.value = memberOdometry;
		coordPose.value = memberPose;
	}
}
