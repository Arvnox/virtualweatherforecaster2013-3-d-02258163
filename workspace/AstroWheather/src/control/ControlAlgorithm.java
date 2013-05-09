/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import boundary.ResultsWindow;
import entity.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lin
 */
public class ControlAlgorithm {

    /*Arreglo estatico donde se guardan los resultados del SMA para ejecutar
     * el DMA
     */
//    static List<Double> arrayDMA = new ArrayList<>();

    /*public static void main(String[] args) {
     new ControlAlgorithm().SMA(new ArrayList<Reporte>());
     new ControlAlgorithm().SMA(new ArrayList<Reporte>());
     new ControlAlgorithm().SMA(new ArrayList<Reporte>());
     new ControlAlgorithm().ES(new ArrayList<Reporte>());
     new ControlAlgorithm().DMA();
     }*/
    private static ResultsWindow resultsWindow = new ResultsWindow();
    
    /**
     * @param forecast 
     * @return 
     */
    private static double ES(List<Forecast> forecast) {
        /*forecast.add(new Forecast(123, Calendar.getInstance()));
         forecast.add(new Forecast(123, Calendar.getInstance()));
         forecast.add(new Forecast(123, Calendar.getInstance()));
         * */

        /*Inicialización de las variables
         */
        double datos[] = new double[forecast.size()];
        Calendar fecha = Calendar.getInstance();

        /*
         * un alfa que uno determina al azar entre 0 y 1, sientanse libres de
         * cambiarlo
         */
        double a;
        a = 0.7;

        /*Guardar solo las temperaturas de dicho arreglo
         */
        for (int k = 0; k < forecast.size(); k++) {
            datos[k] = forecast.get(k).getForecast();
        }

        /*
         * Si solo hay un elemento (una temperatura) por definición, la
         * predicción solo retornará dicho elemento
         */
        double resultES = datos[0];

        if (datos.length == 1) {
            return resultES;
        }

        /*
         * Se aplica la fórmula para n>1 elementos del arreglo de datos
         */
        for (int k = 0; k < datos.length; k++) {
            resultES = (a * datos[k]) + ((1 - a) * resultES);
        }
        //System.out.println("resultES: " + resultES);
        return resultES;

    }

    /**
     * @param forecast 
     * @return 
     */
    private static double SMA(List<Forecast> forecast) {
        /*
         forecast.add(new Forecast(123, Calendar.getInstance()));
         forecast.add(new Forecast(123, Calendar.getInstance()));
         forecast.add(new Forecast(123, Calendar.getInstance()));
         * */
        /*Inicialización de las variables
         */
        Calendar fecha = Calendar.getInstance();
        double datos[] = new double[forecast.size()];

        /*Guardar solo las temperaturas en el arreglo
         */
        for (int k = 0; k < forecast.size(); k++) {
            datos[k] = forecast.get(k).getForecast();
        }

        double resultSMA = average(datos);

//        arrayDMA.add(resultSMA);

        //System.out.println("resultSMA: " + resultSMA);
        /*Retorno del algoritmo
         */
        return resultSMA;
    }

    /**
     * @param forecast 
     * @return 
     */
    private static double DMA(List<Forecast> forecast) {
        /*Inicialización de las variables
         */

        if (forecast.size() <= 3) {
            return SMA(forecast);
        }

        double resultDMA = 0;
        double datos[] = new double[forecast.size()];

        /*Guardar las temperaturas en el arreglo
         */
        for (int k = 0; k < datos.length; k++) {
            datos[k] = forecast.get(k).getForecast();
        }

        /*Realizar la sumatoria de las temperaturas
         */
        for (int i = 2, j = 0; i < datos.length; i++, j++) {
            resultDMA += average(Arrays.copyOfRange(datos, j, i + 1));

        }

        /*division entre la cantidad de datos para terminar el promedio
         */
        resultDMA /= (datos.length - 2);

        //System.out.println("resultDMA: " + resultDMA);

        /*Retorno del algoritmo
         */
        return resultDMA;
    }

    /**
     * @param data 
     * @return 
     */
    private static double DES(List<Forecast> data) {
        
        double alpha = 0.7;
        double beta = 0.7;
        
        double s[] = new double[data.size()];
        double b[] = new double[data.size()];
        
        s[0] = data.get(0).getForecast();
        b[0] = data.get(1).getForecast()-s[0];
        
        for(int i=1;i<data.size();i++){
            s[i] = alpha*data.get(i).getForecast()+(1-alpha)*(s[i-1]+b[i-1]);
            b[i] = beta*(s[i]-s[i-1])+(1-beta)*b[i-1];
        }
        
        return s[data.size()-1]+b[data.size()-1];
    }

    /**
     * @param forecasts 
     * @param data 
     * @return 
     */
    private static double mape(double[] data, double[] forecasts) {
        double result = 0;

        for (int i = 0; i < forecasts.length; i++) {
            result += Math.abs((data[i] - forecasts[i]) / data[i]);
        }

        return result;
    }

    /**
     * @param date 
     * @param data
     */
    public static void generateForecast(Date date, List<Forecast> data) {
        double[] results = new double[data.size()];
        double[] nextResults = new double[data.size()];
        double[] dataForecasts = new double[data.size()];
        double dataDispertion;
        
        for (int i = 0; i < data.size(); i++) {
            dataForecasts[i] = data.get(i).getForecast();
        }
        /*
         * DMA
         * ES
         * SMA
         * DES
         */

        //DMA
        List<Forecast> dataIteration = new ArrayList<>(data);
        
        for (int i = 0; i < data.size(); i++) {
            results[i] = DMA(dataIteration);
            
            //Get Last Forecast
            Forecast lastForecast = dataIteration.get(dataIteration.size() - 1);
            Calendar nextDate = Calendar.getInstance();
            nextDate.setTime(lastForecast.getDate());
            nextDate.add(Calendar.DAY_OF_YEAR, 1);
            
            //Delete first entry and append the last Forecast
            dataIteration.add(new Forecast(nextDate.getTime(), results[i], 
                    Scale.KELVIN));
            dataIteration.remove(0);
        }

        dataDispertion = mape(dataForecasts, results);
        
        //SMA
        dataIteration = new ArrayList<>(data);
        
        for (int i = 0; i < data.size(); i++) {
            nextResults[i] = SMA(dataIteration);
            
            //Get Last Forecast
            Forecast lastForecast = dataIteration.get(dataIteration.size() - 1);
            Calendar nextDate = Calendar.getInstance();
            nextDate.setTime(lastForecast.getDate());
            nextDate.add(Calendar.DAY_OF_YEAR, 1);
            
            //Delete first entry and append the last Forecast
            dataIteration.add(new Forecast(nextDate.getTime(), nextResults[i], 
                    Scale.KELVIN));
            dataIteration.remove(0);
        }
        
        if(isBetterAlgorithm(dataForecasts, results, nextResults)) {
            results = nextResults;
            dataDispertion = mape(dataForecasts, results);
        }

        nextResults = new double[data.size()];
        
        //ES
        dataIteration = new ArrayList<>(data);
        
        for (int i = 0; i < data.size(); i++) {
            nextResults[i] = ES(dataIteration);
            
            //Get Last Forecast
            Forecast lastForecast = dataIteration.get(dataIteration.size() - 1);
            Calendar nextDate = Calendar.getInstance();
            nextDate.setTime(lastForecast.getDate());
            nextDate.add(Calendar.DAY_OF_YEAR, 1);
            
            //Delete first entry and append the last Forecast
            dataIteration.add(new Forecast(nextDate.getTime(), nextResults[i], 
                    Scale.KELVIN));
            dataIteration.remove(0);
        }
        
        if(isBetterAlgorithm(dataForecasts, results, nextResults)) {
            results = nextResults;
            dataDispertion = mape(dataForecasts, results);
        }

        nextResults = new double[data.size()];
        
        //DES
        dataIteration = new ArrayList<>(data);
        
        for (int i = 0; i < data.size(); i++) {
            nextResults[i] = DES(dataIteration);
            
            //Get Last Forecast
            Forecast lastForecast = dataIteration.get(dataIteration.size() - 1);
            Calendar nextDate = Calendar.getInstance();
            nextDate.setTime(lastForecast.getDate());
            nextDate.add(Calendar.DAY_OF_YEAR, 1);
            
            //Delete first entry and append the last Forecast
            dataIteration.add(new Forecast(nextDate.getTime(), nextResults[i], 
                    Scale.KELVIN));
            dataIteration.remove(0);
        }
        
        if(isBetterAlgorithm(dataForecasts, results, nextResults)) {
            results = nextResults;
            dataDispertion = mape(dataForecasts, results);
        }

        nextResults = new double[data.size()];
        
        //Create the Forecasts with the best results
        List<Forecast> resultForecast = new ArrayList<>(data.size() * 2);
        
        for (double result : results) {
            Calendar nextDate = Calendar.getInstance();
            nextDate.setTime(date);
            nextDate.add(Calendar.DAY_OF_YEAR, 1);
            date = (Date) nextDate.getTime().clone();
            resultForecast.add(new Forecast(date, result, Scale.KELVIN));
        }
        
        resultsWindow.setVisible(true);
        resultsWindow.showForecast(dataDispertion, resultForecast, data);
    }

    /**
     * @param data 
     * @param result 
     * @param newResult  
     * @return 
     */
    private static boolean isBetterAlgorithm(double[] data, double[] result,
            double[] newResult) {
        return mape(data, result) > mape(data, newResult);
    }

    /**
     * @param datos 
     * @return 
     */
    private static double average(double[] datos) {
        double resultSMA = 0;

        /*Realizar las sumatoria de las temperaturas
         */
        for (int k = 0; k < datos.length; k++) {
            resultSMA += (datos[k]);

        }
        /*division entre la cantidad de datos para terminar el promedio
         */
        resultSMA /= datos.length;
        return resultSMA;
    }
}
