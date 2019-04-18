package za.co.steff.goals.common.type;

import za.co.steff.goals.R;

public class PaletteType {

    public static final int GreenSea = 1;
    public static final int Salmon = 2;
    public static final int Yellow = 3;
    public static final int Blueberry = 4;
    public static final int Orange = 5;
    public static final int Pink = 6;
    public static final int Crimson = 7;
    public static final int Ocean = 8;
    public static final int Lime = 9;
    public static final int Purple = 10;
    public static final int Rose = 11;
    public static final int Sky = 12;
    public static final int Peach = 13;
    public static final int Bedrock = 14;

    public static int getDarkRes(int id) {
        switch(id) {
            case GreenSea:
                return R.color.green_sea_dark;
            case Salmon:
                return R.color.salmon_dark;
            case Yellow:
                return R.color.yellow_dark;
            case Blueberry:
                return R.color.blueberry_dark;
            case Orange:
                return R.color.orange_dark;
            case Crimson:
                return R.color.crimson_dark;
            case Ocean:
                return R.color.ocean_dark;
            case Lime:
                return R.color.lime_dark;
            case Purple:
                return R.color.purple_dark;
            case Rose:
                return R.color.rose_dark;
            case Sky:
                return R.color.sky_dark;
            case Peach:
                return R.color.peach_dark;
            case Bedrock:
                return R.color.bedrock_dark;
            default:
                return R.color.pink_dark;
        }
    }

    public static int getLightRes(int id) {
        switch(id) {
            case GreenSea:
                return R.color.green_sea_light;
            case Salmon:
                return R.color.salmon_light;
            case Yellow:
                return R.color.yellow_light;
            case Blueberry:
                return R.color.blueberry_light;
            case Orange:
                return R.color.orange_light;
            case Crimson:
                return R.color.crimson_light;
            case Ocean:
                return R.color.ocean_light;
            case Lime:
                return R.color.lime_light;
            case Purple:
                return R.color.purple_light;
            case Rose:
                return R.color.rose_light;
            case Sky:
                return R.color.sky_light;
            case Peach:
                return R.color.peach_light;
            case Bedrock:
                return R.color.bedrock_light;
            default:
                return R.color.pink_light;
        }
    }

}
