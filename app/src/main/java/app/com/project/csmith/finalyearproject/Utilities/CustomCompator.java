package app.com.project.csmith.finalyearproject.Utilities;

import java.util.Comparator;

import app.com.project.csmith.finalyearproject.AsyncTasks.FBFriendDetails;

/**
 * Created by csmith on 05/03/15.
 */
public class CustomCompator implements Comparator<FBFriendDetails> {
    @Override
    public int compare(FBFriendDetails lhs, FBFriendDetails rhs) {
        if (lhs.getMetricDistance() > rhs.getMetricDistance()) {
            return 1;
        } else if (lhs.getMetricDistance() < rhs.getMetricDistance()) {
            return -1;
        } else return 0;
    }
}
