/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author chucho
 */
public enum Scale {
    //the scales are celsius, farenheit, kelvin and rankine

    CELSIUS("°C", "Celsius"),
    FAHRENHEIT("°F", "Fahrenheit"),
    KELVIN("°K", "Kelvin"),
    RANKINE("°R", "Rankine");
    private final String name;
    private final String longName;

    Scale(String name, String longName) {
        this.name = name;
        this.longName = longName;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getLongName() {
        return this.longName;
    }
}
