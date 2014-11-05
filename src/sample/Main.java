package sample;


import javafx.scene.input.MouseButton;
import javafx.scene.shape.Line;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;


public class Main extends Application {
    //----------------------Model---------------------//
    public static ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
    public static ArrayList<ILine> lineList = new ArrayList<ILine>();
    public static Vertex vertex;
    public static ILine line;

    public static int i = 0;   //счетчик вершин
//----------------------ModelEND---------------------//


    public double getSampleWidth() {
        return 500;
    }

    public double getSampleHeight() {
        return 500;
    }

    private Group root;

    private javafx.scene.control.Button NewFile;

    private javafx.scene.control.Button DeleteObject;
    private javafx.scene.control.Button Empty;
    private javafx.scene.control.Button Refresh;


    private ImageView background;
    private ImageView image;

    private double initX;
    private double initY;
    private Point2D dragAnchor;
    private ToolBar toolbar;


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Graph Editor");
        root = new Group();

        Scene scene = new Scene(root, 800, 450);
        primaryStage.setScene(scene);
        scene.getStylesheets().add
                (Main.class.getResource("/sample/Style.css").toExternalForm());
        primaryStage.show();


        background = new ImageView(getClass().getResource("11.jpg").toExternalForm());
        image = new ImageView(getClass().getResource("11.jpg").toExternalForm());
        root.getChildren().addAll(background, image);


//-----------------------------TOOLBAR-------------------------------------------//
        toolbar = new ToolBar();
        toolbar.getItems().add(NewFile = new javafx.scene.control.Button("New"));

        toolbar.getItems().add(Refresh = new javafx.scene.control.Button("Refresh"));


        root.getChildren().add(toolbar);

        toolbar.setId("toolbar");

        toolbar.setMinSize(950, 40);


        NewFile.setId("NewFile");


        Refresh.setId("Refresh");

        root.setId("All");




        NewFile.setMinSize(70, 30);



        Refresh.setMinSize(70, 30);



//-----------------------------TOOLBAR_end-------------------------------------------//



        image.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //when mouse is pressed, store initial position


                dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
                final Circle circleBig = createCircle("Circle", javafx.scene.paint.Color.BLUE, 30);

                if ((me.getSceneY()>=50+circleBig.getRadius())&&(me.getSceneY()>=circleBig.getRadius()) && (me.getSceneY()<=450-circleBig.getRadius())) {
                    circleBig.setTranslateY(me.getSceneY());


                    circleBig.setTranslateX(me.getSceneX());

                    System.out.println(circleBig.getTranslateX());
                    System.out.println(circleBig.getTranslateY());

                    vertex = new Vertex(++i, circleBig.getTranslateX(), circleBig.getTranslateY(), 30);
                    vertexList.add(vertex);
                    System.out.println(vertex.getNum() + " " + vertex.getX() + " " + vertex.getY());
                    root.getChildren().add(circleBig);
                    circleBig.setId("Circle");
                }
            }

        });
        Refresh.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Refresh();
            }
        });
        NewFile.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                image.toFront();
                toolbar.toFront();
                vertexList = new ArrayList<Vertex>();
                lineList=new ArrayList<ILine>();
            }
        });
    }


    private void DeleteC(MouseEvent me) {


        for (int i = vertexList.size() - 1; i >= 0; i--) {
            if (Math.pow((me.getSceneX() - vertexList.get(i).getX()), 2) + Math.pow((me.getSceneY() - vertexList.get(i).getY()), 2) < Math.pow(vertexList.get(i).getRadius(), 2)) {

                for (int j = lineList.size() - 1; j >= 0; j--) {
                    if (lineList.get(j).getStartVertex() == vertexList.get(i) || lineList.get(j).getEndVertex() == vertexList.get(i)) {
                        lineList.remove(lineList.get(j));
                    }

                }
                vertexList.remove(vertexList.get(i));
                Refresh();
            }
        }


    }

    private void Refresh() {
        image.toFront();
        toolbar.toFront();

        for (int j = 0; j < lineList.size(); j++) {
            final Line line = new Line();
            final Line line1 = new Line();
            final Line line2 = new Line();
            line.setStartX(lineList.get(j).getStartVertex().getX());
            line.setStartY(lineList.get(j).getStartVertex().getY());
            line.setEndX(lineList.get(j).getEndVertex().getX());
            line.setEndY(lineList.get(j).getEndVertex().getY());
            line.setFill(null);
            line.setStroke(javafx.scene.paint.Color.RED);
            line.setStrokeWidth(2);


            Double k = Math.atan((lineList.get(j).getEndVertex().getY() - lineList.get(j).getStartVertex().getY()) / (lineList.get(j).getEndVertex().getX() - lineList.get(j).getStartVertex().getX()));
            if (lineList.get(j).getStartVertex().getX() > lineList.get(j).getEndVertex().getX()) {
                line.setEndX(lineList.get(j).getEndVertex().getX() + lineList.get(j).getEndVertex().getRadius() * Math.cos(k));
                line.setEndY(lineList.get(j).getEndVertex().getY() + lineList.get(j).getEndVertex().getRadius() * Math.sin(k));
            } else {
                line.setEndX(lineList.get(j).getEndVertex().getX() - lineList.get(j).getEndVertex().getRadius() * Math.cos(k));
                line.setEndY(lineList.get(j).getEndVertex().getY() - lineList.get(j).getEndVertex().getRadius() * Math.sin(k));
            }
            CreateRis(line.getEndX(), line.getEndY(), line, line1, line2);


            root.getChildren().addAll(line, line1, line2);
        }
        for (int j = 0; j < vertexList.size(); j++) {
            final Circle circle = createCircle("Circle", javafx.scene.paint.Color.BLUE, vertexList.get(j).getRadius());
            circle.setTranslateX(vertexList.get(j).getX());
            circle.setTranslateY(vertexList.get(j).getY());
            root.getChildren().add(circle);
            System.out.println("Success!");
        }

    }

    private Circle createCircle(final String name, final javafx.scene.paint.Color color, int radius) {
        //create a circle with desired name,  color and radius
        final Circle circle = new Circle(radius, new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[]{
                new Stop(0, javafx.scene.paint.Color.rgb(250, 250, 255)),
                new Stop(1, color)
        }));
        //add a shadow effect
        circle.setEffect(new InnerShadow(7, color.darker().darker()));
        //change a cursor when it is over circle
        circle.setCursor(Cursor.HAND);
        //add a mouse listeners
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                // showOnConsole("Clicked on" + name + ", " + me.getClickCount() + "times");
                //the event will be passed only to the circle which is on front
                me.consume();
            }
        });


//создаем новую линии:основную и две для уголка
        circle.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                //правая кнопка мыши
                if (me.getButton() == MouseButton.SECONDARY) {
                    DeleteC(me);

                    System.out.println("delete");
                } else {
                    final Line line = new Line();
                    final Line line1 = new Line();
                    final Line line2 = new Line();
                    final Double x = me.getSceneX();
                    final Double y = me.getSceneY();
                    line.setStartX(me.getSceneX());
                    line.setStartY(me.getSceneY());
                    line.setEndX(me.getSceneX());
                    line.setEndY(me.getSceneY());
                    line.onDragEnteredProperty();
                    line.setFill(null);
                    line.setStroke(javafx.scene.paint.Color.RED);
                    line.setStrokeWidth(2);
                    final ILine lin = new ILine();

//рисуем линию и уголок в след за курсором
                    circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {

                            line.setEndX(me.getSceneX());
                            line.setEndY(me.getSceneY());
                            CreateRis(me.getSceneX(), me.getSceneY(), line, line1, line2);


                        }
                    });

//создаем стрелку
                    circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {

                            CreateArrow(me, line, lin, line1, line2, x, y);
                        }
                    });         // show the line shape;
                    root.getChildren().add(line1);
                    root.getChildren().add(line2);
                    root.getChildren().add(line);
                }
            }
        });


        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //change the z-coordinate of the circle
                circle.toFront();
                //showOnConsole("Mouse entered " + name);
            }
        });


        circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //showOnConsole("Mouse released above " +name);
            }
        });


        return circle;
    }

    //функция создания стрелки:использую два цикла:1)для проверки,лежит ли конец стрелки в круге, вычисления точки пересечения с кругом.
// 2)вычисление для начала стрелки
    private void CreateArrow(MouseEvent me, Line line, ILine lin, Line line1, Line line2, Double x, Double y) {
        for (Vertex vert : vertexList) {

            if (Math.pow((me.getSceneX() - vert.getX()), 2) + Math.pow((me.getSceneY() - vert.getY()), 2) < Math.pow(vert.getRadius(), 2)) {


                for (Vertex vert1 : vertexList) {
                    if (Math.pow((line.getStartX() - vert1.getX()), 2) + Math.pow((line.getStartY() - vert1.getY()), 2) < Math.pow(vert.getRadius(), 2)) {
                        line.setStartX(vert1.getX());
                        line.setStartY(vert1.getY());
                        lin.setStartVertex(vert1);
                        lineList.add(lin);
                        Double k = Math.atan((vert1.getY() - vert.getY()) / (vert1.getX() - vert.getX()));
                        if (vert1.getX() > vert.getX()) {
                            line.setEndX(vert.getX() + vert.getRadius() * Math.cos(k));
                            line.setEndY(vert.getY() + vert.getRadius() * Math.sin(k));

                        } else {
                            line.setEndX(vert.getX() - vert.getRadius() * Math.cos(k));
                            line.setEndY(vert.getY() - vert.getRadius() * Math.sin(k));


                        }
                        CreateRis(line.getEndX(), line.getEndY(), line, line1, line2);
                        lin.setEndVertex(vert);
                        break;
                    }

                }
                break;
            } else {

                line.setEndX(x);
                line.setEndY(y);
                line1.setStartX(x);
                line1.setEndX(x);
                line1.setStartY(y);
                line1.setEndY(y);
                line2.setStartX(x);
                line2.setEndX(x);
                line2.setStartY(y);
                line2.setEndY(y);
            }

        }
    }

    //рисование самого уголка
    private void CreateRis(Double X, Double Y, Line line, Line _line1, Line _line2) {

        double beta = Math.atan2(Y - line.getStartY(), X - line.getStartX()); //{ArcTan2 ищет арктангенс от x/y что бы неопределенностей не
        //  возникало типа деления на ноль}
        double alfa = Math.PI / 15;// {угол между основной осью стрелки и рисочки в конце}
        int r1 = 20; //{длинна риски}

        int x1 = (int) Math.round(X - r1 * Math.cos(beta + alfa));
        int y1 = (int) Math.round(Y - r1 * Math.sin(beta + alfa));
        int x2 = (int) Math.round(X - r1 * Math.cos(beta - alfa));
        int y2 = (int) Math.round(Y - r1 * Math.sin(beta - alfa));


        _line1.setStartX(X);
        _line1.setStartY(Y);
        _line1.setEndX(x1);
        _line1.setEndY(y1);
        _line1.onDragEnteredProperty();
        _line1.setFill(null);
        _line1.setStroke(javafx.scene.paint.Color.RED);
        _line1.setStrokeWidth(2);


        _line2.setStartX(X);
        _line2.setStartY(Y);
        _line2.setEndX(x2);
        _line2.setEndY(y2);
        _line2.onDragEnteredProperty();
        _line2.setFill(null);
        _line2.setStroke(javafx.scene.paint.Color.RED);
        _line2.setStrokeWidth(2);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
