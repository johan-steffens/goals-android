package za.co.steff.goals.data.database;

import android.content.Context;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import za.co.steff.goals.R;
import za.co.steff.goals.common.type.PaletteType;
import za.co.steff.goals.data.database.entity.Aspect;
import za.co.steff.goals.data.database.entity.Challenge;
import za.co.steff.goals.data.database.entity.Palette;

public class DatabaseSeeder {

    private Box<Aspect> aspectBox;
    private Box<Challenge> challengeBox;
    private Box<Palette> paletteBox;

    private Context context;

    public DatabaseSeeder(Context context, BoxStore boxStore) {
        this.context = context;
        this.aspectBox = boxStore.boxFor(Aspect.class);
        this.challengeBox = boxStore.boxFor(Challenge.class);
        this.paletteBox = boxStore.boxFor(Palette.class);
    }

    public void seedInitialDataset() {
        // Seed Palettes
        Palette greenSea = new Palette();
        greenSea.setName("Green Sea");
        greenSea.setPaletteType(PaletteType.GreenSea);
        paletteBox.put(greenSea);

        Palette salmon = new Palette();
        salmon.setName("Salmon");
        salmon.setPaletteType(PaletteType.Salmon);
        paletteBox.put(salmon);

        Palette blueberry = new Palette();
        blueberry.setName("Blueberry");
        blueberry.setPaletteType(PaletteType.Blueberry);
        paletteBox.put(blueberry);

        Palette orange = new Palette();
        orange.setName("Orange");
        orange.setPaletteType(PaletteType.Orange);
        paletteBox.put(orange);

        Palette pink = new Palette();
        pink.setName("Pink");
        pink.setPaletteType(PaletteType.Pink);
        paletteBox.put(pink);

        Palette crimson = new Palette();
        crimson.setName("Crimson");
        crimson.setPaletteType(PaletteType.Crimson);
        paletteBox.put(crimson);

        Palette ocean = new Palette();
        ocean.setName("Ocean");
        ocean.setPaletteType(PaletteType.Ocean);
        paletteBox.put(ocean);

        Palette lime = new Palette();
        lime.setName("Lime");
        lime.setPaletteType(PaletteType.Lime);
        paletteBox.put(lime);

        Palette purple = new Palette();
        purple.setName("Purple");
        purple.setPaletteType(PaletteType.Purple);
        paletteBox.put(purple);

        Palette rose = new Palette();
        rose.setName("Rose");
        rose.setPaletteType(PaletteType.Rose);
        paletteBox.put(rose);

        Palette sky = new Palette();
        sky.setName("Sky");
        sky.setPaletteType(PaletteType.Sky);
        paletteBox.put(sky);

        Palette peach = new Palette();
        peach.setName("Peach");
        peach.setPaletteType(PaletteType.Peach);
        paletteBox.put(peach);

        Palette bedrock = new Palette();
        bedrock.setName("Bedrock");
        bedrock.setPaletteType(PaletteType.Bedrock);
        paletteBox.put(bedrock);

        Palette yellow = new Palette();
        yellow.setName("Yellow");
        yellow.setPaletteType(PaletteType.Yellow);
        paletteBox.put(yellow);

        // Seed Aspects
        Aspect exercise = new Aspect();
        exercise.setName("Exercise");
        exercise.setGoal("Get through toe injury so that I can pick up more intense cardio again.");
        exercise.getPalette().setTarget(crimson);
        aspectBox.put(exercise);

        Aspect detox = new Aspect();
        detox.setName("Detox");
        detox.setGoal("Make it to day 100!");
        detox.getPalette().setTarget(lime);
        aspectBox.put(detox);

        Aspect careerGoals = new Aspect();
        careerGoals.setName("Career Goals");
        careerGoals.setGoal("Level up.");
        careerGoals.getPalette().setTarget(ocean);
        aspectBox.put(careerGoals);

        Aspect studies = new Aspect();
        studies.setName("Studies");
        studies.setGoal("Finish first course.");
        studies.getPalette().setTarget(blueberry);
        aspectBox.put(studies);

        Aspect diet = new Aspect();
        diet.setName("Diet");
        diet.setGoal("Stick to ketogenic diet for duration of detox.");
        diet.getPalette().setTarget(rose);
        aspectBox.put(diet);

        Aspect hobbies = new Aspect();
        hobbies.setName("Hobbies");
        hobbies.setGoal("Start getting in touch with old hobbies again.");
        hobbies.getPalette().setTarget(orange);
        aspectBox.put(hobbies);

        Aspect personalFinance = new Aspect();
        personalFinance.setName("Personal Finance");
        personalFinance.setGoal("Get started with long-term financial plan.");
        personalFinance.getPalette().setTarget(pink);
        aspectBox.put(personalFinance);
    }

}
