package za.co.steff.goals.data.database.entity;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Palette extends BaseEntity {

    private String name;
    private int paletteType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPaletteType() {
        return paletteType;
    }

    public void setPaletteType(int paletteType) {
        this.paletteType = paletteType;
    }

    public static Palette getForPaletteType(int paletteType, Box<Palette> box) {
        return box.query().equal(Palette_.paletteType, paletteType).build().findFirst();
    }

    @Override
    public String toString() {
        return "Palette{" +
                "name='" + name + '\'' +
                ", paletteType=" + paletteType +
                '}';
    }
}
