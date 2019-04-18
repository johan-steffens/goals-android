package za.co.steff.goals.common.type;

import java.util.ArrayList;
import java.util.List;

public class ChallengeType {

    public static final int Score = 0;
    public static final int Percentage = 1;
    public static final int Daily = 2;
    public static final int Mission = 3;

    private static List<String> names;

    public static List<String> names() {
        if(names == null) {
            names = new ArrayList<>();
            names.add(Score, "Score");
            names.add(Percentage, "Percentage");
            names.add(Daily, "Daily");
            names.add(Mission, "Mission");
        }
        return names;
    }

}
