package field_calculator.gui;

import field_calculator.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.swing.*;


import java.awt.*;
import java.io.*;


public class Controller extends Component {

    private Area area;
    private File sourceFile;
    int number = 1;
    private boolean loadedProject = false;
    String[] areaTypes = { "Select area type", "Rectangular area", "Inclined rectangular area", "Circus area"};

    public Pane chart1;
    public TextField xSizeField;
    public TextField xStepField;
    public TextField ySizeField;
    public TextField yStepField;
    public Button areaApplyBut;
    public Button loadProjBut;
    public Button saveProjBut;
    public Label yStepLabel;
    public Label ySizeLabel;
    public Button newProjBut;
    public Label xStepLabel;
    public Label xSizeLabel;
    public Label hSizeLabel;
    public TextField hSizeTextField;
    public ChoiceBox areaTypeChooser;
    final ObservableList<String> areasList = FXCollections.observableArrayList();

    public Tab selSourceTab;
    public Pane chart2;
    public ListView sourcesList;
    public Button addSourceBut;
    public Button modSourceBut;
    public Button delSourceBut;
    public SubScene addModifyScene;
    public Button applySourceBut;
    public TextField xSourceField;
    public TextField ySourceField;
    public TextField zSourceField;
    public TextField delaySourceField;
    public TextField phiSourceField;
    public TextField thetaSourceField;
    public TextField gainSourceField;
    public Button prototypeBut;
    public Label xText;
    public Label yText;
    public Label zText;
    public Label delayText;
    public Label phiText;
    public Label thetaText;
    public Label gainText;
    public Label fileNameText;
    final ObservableList<String> listItems = FXCollections.observableArrayList();
    public Button applyModificationBut;
    private boolean wasPressed2Tab = false;

    public Pane chart3;
    public ChoiceBox freqChoise;
    public Button singleSourceCalcBut;
    public Button sumFieldCalcBut;
    final ObservableList<String> freqList = FXCollections.observableArrayList();
    public ToggleButton zAxisSPL;
    public ToggleButton zAxisHeight;
    final ToggleGroup axisGroup = new ToggleGroup();
    private boolean showSPLonZ = true;
    private boolean wasPressed3Tab = false;





    public void areaApplyButCallback(ActionEvent actionEvent) {

        int areaTypeIdx = areaTypeChooser.getSelectionModel().getSelectedIndex();
        switch (areaTypeIdx) {
            case 1: createRectArea();
                break;
            case 2: createRectInclinedArea();
                break;
            case 3: createCircusArea();
                break;

        }

        ImageView imageView = area.plotSurface();
        chart1.getChildren().add(imageView);

        for (double freq:Area.FREQS) {
            freqList.add(Double.toString(freq));
        }
        freqChoise.setValue(Double.toString(Area.FREQS[18]));
        freqChoise.setItems(freqList);

        listItems.removeAll();
        sourcesList.setItems(listItems);
        number = 1;
        plotSources();


        chart2.getChildren().removeAll();
        chart3.getChildren().removeAll();

        readyForSourcesNotification();
    }

    public void addSourceButCallback(ActionEvent actionEvent) {
        addSourceGroupVisible(true);
    }

    public void modSourceButCallback(ActionEvent actionEvent) {
        addSourceGroupVisible(true);
        applySourceBut.setVisible(false);
        applyModificationBut.setVisible(true);
        prototypeBut.setVisible(false);

        int selectedString = sourcesList.getSelectionModel().getSelectedIndex();
        String field = listItems.get(selectedString);
        String sourceNum = field.split("\\_")[0];
        Source selectedSource = area.sources.get(Integer.parseInt(sourceNum));

        xSourceField.setText(Double.toString(selectedSource.position[0]));
        ySourceField.setText(Double.toString(selectedSource.position[1]));
        zSourceField.setText(Double.toString(selectedSource.position[2]));
        delaySourceField.setText(Double.toString(selectedSource.tau0));
        phiSourceField.setText(Double.toString(selectedSource.phi0));
        thetaSourceField.setText(Double.toString(selectedSource.theta0));
        gainSourceField.setText(Double.toString(selectedSource.kAmp));
    }

    public void delSourceButCallback(ActionEvent actionEvent) {
        int selectedSource = sourcesList.getSelectionModel().getSelectedIndex();
        String field = listItems.get(selectedSource);
        String sourceNum = field.split("\\_")[0];
        area.sources.remove(Integer.parseInt(sourceNum));
        listItems.remove(selectedSource);
        sourcesList.setItems(listItems);
        plotSources();
    }

    public void applySourceButCallback(ActionEvent actionEvent) {
        if(sourceFile == null) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Choose serializable file with SoundSource object!",
                    "File error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        double xPos = Double.parseDouble(xSourceField.getText());
        double yPos = Double.parseDouble(ySourceField.getText());
        double zPos = Double.parseDouble(zSourceField.getText());
        double delay = Double.parseDouble(delaySourceField.getText());
        double phi = Double.parseDouble(phiSourceField.getText());
        double theta = Double.parseDouble(thetaSourceField.getText());
        double gain = Double.parseDouble(gainSourceField.getText());

        double[] curPosition = {xPos, yPos, zPos};
        try {
            FileInputStream fileStream = new FileInputStream(sourceFile);
            ObjectInputStream os = new ObjectInputStream(fileStream);
            Object firstObject = os.readObject();
            SoundSource soundSource = (SoundSource) firstObject;
            os.close();

            String name = number + "_" + soundSource.getName();
            // add Single source
            SingleSource curSource = new SingleSource(number, name, curPosition, soundSource);
            curSource.tau0 = delay;
            curSource.phi0 = (int) phi;
            curSource.theta0 = (int) theta;
            curSource.kAmp = gain;

            area.addSource(curSource, number);
            number++;

            listItems.add(name);
            sourcesList.setItems(listItems);

            plotSources();
            resetFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(),
                    ex.getMessage(),
                    "Add source error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } finally {
            addSourceGroupVisible(false);
        }

    }

    public void prototypeButCallback(ActionEvent actionEvent) {

        Stage stage = new Stage();
        stage.setTitle("Select SoundSource file");
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SoundSource object", "*.ser"));

        String userDir = System.getProperty("user.dir");
        fileChooser.setInitialDirectory(new File(userDir));

        sourceFile = fileChooser.showOpenDialog(stage);
        if (sourceFile == null) return;

        fileNameText.setText(sourceFile.getName());
    }

    public void singleSourceCalcButCallback(ActionEvent actionEvent) {
        int selectedSource = sourcesList.getSelectionModel().getSelectedIndex();
        try {
            String field = listItems.get(selectedSource);

            String sourceNum = field.split("\\_")[0];
            int freqIdx = freqChoise.getSelectionModel().getSelectedIndex();

            area.sources.get(Integer.parseInt(sourceNum)).calcSourcePreasure(area.gridX, area.gridY, area.gridZ, freqIdx);

            chart3.getChildren().removeAll();
            ImageView imageView = area.plotSingleSourceField(freqIdx, Integer.parseInt(sourceNum), showSPLonZ);
            // JavaFX
            chart3.getChildren().add(imageView);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Please, select sound source in Sources tab!",
                    "Source selection error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void sumFieldCalcButCallback(ActionEvent actionEvent) {
        int freqIdx = freqChoise.getSelectionModel().getSelectedIndex();

        for (Integer key: area.sources.keySet()) {
            area.sources.get(key).calcSourcePreasure(area.gridX, area.gridY, area.gridZ, freqIdx);
        }

        area.calcSummPreasure(freqIdx);

        chart3.getChildren().removeAll();

        ImageView imageView = area.plotSummaryField(freqIdx, showSPLonZ);
        // JavaFX
        chart3.getChildren().add(imageView);
    }

    public void refreshArea(Event event) {

        if (!wasPressed2Tab && !loadedProject) {
            ImageView imageView = area.plotSurface();
            chart2.getChildren().add(imageView);
            wasPressed2Tab = true;
        }
    }

    public void selectSoundSource(MouseEvent mouseEvent) {
        int selectedString = sourcesList.getSelectionModel().getSelectedIndex();
        String field = listItems.get(selectedString);
        String sourceNum = field.split("\\_")[0];
        ImageView imageView = area.plotLightedSource(Integer.parseInt(sourceNum));
        chart2.getChildren().removeAll();
        chart2.getChildren().add(imageView);
    }

    public void applyModificationButCallback(ActionEvent actionEvent) {
        int selectedString = sourcesList.getSelectionModel().getSelectedIndex();
        String field = listItems.get(selectedString);
        String sourceNum = field.split("\\_")[0];
        Source selectedSource = area.sources.get(Integer.parseInt(sourceNum));

        double xPos = Double.parseDouble(xSourceField.getText());
        double yPos = Double.parseDouble(ySourceField.getText());
        double zPos = Double.parseDouble(zSourceField.getText());
        double delay = Double.parseDouble(delaySourceField.getText());
        double phi = Double.parseDouble(phiSourceField.getText());
        double theta = Double.parseDouble(thetaSourceField.getText());
        double gain = Double.parseDouble(gainSourceField.getText());
        double[] curPosition = {xPos, yPos, zPos};

        selectedSource.position = curPosition;
        selectedSource.tau0 = delay;
        selectedSource.phi0 = (int) phi;
        selectedSource.theta0 = (int) theta;
        selectedSource.kAmp = gain;

        area.sources.replace(Integer.parseInt(sourceNum), selectedSource);

        resetFields();
        applyModificationBut.setVisible(false);
        addSourceGroupVisible(false);

        plotSources();
    }

    public void loadProjButtCallback(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setTitle("Select Project file");
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Area object", "*.ser"));

        String userDir = System.getProperty("user.dir");
        fileChooser.setInitialDirectory(new File(userDir));


        File areaFile = fileChooser.showOpenDialog(stage);
        if (areaFile == null) return;

        tryOpenAreaFile(areaFile);

        areaApplyBut.setVisible(false);

        ImageView imageView = area.plotSurface();
        chart1.getChildren().add(imageView);

        for (double freq:Area.FREQS) {
            freqList.add(Double.toString(freq));
        }
        freqChoise.setValue(Double.toString(Area.FREQS[18]));
        freqChoise.setItems(freqList);

        for (Integer key: area.sources.keySet()) {
            Source curSource = area.sources.get(key);
            String name = curSource.name;
            listItems.add(name);
            sourcesList.setItems(listItems);
        }

        plotSources();
        loadProjBut.setVisible(false);
        saveProjBut.setVisible(true);

        loadedProject = true;
    }

    public void saveProjButtCallback(ActionEvent actionEvent) {

        Stage stage = new Stage();
        stage.setTitle("Save Project file");
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Area object", "*.ser"));

        String userDir = System.getProperty("user.dir");
        fileChooser.setInitialDirectory(new File(userDir));


        File areaFile = fileChooser.showSaveDialog(stage);
        if (areaFile == null) return;

        try {
            FileOutputStream fileStream = new FileOutputStream(areaFile);
            ObjectOutputStream os = new ObjectOutputStream(fileStream);
            os.writeObject(area);
            os.close();
        } catch (Exception ex) {ex.printStackTrace();}

    }

    public void newProjButtCallback(ActionEvent actionEvent) {
        loadProjBut.setVisible(false);

        for (String areaType:areaTypes) {
            areasList.add(areaType);
        }
        areaTypeChooser.setValue(areaTypes[0]);
        areaTypeChooser.setItems(areasList);
        areaTypeChooser.setVisible(true);

        areaTypeChooser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                switch (newValue.intValue()) {
                    case 1: showRectInclinedAreaTextFields();
                        break;
                    case 2: showRectAreaTextFields();
                        break;
                    case 3: showCircusAreaTextFields();
                        break;
                    default: return;
                }

                saveProjBut.setVisible(true);
                newProjBut.setVisible(false);
            }
        });

        newProjBut.setVisible(false);

    }

    public void splOnZaxis(ActionEvent actionEvent) { showSPLonZ = true; }

    public void heightOnZaxis(ActionEvent actionEvent) { showSPLonZ = false; }

    public void calculationsSelected(Event event) {
        if (!wasPressed3Tab) {
            createZaxisToggleGroup();
            wasPressed3Tab = true;
        }
    }

    public void zAxisMouseEntered(MouseEvent mouseEvent) {
        Notifications notif1 = Notifications.create();
        notif1.title("Warning message");
        notif1.text("This configuration is slower than 'SPL on Z axis'");
        notif1.hideAfter(Duration.seconds(3));
        notif1.position(Pos.CENTER);
        notif1.owner(chart3);
        notif1.showWarning();
    }





    private void addSourceGroupVisible(boolean setVisibleValue) {
        xSourceField.setVisible(setVisibleValue);
        ySourceField.setVisible(setVisibleValue);
        zSourceField.setVisible(setVisibleValue);
        delaySourceField.setVisible(setVisibleValue);
        phiSourceField.setVisible(setVisibleValue);
        thetaSourceField.setVisible(setVisibleValue);
        gainSourceField.setVisible(setVisibleValue);
        prototypeBut.setVisible(setVisibleValue);
        xText.setVisible(setVisibleValue);
        yText.setVisible(setVisibleValue);
        zText.setVisible(setVisibleValue);
        delayText.setVisible(setVisibleValue);
        phiText.setVisible(setVisibleValue);
        thetaText.setVisible(setVisibleValue);
        gainText.setVisible(setVisibleValue);
        applySourceBut.setVisible(setVisibleValue);
        fileNameText.setVisible(setVisibleValue);
    }

    private void resetFields() {
        xSourceField.setText("0");
        ySourceField.setText("0");
        zSourceField.setText("0");
        delaySourceField.setText("0");
        phiSourceField.setText("0");
        thetaSourceField.setText("0");
        gainSourceField.setText("0");

    }

    private void plotSources() {
        chart2.getChildren().removeAll();
        ImageView imageView = area.plotSources();
        // JavaFX
        chart2.getChildren().add(imageView);
    }

    private void createZaxisToggleGroup() {
        zAxisSPL.setToggleGroup(axisGroup);
        zAxisSPL.setSelected(true);
        zAxisHeight.setToggleGroup(axisGroup);
    }

    private void newAreaGroupVisible(boolean visible) {
        xSizeLabel.setVisible(visible);
        xSizeField.setVisible(visible);
        xStepLabel.setVisible(visible);
        xStepField.setVisible(visible);
        ySizeLabel.setVisible(visible);
        ySizeField.setVisible(visible);
        yStepLabel.setVisible(visible);
        yStepField.setVisible(visible);
        areaApplyBut.setVisible(visible);
        hSizeTextField.setVisible(visible);
        hSizeLabel.setVisible(visible);
    }

    private void tryOpenAreaFile (File areaFile) {
        try {
            FileInputStream fileStream = new FileInputStream(areaFile);
            ObjectInputStream os = new ObjectInputStream(fileStream);
            Object firstObject = os.readObject();
            Class fileClass = firstObject.getClass();
            if(fileClass.toString().equals("class field_calculator.RectangularArea")) {
                area = (RectangularArea) firstObject;
                xSizeField.setText(Double.toString(area.xSize));
                ySizeField.setText(Double.toString(area.ySize));
                xStepField.setText(Double.toString(area.xStep));
                yStepField.setText(Double.toString(area.xStep));
                newAreaGroupVisible(true);
                hSizeLabel.setVisible(false);
                hSizeTextField.setVisible(false);
            } else if (fileClass.toString().equals("class field_calculator.InclinedArea")) {
                area = (InclinedArea) firstObject;
                xSizeField.setText(Double.toString(area.xSize));
                ySizeField.setText(Double.toString(area.ySize));
                xStepField.setText(Double.toString(area.xStep));
                yStepField.setText(Double.toString(area.xStep));
                hSizeTextField.setText(Double.toString(area.zSize));
                newAreaGroupVisible(true);
            }
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void readyForSourcesNotification() {
        Notifications notif2 = Notifications.create();
        notif2.title("Info message");
        notif2.text(new StringBuilder("Ready to add sources. \n You can move to 'Sources' tab").toString());
        notif2.hideAfter(Duration.seconds(4));
        notif2.position(Pos.CENTER);
        notif2.owner(chart1);
        notif2.showInformation();
    }



    private void showRectInclinedAreaTextFields() {
        newAreaGroupVisible(true);
        hSizeTextField.setVisible(false);
        hSizeLabel.setVisible(false);

        xSizeLabel.setText("X Size");
        xSizeLabel.setLayoutX(55);

        ySizeLabel.setText("Y Size");
        ySizeLabel.setLayoutX(55);
    }

    private void createRectInclinedArea() {
        double xSize = Double.parseDouble(xSizeField.getText());
        double ySize = Double.parseDouble(ySizeField.getText());
        double xStep = Double.parseDouble(xStepField.getText());
        double yStep = Double.parseDouble(yStepField.getText());
        double hMax = Double.parseDouble(hSizeTextField.getText());

        if ((xSize <= 0) || (ySize <= 0) || (xStep <= 0) || (yStep <= 0) || (xStep >= xSize) || (yStep >= ySize) || (hMax <= 0)) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Please, enter a valid numbers!",
                    "Invalid values error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        area = new InclinedArea(xSize, ySize, xStep, yStep, hMax);
    }

    private void showRectAreaTextFields() {
        newAreaGroupVisible(true);
        xSizeLabel.setText("X Size");
        xSizeLabel.setLayoutX(55);

        ySizeLabel.setText("Y Size");
        ySizeLabel.setLayoutX(55);
    }

    private void createRectArea() {
        double xSize = Double.parseDouble(xSizeField.getText());
        double ySize = Double.parseDouble(ySizeField.getText());
        double xStep = Double.parseDouble(xStepField.getText());
        double yStep = Double.parseDouble(yStepField.getText());

        if ((xSize <= 0) || (ySize <= 0) || (xStep <= 0) || (yStep <= 0) || (xStep >= xSize) || (yStep >= ySize)) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Please, enter a valid numbers!",
                    "Invalid values error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        area = new RectangularArea(xSize, ySize, xStep, yStep);
    }

    private void showCircusAreaTextFields() {
        newAreaGroupVisible(true);
        xSizeLabel.setText("Inner radius");
        xSizeLabel.setLayoutX(25);

        ySizeLabel.setText("Outer radius");
        ySizeLabel.setLayoutX(25);
    }

    private void createCircusArea() {
        double rInner = Double.parseDouble(xSizeField.getText());
        double rOuter = Double.parseDouble(ySizeField.getText());
        double xStep = Double.parseDouble(xStepField.getText());
        double yStep = Double.parseDouble(yStepField.getText());
        double hMax = Double.parseDouble(hSizeTextField.getText());

        if ((rInner <= 0) || (rOuter <= 0) || (xStep <= 0) || (yStep <= 0) || (xStep >= rInner) || (yStep >= rInner) ||
                (hMax <= 0) || (rInner >= rOuter)) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Please, enter a valid numbers!",
                    "Invalid values error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        area = new CircusArea(rInner, rOuter, xStep, yStep, hMax);
    }

}
