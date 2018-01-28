package field_calculator.gui;

import field_calculator.Area;
import field_calculator.SoundSource;
import field_calculator.Source;
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
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.*;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.IntegerTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.rendering.view.ViewportMode;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Transform;
import org.jzy3d.plot3d.transform.Translate;
import org.jzy3d.plot3d.rendering.legends.colorbars.AWTColorbarLegend;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

        area = new Area(xSize, ySize, xStep, yStep);
        plotAreaOnChart1();

        for (double freq:Area.FREQS) {
            freqList.add(Double.toString(freq));
        }
        freqChoise.setValue(Double.toString(Area.FREQS[18]));
        freqChoise.setItems(freqList);

        chart2.getChildren().removeAll();
        chart3.getChildren().removeAll();
    }

    private void plotAreaOnChart1() {
        Shape surface = buildSurface();
        JavaFXChartFactory factory = new JavaFXChartFactory();
        Quality quality = Quality.Intermediate;
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");
        chart.getScene().getGraph().add(surface);
        chart.getView().setSquared(false);

        chart.getAxeLayout().setXTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setYTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setZTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setXAxeLabel("X");
        chart.getAxeLayout().setYAxeLabel("Y");
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        chart.getView().setMaximized(true);
        chart.addMouseCameraController();

        ImageView imageView = factory.bindImageView(chart);
        chart1.getChildren().add(imageView);
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
        plotSources(-1, false);
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
            Source curSource = new Source(number, name, curPosition, soundSource);
            curSource.tau0 = delay;
            curSource.phi0 = (int) phi;
            curSource.theta0 = (int) theta;
            curSource.kAmp = gain;

            area.addSource(curSource, number);
            number++;

            listItems.add(name);
            sourcesList.setItems(listItems);

            plotSources(-1, false);
            resetFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(),
                    ex.getLocalizedMessage(),
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

            Shape surface = buildPreasureFieldShape(area.sources.get(Integer.parseInt(sourceNum)).preasureAbs[freqIdx]);
            JavaFXChartFactory factory = new JavaFXChartFactory();
            Quality quality = Quality.Advanced;
            AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");

            AWTColorbarLegend cbar = new AWTColorbarLegend(surface, chart.getView().getAxe().getLayout());
            surface.setLegend(cbar);
            surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display

            chart.getScene().getGraph().add(surface);
            chart.getView().setSquared(false);
            chart.getView().getCamera().setScreenGridDisplayed(false);
            chart.getAxeLayout().setXTickRenderer( new IntegerTickRenderer() );
            chart.getAxeLayout().setYTickRenderer( new IntegerTickRenderer() );
            chart.getAxeLayout().setZTickRenderer( new IntegerTickRenderer() );
            chart.getAxeLayout().setXAxeLabel("X");
            chart.getAxeLayout().setYAxeLabel("Y");
            chart.getAxeLayout().setYAxeLabel("dB SPL");
            org.jzy3d.maths.Scale zScale = new org.jzy3d.maths.Scale(surface.getBounds().getZmin(), surface.getBounds().getZmax());
            chart.getView().setScaleZ(zScale);
            chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
            chart.addMouseCameraController();
            chart.getView().setMaximized(true);

            //layout2d(chart);

            ImageView imageView = factory.bindImageView(chart);
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

        Shape surface = buildPreasureFieldShape(area.sumFieldAbs[freqIdx]);
        JavaFXChartFactory factory = new JavaFXChartFactory();
        Quality quality = Quality.Intermediate;
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");

        AWTColorbarLegend cbar = new AWTColorbarLegend(surface, chart.getView().getAxe().getLayout());
        surface.setLegend(cbar);
        surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display

        chart.getScene().getGraph().add(surface);
        chart.getView().setSquared(false);
        chart.getView().getCamera().setScreenGridDisplayed(false);
        chart.getAxeLayout().setXTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setYTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setZTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setXAxeLabel("X");
        chart.getAxeLayout().setYAxeLabel("Y");
        chart.getAxeLayout().setYAxeLabel("dB SPL");
        org.jzy3d.maths.Scale zScale = new org.jzy3d.maths.Scale(surface.getBounds().getZmin(), surface.getBounds().getZmax());
        chart.getView().setScaleZ(zScale);
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        chart.addMouseCameraController();
        chart.getView().setMaximized(true);
        //layout2d(chart);

        ImageView imageView = factory.bindImageView(chart);
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

    private void plotSources(int selectedNum, boolean turnLightOn) {
        chart2.getChildren().removeAll();

        Shape surface = buildSurface();
        JavaFXChartFactory factory = new JavaFXChartFactory();
        Quality quality = Quality.Intermediate;
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");
        chart.getScene().getGraph().add(surface);

        double soundAxisLength = Math.max(area.xSize, area.ySize)*0.3;
        double maxZPosition = 0;
        for (Integer key:area.sources.keySet()) {
            Source curSource = area.sources.get(key);

            if (curSource.position[2] > maxZPosition) {maxZPosition = curSource.position[2];}

            double xMin = - curSource.soundSourceObj.getCentralPoint()[0];
            double yMin = - curSource.soundSourceObj.getCentralPoint()[1];
            double zMin = - curSource.soundSourceObj.getCentralPoint()[2];
            double xMax = xMin + curSource.soundSourceObj.getSizes()[0];
            double yMax = yMin + curSource.soundSourceObj.getSizes()[1];
            double zMax = zMin + curSource.soundSourceObj.getSizes()[2];

            BoundingBox3d bounds = new BoundingBox3d((float) xMin, (float) xMax, (float) yMin, (float) yMax, (float) zMin, (float) zMax);
            Parallelepiped box = new Parallelepiped(bounds);

            Color c1 = new Color(255, 0, 0);
            Point p1 = new Point(new Coord3d(0,0,0), c1);
            Color c2 = new Color(178, 255, 102);
            Point p2 = new Point(new Coord3d(soundAxisLength,0,0), c2);
            LineStrip sourceAxis = new LineStrip(p1, p2);

            if(key == selectedNum && turnLightOn) {
                box.setColorMapper(new ColorMapper(new ColorMapGrayscale(), 0, 0, new Color(0, 0, 0, 0f)));
            } else {
                box.setColorMapper(new ColorMapper(new ColorMapRedAndGreen(), -5, 30, new Color(1f, 1, 1f, 0.35f)));
            }

            Transform transform = new Transform();
            Rotate rotateTheta = new Rotate(-curSource.theta0, new Coord3d(0, curSource.position[1] + curSource.soundSourceObj.getCentralPoint()[1], 0));
            transform.add(rotateTheta);
            box.applyGeometryTransform(transform);
            sourceAxis.applyGeometryTransform(transform);
            transform = null;

            transform = new Transform();
            Rotate rotatePhi = new Rotate(-curSource.phi0, new Coord3d(0, 0, curSource.position[2] + curSource.soundSourceObj.getCentralPoint()[2]));
            transform.add(rotatePhi);
            box.applyGeometryTransform(transform);
            sourceAxis.applyGeometryTransform(transform);

            Transform transformShift = new Transform();
            Translate shift = new Translate(new Coord3d(curSource.position[0], curSource.position[1], curSource.position[2]));
            transformShift.add(shift);
            box.applyGeometryTransform(transformShift);
            sourceAxis.applyGeometryTransform(transformShift);
            chart.getScene().getGraph().add(box);
            chart.getScene().getGraph().add(sourceAxis);

            rotateTheta = null;
            rotatePhi = null;
            transform = null;
        }
        BoundingBox3d bbox = new BoundingBox3d();
        bbox.setXmax((float) area.xSize);
        bbox.setXmin((float) (0));
        bbox.setYmax((float) area.ySize);
        bbox.setYmin((float) (0));
        bbox.setZmax((float) (maxZPosition + 1));
        bbox.setZmin((float) (0));
        chart.getView().setBoundManual(bbox);
        chart.getAxeLayout().setXAxeLabel("X");
        chart.getAxeLayout().setYAxeLabel("Y");
        chart.getAxeLayout().setZAxeLabel("Z");
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        chart.addMouseCameraController();

        chart.getView().setSquared(false);
        chart.getAxeLayout().setXTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setYTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setZTickRenderer( new IntegerTickRenderer() );

        ImageView imageView = factory.bindImageView(chart);
        // JavaFX
        chart2.getChildren().add(imageView);
    }

    private Shape buildSurface() {
        double[][] x = area.gridX;
        double[][] y = area.gridY;
        double z = 0;

        // Create the 3d object
        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < (x.length - 1); i++) {
            for (int j = 0; j < (x[0].length - 1); j++) {
                Polygon polygon = new Polygon();
                polygon.add(new Point( new Coord3d(x[i][j], y[i][j], z) ));
                polygon.add(new Point( new Coord3d(x[i][j+1], y[i][j+1], z)));
                polygon.add(new Point( new Coord3d(x[i+1][j+1], y[i+1][j+1], z)));
                polygon.add(new Point( new Coord3d(x[i+1][j], y[i+1][j], z)));
                polygons.add(polygon);
            }
        }

        // Jzy3d
        Shape surface = new Shape(polygons);
        surface.setColorMapper(new ColorMapper(new ColorMapGrayscale(), -5, 1, new Color(1,1,1,1f)));
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.GRAY);
        return surface;
    }

    private Shape buildPreasureFieldShape(double[][] pressure) {
        double[][] x = area.gridX;
        double[][] y = area.gridY;

        // Create the 3d object
        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < (x.length - 1); i++) {
            for (int j = 0; j < (x[0].length - 1); j++) {
                Polygon polygon = new Polygon();
                polygon.add(new Point( new Coord3d(x[i][j], y[i][j], pressure[i][j]) ));
                polygon.add(new Point( new Coord3d(x[i][j+1], y[i][j+1], pressure[i][j+1])));
                polygon.add(new Point( new Coord3d(x[i+1][j+1], y[i+1][j+1], pressure[i+1][j+1])));
                polygon.add(new Point( new Coord3d(x[i+1][j], y[i+1][j], pressure[i+1][j])));
                polygons.add(polygon);
            }
        }

        // Jzy3d
        Shape surface = new Shape(polygons);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbowNoBorder(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,1f)));
        surface.setWireframeDisplayed(false);
        surface.setWireframeColor(Color.GRAY);

        return surface;
    }

    public void refreshArea(Event event) {

        if (!wasPressed && !loadedProject) {
            Shape surface = buildSurface();
            JavaFXChartFactory factory = new JavaFXChartFactory();
            Quality quality = Quality.Intermediate;
            AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");
            chart.getScene().getGraph().add(surface);
            chart.getView().setSquared(false);

            chart.getAxeLayout().setXTickRenderer( new IntegerTickRenderer() );
            chart.getAxeLayout().setYTickRenderer( new IntegerTickRenderer() );
            chart.getAxeLayout().setZTickRenderer( new IntegerTickRenderer() );
            chart.getAxeLayout().setXAxeLabel("X");
            chart.getAxeLayout().setYAxeLabel("Y");
            chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
            chart.addMouseCameraController();

            ImageView imageView = factory.bindImageView(chart);
            chart2.getChildren().add(imageView);
            wasPressed = true;
        }
    }

    public void selectSoundSource(MouseEvent mouseEvent) {
        int selectedString = sourcesList.getSelectionModel().getSelectedIndex();
        String field = listItems.get(selectedString);
        String sourceNum = field.split("\\_")[0];
        plotSources(Integer.parseInt(sourceNum), true);
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

        plotSources(-1, false);
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

        plotAreaOnChart1();

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

        plotSources(-1, false);
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
            if (filename == null) {
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
            area = (Area) firstObject;
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
