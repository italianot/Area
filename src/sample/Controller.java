package sample;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Affine;

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
    public Slider sldZoom;
    @FXML
    public Label messageLabel;
    @FXML
    public Label finalLabel;
    @FXML
    public Canvas mainCanvas;

    double offsetX = 0;
    double offsetY = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        draw();
        count();

        sldUp.valueProperty().addListener((observable, oldValue, newValue) -> {
            draw();
            count();
        });
        sldLow.valueProperty().addListener((observable, oldValue, newValue) -> {
            draw();
            count();
        });
        sldH.valueProperty().addListener((observable, oldValue, newValue) -> {
            draw();
            count();
        });
        sldZoom.valueProperty().addListener((observable, oldValue, newValue) -> {
            draw();
            count();
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

            double c = (low - up) / 2;
            double a = Math.sqrt(Math.pow(h, 2) + Math.pow(c, 2));

            double r = (c + h - a) / 2;
            double d = 2 * r;

            //толщина линии
            ctx.setLineWidth(2. / zoom);
            ctx.setLineJoin(StrokeLineJoin.BEVEL);
            //треугольник
            ctx.strokePolygon(
                    new double[]{-up / 2, -up / 2, -low / 2},
                    new double[]{h / 2, -h / 2, -h / 2 },
                    3
            );
            //заливка треугольника
            ctx.setFill(Color.RED);
            ctx.fillPolygon(
                    new double[]{-up / 2, -up / 2, -low / 2},
                    new double[]{h / 2, -h / 2, -h / 2 },
                    3
            );
            //трапеция
            ctx.strokePolygon(
                    new double[]{-up / 2, up / 2, low / 2, -low / 2},
                    new double[]{h / 2, h / 2, -h / 2, -h / 2 },
                    4
            );

            //окружность
            ctx.strokeOval(
                    (-up / 2) - d,
                    (-h / 2),
                    2 * r,
                    2 * r
            );
            ctx.setFill(Color.WHITE);
            ctx.fillOval(
                    (-up / 2) - d,
                    (-h / 2),
                    2 * r,
                    2 * r
            );
            // восст. состояние матрицы
            ctx.restore();
        }

        void count(){
            double up = sldUp.getValue();
            double low = sldLow.getValue();
            double h = sldH.getValue();

            if (up < low){
                /*
                       /|
                    a / | b
                     /  |
                    /___|
                      c
                */

                double c = (low - up) / 2;
                double a = Math.sqrt(Math.pow(h, 2) + Math.pow(c, 2));
                double p = (a + h + c) / 2;// полупериметр треугольника
                double s = ((c * h) / 2) - (Math.PI * Math.pow((p - a),2)) ;// площадь треугольника минус площадь окружности

                messageLabel.setText("Площадь посчитана. Фигура нарисована.");
                finalLabel.setText( "Площадь равна: "+ s + " см");
            }
            else {
                messageLabel.setText("Верхнее основание должно быть меньше нижнего!");
                finalLabel.setText( "Площадь не может быть посчитана");
            }
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
}