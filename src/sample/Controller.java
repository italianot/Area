package sample;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    public Slider sldUp;
    @FXML
    public Slider sldLow;
    @FXML
    public Slider sldH;
    @FXML
    public Slider sldR;
    @FXML
    public Slider sldZoom;

    @FXML
    public TextField upperBase;

    @FXML
    public TextField lowerBase;

    @FXML
    public TextField height;

    @FXML
    public Label messageLabel;

    @FXML
    public Label finalLabel;

    @FXML
    public Button button;

    @FXML
    public Canvas mainCanvas;

    double offsetX = 0;
    double offsetY = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        draw();

        sldUp.valueProperty().addListener((observable, oldValue, newValue) -> {
            draw();
        });
        sldLow.valueProperty().addListener((observable, oldValue, newValue) -> {
            draw();
        });
        sldH.valueProperty().addListener((observable, oldValue, newValue) -> {
            draw();
        });
        sldR.valueProperty().addListener((observable, oldValue, newValue) -> {
            draw();
        });
        sldZoom.valueProperty().addListener((observable, oldValue, newValue) -> {
            draw();
        });

    }

        void draw(){
            double dpi = Toolkit.getDefaultToolkit().getScreenResolution();
            double zoom = dpi / 2.54 * sldZoom.getValue() / 100;

            GraphicsContext ctx = mainCanvas.getGraphicsContext2D();

            ctx.setFill(Color.WHITE);
            ctx.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

            //сохранение состояния матрицы
            ctx.save();
            //запрос матрицы преобразования
            Affine transform = ctx.getTransform();

            transform.appendTranslation(mainCanvas.getWidth() / 2 + offsetX, mainCanvas.getHeight() / 2 + offsetY);
            //увеличение масштаба
            transform.appendScale(zoom, -zoom);
            //присваиваем обратно
            ctx.setTransform(transform);


            double up = sldUp.getValue();
            double low = sldLow.getValue();
            double h = sldH.getValue();
            double r = sldR.getValue();


            ctx.setLineWidth(2. / zoom);

            ctx.strokeLine(
                    -up / 2,
                    h / 2,
                    -up / 2,
                    -h / 2
            );
            ctx.strokePolygon(
                    new double[]{-up / 2, up / 2, low / 2, -low / 2},
                    new double[]{h / 2, h / 2, -h / 2, -h / 2 },
                    4
            );



            /*ctx.strokeArc(
                     low / 2 - r,
                    -h / 2 - r,
                    2 * r,
                    2 * r,
                    -90,
                    -90,
                    ArcType.ROUND
            );*/

            ctx.restore();// восст. состояние матрицы



        }

    public void onMouseDragged(MouseEvent mouseEvent) {
        offsetX += mouseEvent.getX() - pressedX;
        offsetY += mouseEvent.getY() - pressedY;
        pressedX = mouseEvent.getX();
        pressedY = mouseEvent.getY();
        draw();
    }

    double pressedX;
    double pressedY;
    public void onMousePressed(MouseEvent mouseEvent) {
        pressedX = mouseEvent.getX();
        pressedY = mouseEvent.getY();
    }


        /*public void buttonClick(ActionEvent actionEvent) {
            double up = Integer.parseInt(upperBase.getText());// верхнее основание
            double low = Integer.parseInt(lowerBase.getText());// нижнее основание
            double b = Integer.parseInt(height.getText());// высота



            /*Double Up = up;
            Double Low = low;
            Double B = b;

            if (Up != null && Low != null && B != null){
            // здесь должен быть код который ниже
                messageLabel.setText("Площадь посчитана. Фигура нарисована.");
            }else{
                messageLabel.setText("Ведите данные!");
            }*/

            //if (up < low){
                /*
                       /|
                    a / | b
                     /  |
                    /___|
                      c
                */

                /*double c = (low - up) / 2;
                double a = Math.sqrt(Math.pow(b, 2) + Math.pow(c, 2));
                double p = (a + b + c) / 2;// полупериметр треугольника
                double s = ((c * b) / 2) - (Math.PI * Math.pow((p - a),2)) ;// площадь треугольника минус площадь окружности

                draw();


                messageLabel.setText("Площадь посчитана. Фигура (пока не)нарисована.");
                finalLabel.setText( "Площадь равна: " + s);
            }
            else {
                messageLabel.setText("Верхнее основание должно быть меньше нижнего!");
            }
        }*/




}



