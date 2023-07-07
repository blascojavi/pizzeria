/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.sun.deploy.util.StringUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import pizzeria.FXMLDocumentController;

public class Precios {

    private Map<String, Double> tipoMasa = new HashMap<>();
    private Map<String, Double> tipoPizza = new HashMap<>();
    private Map<String, Double> ingredientes = new HashMap<>();
    private Map<String, Double> tamaño = new HashMap<>();

    public Precios() {
        tipoMasa.put("Normal", 3.0);
        tipoMasa.put("Integral", 3.5);

        ingredientes.put("jamón", 0.50);
        ingredientes.put("queso", 0.75);
        ingredientes.put("Tomate", 1.50);
        ingredientes.put("cebolla", 0.75);
        ingredientes.put("Olivas", 1.00);

        tipoPizza.put("Básica", 3.00);
        tipoPizza.put("cuatro quesos", 5.00);
        tipoPizza.put("Barbacoa", 7.00);
        tipoPizza.put("Mexicana", 8.50);

        tamaño.put("pequeña", 0.00);
        tamaño.put("mediana", 15.00);
        tamaño.put("familiar", 30.00);

    }

    public Precios(
            Map<String, Double> masa, Map<String, Double> tipo, Map<String, Double> ingredientes, Map<String, Double> tamaño
    ) {
        this.tipoMasa = masa;
        this.tipoPizza = tipo;
        this.ingredientes = ingredientes;
        this.tamaño = tamaño;
    }

    public Double precioDeMasa(String masa) {
        return this.tipoMasa.get(masa);
    }

    public Double precioDeTipo(String tipo) {
        return this.tipoPizza.get(tipo);
    }

    public Double precioDeIngredientes(Set<String> ingredientesAsumar) {
        Double precioIngredientes = 0.00;

        for (String ingrediente : ingredientesAsumar) {
            precioIngredientes += ingredientes.get(ingrediente);
        }

        return precioIngredientes;
    }

    public Double porcentajeDeTamaño(String tamaño) {
        return this.tamaño.get(tamaño);
    }

    public Set<String> tiposMasa() {
        return tipoMasa.keySet();
    }

    public Set<String> tiposTiposPizza() {
        return tipoPizza.keySet();
    }

    public Set<String> tiposIngrediente() {
        return ingredientes.keySet();
    }

    public Set<String> tiposTamaño() {
        return tamaño.keySet();
    }

    public void cargaPrecios(Path archivo) throws IOException {

        String nombre = "listaPrecios.txt";

        String rutacompleta = archivo.toString() + "\\" + nombre;
        Path rutaArchivo = Paths.get(rutacompleta);

        String tMasa = "@tipoMasa";
        String tPizza = "@tipoPizza";
        String ingre = "@ingredientes";
        String tama = "@tamaño";
        String arroba = "@";
        

        //Borramos los datos de los Maps antes de rellenarlos con los nuevos
        tipoMasa.clear();
        tipoPizza.clear();
        ingredientes.clear();
        tamaño.clear();

        try ( Stream<String> datos = Files.lines(rutaArchivo)) {
            Iterator<String> it = datos.iterator();
            String nombreMap = it.next();//leemos la primera linea
            String primerCaracterMap = String.valueOf(nombreMap.charAt(0));

            while (it.hasNext()) {

                //separamos las lineas por los : en dos partes
                String string = it.next();
                //comparar el string para ver si es normal o con arroba
                String pruebaArroba = String.valueOf(string.charAt(0));
                if (pruebaArroba.equals(arroba)) {
                    nombreMap = string;//actualzamos el nombre del map
                } else {
                    
                    String[] parts = string.split(":");
                    String part1 = parts[0];
                    String part2 = parts[1];

                    if (nombreMap.equals(tMasa)) {
                        tipoMasa.put(part1, Double.parseDouble(part2));
                    }

                    if (nombreMap.equals(tPizza)) {
                        tipoPizza.put(part1, Double.parseDouble(part2));

                    }

                    if (nombreMap.equals(ingre)) {
                        ingredientes.put(part1, Double.parseDouble(part2));

                    }
                    if (nombreMap.equals(tama)) {
                        tamaño.put(part1, Double.parseDouble(part2));

                    }
                }//corchete Else

            }//corchete del while

        } catch (IOException e) {
            System.out.println("No se han podido guardar los datos");

        }

    }
}
