/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.scene.control.Alert;
import pizzeria.Pizzeria;

public class Pizza {
    //  private int identificador;

    final double conBebida = 2.00;
    final double conGratinado = 2.00;//es porcentaje%
    Double incrementoGratinada = 0.00;
    int numPedido = 1;
    String rutacompleta;

    private String tipoMasa;
    private String tipoPizza;
    private Set<String> ingredientesExtra;
    private String tamaño;
    private boolean bebida;
    private boolean gratinada;
    String fechaString;
    String nombreArchivo;
    private Precios precios;
    Double precioTotal;
     

    public Pizza() {

    }

    public boolean isBebida() {
        return bebida;
    }

    public void setBebida(boolean bebida) {
        this.bebida = bebida;
    }

    public boolean isGratinada() {
        return gratinada;
    }

    public void setGratinada(boolean gratinada) {
        this.gratinada = gratinada;
    }

    public Pizza(String masa, String tipo, Set<String> ingredientesExtra, String tamaño) {
        this.tipoMasa = masa;
        this.tipoPizza = tipo;
        this.ingredientesExtra = ingredientesExtra;
        this.tamaño = tamaño;
    }

    
    public String getMasa() {
        return tipoMasa;
    }

    public void setMasa(String masa) {
        this.tipoMasa = masa;
    }

    public String getTipo() {
        return tipoPizza;
    }

    public void setTipo(String tipo) {
        this.tipoPizza = tipo;
    }

    public Set<String> getIngredientesExtra() {
        return ingredientesExtra;
    }

    public void setIngredientesExtra(Set<String> ingredientesExtra) {
        this.ingredientesExtra = ingredientesExtra;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    public Precios getPrecios() {
        return precios;
    }

    public void setPrecios(Precios precios) {
        this.precios = precios;
    }

    public Double calcularPrecio() {
        
      
        
        Double precioMasa = precios.precioDeMasa(this.tipoMasa);
        Double precioTipo = precios.precioDeTipo(this.tipoPizza);
        Double porcentajeTamaño = precios.porcentajeDeTamaño(this.tamaño);
        Double precioIngredientes = precios.precioDeIngredientes(this.ingredientesExtra);
        Double precioBebida = 0.00;

        if (bebida == true) {
            precioBebida = conBebida;
        }

        precioTotal = (precioMasa + precioTipo + precioIngredientes);
        precioTotal += (precioTotal * (porcentajeTamaño / 100));
       

        if (gratinada == true) {
            incrementoGratinada = conGratinado * (precioTotal / 100);
            
            precioTotal += incrementoGratinada;
           
        }
        precioTotal += precioBebida;
        

        return precioTotal;
        
    }

    public String composicion() {
       
        String cadena = "";

        Double precioMasa = precios.precioDeMasa(this.tipoMasa);
        Double precioTipo = precios.precioDeTipo(this.tipoPizza);
        Double porcentajeTamaño = precios.porcentajeDeTamaño(this.tamaño);
        Double precioIngredientes = precios.precioDeIngredientes(this.ingredientesExtra);
        String bebidaString = "";
        Double precioBebida = conBebida;
        Double increGratinar = 0.00;

        if (bebida == true) {
            bebidaString = "Si";

        } else {
            bebidaString = "No";
            precioBebida = 0.00;
        }
        if (gratinada == true) {
            increGratinar = incrementoGratinada;
           
        }

        cadena += "masa : " + this.tipoMasa + " - " + precioMasa + "€" + "\n";
        cadena += "tipo: : " + this.tipoPizza + " - " + precioTipo + "€" + "\n";
        cadena += "INGREDIENTES EXTRA: " + this.ingredientesExtra + " - " + precioIngredientes + "€" + "\n";
        cadena += "tamaño : " + this.tamaño + " - " + porcentajeTamaño + "%" + "\n";
        cadena += "\n El cliente " + bebidaString + " quiere bebida, el coste es: " + precioBebida + "€" + "\n";
        cadena += "\n El gratinado " +   String.format("%.2f", increGratinar);
        cadena += "\n El total del pedido es: " +   String.format("%.2f", precioTotal);

        return cadena;
    }

    public Boolean generarTicket(Path ruta) throws IOException {
        //Creara un archivo de texto en la ruta pasada como parámetro, 
        //con la composición de la pizza y su precio total, a modo de Ticket, 
        //incluyendo fecha y hora en la cabecera

        Date fecha = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd-MM-YYYY_hh-mm-ss");
        fechaString = df.format(fecha);
        nombreArchivo = "ticket- " + numPedido + "-" + fechaString + ".txt";
        //quedaría como nombreArchivo = ticket-fecha.txt en la cabecera

        //generamos un booleano de si existe la ruta o no
        boolean existe = Files.exists(ruta);

        //generamos un string con la ruta y el nombre del archivo
        rutacompleta = ruta.toString() + "\\" + nombreArchivo;
        //le indicamos la ruta completa del archivo con su nombre de archivo
        Path rutaArchivo = Paths.get(rutacompleta);

        //generamos un try para escribir en el archivo que hay en rutaArchivo
        try ( BufferedWriter escribir = Files.newBufferedWriter(rutaArchivo, StandardOpenOption.CREATE)) {

            //ponemos en cabecera el nombre del ticket a modo de numeracion
            escribir.write(nombreArchivo);
            escribir.newLine();
            //imprimimos la composicion de la pizza
            escribir.write(composicion());
            escribir.newLine();
            escribir.write("Gracias por confiar en nuestra pizzeria");
            escribir.newLine();

            numPedido++;

        } catch (IOException e) {
            System.out.println("No se ha podido imprimir");
        }
        rutaDeGuardado();
        return existe;
    }

    //imprimimos alerta de donde se guarda el ticket
    public void rutaDeGuardado() {
        Alert PopUp = new Alert(Alert.AlertType.INFORMATION);
        PopUp.setHeaderText(null);
        PopUp.setTitle("Hemos generado su ticket correctamente");
        PopUp.setContentText("Esta guardado para su visualización en: "+ rutacompleta);
        PopUp.showAndWait();
    }
}
