package com.hamza.youtubedownload;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Names {

    public SimpleStringProperty stringProperty = new SimpleStringProperty();

    public Names(String stringProperty) {
        this.stringProperty = new SimpleStringProperty(stringProperty);
    }

    public String getStringProperty() {
        return stringProperty.get();
    }

    public SimpleStringProperty stringPropertyProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty.set(stringProperty);
    }
}
