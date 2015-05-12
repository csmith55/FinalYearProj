package app.com.project.csmith.finalyearproject.Utilities;

import java.util.Comparator;

import app.com.project.csmith.finalyearproject.DistanceTasks.FBFriendDetails;

/**
 * Compares the distances of two FBFriendDetails and orders them
 */
public class DistanceComparator implements Comparator<FBFriendDetails> {
    @Override
    public int compare(FBFriendDetails lhs, FBFriendDetails rhs) {
        if (lhs.getMetricDistance() > rhs.getMetricDistance()) {
            return 1;
        } else if (lhs.getMetricDistance() < rhs.getMetricDistance()) {
            return -1;
        } else return 0;
    }
}
