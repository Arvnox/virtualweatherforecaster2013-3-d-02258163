/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 *
 * @author chucho
 */
public class Forecast 
{
    private Date date;// it is use to save the date of the forecast
    private double forecast;// this is use for store the value of the forecast
    private Scale scale;// this store the kind of scale
    
    public Forecast(Date date, double forecast, Scale scale) {
        this.date = date;
        this.forecast = forecast;
        this.scale = scale;
    }

    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }

    public double getForecast() {
        return forecast;
    }

    public void setForecast(double forecast) {
        this.forecast = forecast;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public String getDateWithFormat(){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
    
    @Override
    public String toString() {
	String dateWithFormat = getDateWithFormat();
        return ""+dateWithFormat+": "+this.forecast+" "+this.scale;
    }
    
}
