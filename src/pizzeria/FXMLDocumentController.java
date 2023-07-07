/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//NOTAS
//Al entrar en seleecionarListaDePrecios() se genera dos veces el mensaje
//Queda pendiente usar el DirecoryChooser para guardar los archivos de Ticket en carpeta Custom
package pizzeria;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;

import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modelo.Pizza;
import modelo.Precios;

public class FXMLDocumentController implements Initializable {

    private Pizza pizza;//creamos objeto pizza
    private Precios precios;//creamos objeto precios
    Path archivo;
    Path ruta;

    Set<String> ingredientesExtra = new HashSet<>();
    @FXML
    private RadioButton radioNormal;
    @FXML
    private ToggleGroup grupoRadiosMasa;
    @FXML
    private RadioButton radioIntegral;
    @FXML
    private Label labelMasa;
    @FXML
    private Spinner<String> spinnerTamaño;
    @FXML
    private ComboBox<String> choiceTipo;
    @FXML
    private Label labelTipo;
    @FXML
    private Label labelIngredientes;
    @FXML
    private Label labelTamaño;
    @FXML
    private ListView<String> listViewIngredientes;
    @FXML
    private Label labelConsejoIngredientes;
    @FXML
    private Label labelPedido;
    @FXML
    private TextArea textareaPedido;
    @FXML
    private Label precio;
    @FXML
    private CheckBox bebida;
    @FXML
    private CheckBox gratinada;
    @FXML
    private Button botonGenerar;
    @FXML
    private Button bListadePrecios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pizza = new Pizza();

        precios = new Precios();

        pizza.setPrecios(precios);

        //insertar imagen de tamaño fijo
        //botonGenerar.setGraphic(new ImageView("estilos/orderPizza.png"));
        
        //insertar imagen con ruta y tamaño variable a un boton
        Image image = new Image("/estilos/orderPizza.png",50, 50, true,true);
        
        ImageView imageView = new ImageView(image);
        botonGenerar.setGraphic(imageView);
        
        

        choiceTipo.setItems(FXCollections.observableArrayList(precios.tiposTiposPizza()));

        //CARGAMOS LOS COMPONENTES CON LOS VALORES OBTENIDOS DE LA CLASE PRECIO
        listViewIngredientes.setItems(FXCollections.observableArrayList(precios.tiposIngrediente()));
        listViewIngredientes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); //PERMITE SELECCIONAR VARIOS ELEMENTOS CON TECLA CTRL

        ListSpinnerValueFactory<String> factoryTamaños = new ListSpinnerValueFactory(FXCollections.observableArrayList(precios.tiposTamaño()));
        spinnerTamaño.setValueFactory(factoryTamaños);

        // valores por defecto 
        radioNormal.setSelected(true);
        choiceTipo.setValue("Básica");
        spinnerTamaño.setAccessibleText("pequeña");

     

        calcularPrecioComposicion();
    }

    private void masa() {
        //SE OBTIENE EL VALOR SELECCIONADO EN EL RADIO BUTTON DE MASA
        String masa = ((RadioButton) grupoRadiosMasa.getSelectedToggle()).getText();
        //SE PASA LA MASA SELECCIONADA A LA PIZZA
        pizza.setMasa(masa);
       
    }

    public void tamaño() {
        String tamaño = spinnerTamaño.getValue();
        pizza.setTamaño(tamaño);
    }

    private void ingredientes() {

        //LOS INGREDIENTES SELECCIONADOS LOS ALMACENAMOS EN UN SET
        for (String ingrediente : listViewIngredientes.getSelectionModel().getSelectedItems()) {
            if (ingrediente != null) {
                ingredientesExtra.add(ingrediente);
            }
        }

        //ALMACENAMOS LOS INGREDIENTES EN LA PIZZA
        pizza.setIngredientesExtra(ingredientesExtra);
    }

    private void tipo() {
        String tipo = choiceTipo.getValue();
        pizza.setTipo(tipo);
    }

    @FXML
    private void calcularPrecioComposicion() {//He cambiado el metodo de event a normal, para poder iniciarlo al principio 
        clearPizza();
        masa();
        tipo();
        tamaño();
        ingredientes();
        eligeBebida();
        eligeGratinada();

        textareaPedido.setText(pizza.composicion());
        precio.setText(String.format("PRECIO TOTAL %.2f€", pizza.calcularPrecio()));
    }

    public void clearPizza() {
        //borramos el area de texto y la lista de ingredientes extra
        textareaPedido.setText("");
        ingredientesExtra.clear();

    }

    public void eligeGratinada() {
        boolean eleccionGratinada = gratinada.selectedProperty().getValue();
        pizza.setGratinada(eleccionGratinada);
    }

    public void eligeBebida() {
        boolean eligeBebida = bebida.selectedProperty().getValue();
        pizza.setBebida(eligeBebida);

    }

    public void imprimirPizza() {

        //String cadenaPedido = "";
    }

    @FXML
    private void generarTicket() throws IOException {

        ruta = Paths.get("tickets");

        pizza.generarTicket(ruta);

    }

    private void cargarFileChooser() {
        //Queda pendiente que solo pueda coger txt
        //Seleccionamos el archivo de la lista de precios
        //de un directorio
        //CREAMOS EL OBJETO FILECHOOSER
        FileChooser fileChooser = new FileChooser();
        //Asignamos una ruta por defecto
        fileChooser.setInitialDirectory( new File("precios"));
      
         String direccion="precios";
       
        //ABRIMOS VENTANA FILECHOOSER Y RECUPERAMOS EL FICHERO SELECCIONADO
        File file = fileChooser.showOpenDialog(null);
        
        if (file != null) {//REVISAMOS SI EL USUARIO HA SELECCIONADO UN FICHERO O NO
          direccion = file.getPath();
          ruta = Paths.get(direccion);
                 
          
        } else {
            
        Alert seleccionDestino = new Alert(Alert.AlertType.WARNING);
        seleccionDestino.setHeaderText(null);
        seleccionDestino.setTitle("NO HAS SELECCIONADO NUNGUN ARCHIVO");
        seleccionDestino.showAndWait();
        
        }

    }

    public void cargarArchivoPrecios() throws IOException {
        archivo = Paths.get("precios");
        cargarFileChooser();
        precios.cargaPrecios(archivo);

    }

    @FXML
    public void seleecionarListaDePrecios() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Seleccione que lista de precios desea");
        alert.setHeaderText("Si selecciona aceptar, cargará la nueva lista de precios, Si selecciona cancelar, usará la lista de precios por defecto");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                cargarArchivoPrecios();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        alert.showAndWait();
        calcularPrecioComposicion();
    }

}
