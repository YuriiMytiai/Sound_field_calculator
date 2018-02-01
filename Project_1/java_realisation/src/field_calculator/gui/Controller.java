package field_calculator.gui;

import field_calculator.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.rendering.view.ViewportMode;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Controller extends Component {


    private Area area;
    private File sourceFile;
    int number = 1;
    private boolean loadedProject = false;

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
    private boolean wasPressed = false;

    public Pane chart3;
    public ChoiceBox freqChoise;
    public Button singleSourceCalcBut;
    public Button sumFieldCalcBut;
    final ObservableList<String> freqList = FXCollections.observableArrayList();





    public void areaApplyButCallback(ActionEvent actionEvent) {

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

       // area = new RectangularArea(xSize, ySize, xStep, yStep);
        area = new InclinedArea(xSize, ySize, xStep, yStep, 5);

        ImageView imageView = area.plotSurface();
        chart1.getChildren().add(imageView);

        for (double freq:Area.FREQS) {
            freqList.add(Double.toString(freq));
        }
        freqChoise.setValue(Double.toString(Area.FREQS[18]));
        freqChoise.setItems(freqList);

        chart2.getChildren().removeAll();
        chart3.getChildren().removeAll();
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
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filterExtensions = new FileNameExtensionFilter("Sound Sources", "ser");
        fileChooser.setFileFilter(filterExtensions);
        String userDir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(userDir));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            sourceFile = fileChooser.getSelectedFile();
        }
        fileNameText.setText(sourceFile.getName());
    }

    public void singleSourceCalcButCallback(ActionEvent actionEvent) {
        int selectedSource = sourcesList.getSelectionModel().getSelectedIndex();
        try {
            String field = listItems.get(selectedSource);

            String sourceNum = field.split("\\_")[0];
            int freqIdx = freqChoise.getSelectionModel().getSelectedIndex();

            area.sources.get(Integer.parseInt(sourceNum)).calcSourcePreasure(area.gridX, area.gridY, freqIdx);

            chart3.getChildren().removeAll();
            ImageView imageView = area.plotSingleSourceField(freqIdx, Integer.parseInt(sourceNum));
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
            area.sources.get(key).calcSourcePreasure(area.gridX, area.gridY, freqIdx);
        }

        area.calcSummPreasure(freqIdx);

        chart3.getChildren().removeAll();

        ImageView imageView = area.plotSummaryField(freqIdx);
        // JavaFX
        chart3.getChildren().add(imageView);
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



    public void refreshArea(Event event) {

        if (!wasPressed && !loadedProject) {
            ImageView imageView = area.plotSurface();
            chart2.getChildren().add(imageView);
            wasPressed = true;
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

    private static void layout2d(AWTChart chart) {
        View view = chart.getView();
        view.setViewPositionMode(ViewPositionMode.TOP);
        view.getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);

        IAxeLayout axe = chart.getAxeLayout();
        axe.setZAxeLabelDisplayed(false);
        axe.setTickLineDisplayed(false);
    }

    public void loadProjButtCallback(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filterExtensions = new FileNameExtensionFilter("Area object", "ser");
        fileChooser.setFileFilter(filterExtensions);
        String userDir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(userDir));

        File areaFile = null;
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            areaFile = fileChooser.getSelectedFile();
        } else {return;}

        tryOpenAreaFile(areaFile);
        xSizeField.setText(Double.toString(area.xSize));
        ySizeField.setText(Double.toString(area.ySize));
        xStepField.setText(Double.toString(area.xStep));
        yStepField.setText(Double.toString(area.xStep));
        newAreaGroupVisible(true);
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
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filterExtensions = new FileNameExtensionFilter("Serialized objects", "ser");
        fileChooser.setFileFilter(filterExtensions);
        String userDir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(userDir));

        String filename = null;
        String dir = null;
        // Demonstrate "Save" dialog:
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            filename = fileChooser.getSelectedFile().getName();
            if (filename.equals("")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                filename = "Project " + dateFormat.format(date);
            }
            dir = fileChooser.getCurrentDirectory().toString();
        }
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        String fullFile = dir + '\\' + filename;
        try {
            FileOutputStream fileStream = new FileOutputStream((fullFile + ".ser"));
            ObjectOutputStream os = new ObjectOutputStream(fileStream);
            os.writeObject(area);
            os.close();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void newProjButtCallback(ActionEvent actionEvent) {
        loadProjBut.setVisible(false);
        newAreaGroupVisible(true);
        saveProjBut.setVisible(true);
        newProjBut.setVisible(false);
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
    }

    private void tryOpenAreaFile (File areaFile) {
        try {
            FileInputStream fileStream = new FileInputStream(areaFile);
            ObjectInputStream os = new ObjectInputStream(fileStream);
            Object firstObject = os.readObject();
            Class fileClass = firstObject.getClass();
            if(fileClass.toString().equals("class field_calculator.RectangularArea")) {
                area = (RectangularArea) firstObject;
            }
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
