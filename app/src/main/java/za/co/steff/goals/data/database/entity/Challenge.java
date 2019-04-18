package za.co.steff.goals.data.database.entity;

import org.joda.time.DateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToOne;
import za.co.steff.goals.data.database.converter.DateTimeConverter;

@Entity
public class Challenge extends BaseEntity {

    private ToOne<Aspect> aspect;
    private int type;
    private String completionCondition;
    private int progress;
    private int goal;
    private String reward;
    @Convert(converter = DateTimeConverter.class, dbType = Long.class)
    private DateTime expiresAt;

    public ToOne<Aspect> getAspect() {
        return aspect;
    }

    public void setAspect(ToOne<Aspect> aspect) {
        this.aspect = aspect;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompletionCondition() {
        return completionCondition;
    }

    public void setCompletionCondition(String completionCondition) {
        this.completionCondition = completionCondition;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public DateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(DateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "aspect=" + aspect +
                ", type=" + type +
                ", completionCondition='" + completionCondition + '\'' +
                ", progress=" + progress +
                ", goal=" + goal +
                ", reward='" + reward + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
