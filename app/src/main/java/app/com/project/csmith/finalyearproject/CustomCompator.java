package app.com.project.csmith.finalyearproject;

import java.util.Comparator;

/**
 * Created by csmith on 05/03/15.
 */
public class CustomCompator implements Comparator<FBFriendDetails> {
    @Override
    public int compare(FBFriendDetails lhs, FBFriendDetails rhs) {
        if(lhs.getDistance() > rhs.getDistance()){
            return 1;
        }
        else if (lhs.getDistance() < rhs.getDistance()){
            return -1;
        }
        else return 0;
    }
}
