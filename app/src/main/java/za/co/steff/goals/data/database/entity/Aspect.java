package za.co.steff.goals.data.database.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToOne;

@Entity
public class Aspect extends BaseEntity {

    private String name;
    private String goal;
    private ToOne<Challenge> challenge;
    private ToOne<Palette> palette;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public ToOne<Challenge> getChallenge() {
        return challenge;
    }

    public void setChallenge(ToOne<Challenge> currentChallenge) {
        this.challenge = currentChallenge;
    }

    public ToOne<Palette> getPalette() {
        return palette;
    }

    public void setPalette(ToOne<Palette> palette) {
        this.palette = palette;
    }

    @Override
    public String toString() {
        return "Aspect{" +
                "name='" + name + '\'' +
                ", goal='" + goal + '\'' +
                ", challenge=" + challenge +
                ", palette=" + palette +
                '}';
    }
}
