package pl.mareksliwinski;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Numbers {

    private IntegerProperty id;
    private IntegerProperty number;


    public Numbers(int id, int number){
        this.id = new SimpleIntegerProperty(id);
        this.number = new SimpleIntegerProperty(number);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getNumber() {
        return number.get();
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }
}

