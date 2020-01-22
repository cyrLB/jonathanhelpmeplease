import java.io.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class GUI{
    // Attributes
    private ArrayList<Product> items = new ArrayList<>();
    private TableView<Product> table;
    private TextField nameInput, priceInput, stockLvlInput;
    private Product product;

    // The constructor
    public GUI(){
        //convert every
        ArrayList<Object> loadData;

        if (new File("MyProducts.obj").exists()) {
                try{
                    FileInputStream fis = new FileInputStream("MyProducts.obj");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    items=(ArrayList<Product>) ois.readObject();
                    ois.close();
                    fis.close();

                                        
                } catch(IOException iex){
                    iex.printStackTrace();
                    
                } catch(ClassNotFoundException cnf){
                    cnf.printStackTrace();
                }

            } else {
                items = new ArrayList();
            }

        Stage window = new Stage();

        // Name column
        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Price column
        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price in $");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // StockLevel column
        TableColumn<Product, Integer> stockLvlColumn = new TableColumn<>("Quantity");
        stockLvlColumn.setMinWidth(100);
        stockLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stockLevel"));

        // Name input
        nameInput = new TextField();
        nameInput.setPromptText("Name");
        nameInput.setMinWidth(100);

        // Price input
        priceInput = new TextField();
        priceInput.setPromptText("Price");
        priceInput.setMinWidth(100);

        // Quantity input
        stockLvlInput = new TextField();
        stockLvlInput.setPromptText("Quantity");
        stockLvlInput.setMinWidth(100);

        // Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addButtonClicked());
        Button dltButton = new Button("Delete");
        dltButton.setOnAction(e -> dltButtonClicked());
        Button sellRestockButton = new Button("Sell/Restock");
        sellRestockButton.setOnAction(e -> sellRestockButtonClicked());
        Button setPriceButton = new Button("Set Price");
        setPriceButton.setOnAction(e -> setPriceClicked());
        Button saveButton = new Button("SAVE");
        saveButton.setOnAction(e -> saveButton());

        // HBox layout for inputs and button
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nameInput,priceInput,stockLvlInput,addButton);

        // FlowPane Layout for Delete and Sell/Restock Button
        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10));
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(dltButton, sellRestockButton, setPriceButton, saveButton);

        // Table
        table = new TableView<>();
        table.setItems(getProduct());
        table.getColumns().addAll(nameColumn,priceColumn,stockLvlColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox, hBox2);

        Scene scene = new Scene(vBox, 700,500);
        window.setTitle("Chaos Clothing LLC");
        window.setScene(scene);
        window.show();
    }

    // Add button Listener
    private void addButtonClicked(){
            try
            {
                String nameIn = nameInput.getText();
                double priceIn = Double.parseDouble(priceInput.getText());
                int stockLvlIn = Integer.parseInt(stockLvlInput.getText());
                if(priceIn > 0 && stockLvlIn > 0)
                {
                    Product product = new Product(nameIn, stockLvlIn, priceIn);
                    items.add(product);
                    table.setItems(getProduct());
                    nameInput.clear();
                    priceInput.clear();
                    stockLvlInput.clear();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Unable to add item");
                    alert.setContentText("Price and Quantity must be a positive value !");

                    alert.showAndWait();
                }
            } catch (NumberFormatException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Unable to add item");
                alert.setContentText("Quantity field only allows positive integer values and price field don't allow any characters other than numbers.");

                alert.showAndWait();
            }
    }

    // Delete Button Listener
    private void dltButtonClicked(){
        ObservableList<Product> productSelected = table.getSelectionModel().getSelectedItems();
        for(int i = 0; i < items.size(); i++)
        {
            if (items.get(i) == productSelected.get(0))
                items.remove(i);
        }
        table.setItems(getProduct());
    }

    // Sell/Restock Button Listener
    private void sellRestockButtonClicked(){
            ObservableList<Product> productSelected = table.getSelectionModel().getSelectedItems();
            if (productSelected.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No item selected");
                alert.setContentText("Select an item.");

                alert.showAndWait();
            } else {
                int modItem = 0;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i) == productSelected.get(0))
                        modItem = i;
                }
                sellRestock.transaction(modItem, items);
                table.refresh();
            }
    }

    // Set Price Button Listener
    private void setPriceClicked(){
        ObservableList<Product> productSelected = table.getSelectionModel().getSelectedItems();
        if (productSelected.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No item selected");
            alert.setContentText("Select an item.");

            alert.showAndWait();
        } else {
            int modItem = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) == productSelected.get(0))
                    modItem = i;
            }
            setPrice.change(modItem, items);
            table.refresh();
        }
    }

    // Save Table
    private void saveButton(){
        
        product.TableColumn<Product, String> nameColumn;
        product.TableColumn<Product, Double> priceColumn;
        product.TableColumn<Product, Integer> stockLvlColumn;
/*
        product.nameIn(nameInput.getTable());
        product.priceIn(priceInput.getText());
        product.stockLvlIn(stockLvlInput.getText());
  */          
        try{
            FileOutputStream fo = new FileOutputStream("MyPerson.obj");
            ObjectOutputStream oo = new ObjectOutputStream(fo);
            oo.writeObject(product);
            oo.close();
            fo.close();

        } catch (IOException ex){
            ex.printStackTrace();
        } 

        System.out.println("saved");
    }

    // Get Product Collection
    private ObservableList<Product> getProduct(){
        ObservableList<Product> products = FXCollections.observableArrayList();
        for (int i = 0; i < items.size(); i++)
            products.addAll(items.get(i));
        return products;
    }
}
