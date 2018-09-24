import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import Ex2.Band;
import controller.BandsDataControllerImpl;
import controller.comparators.FansComparator;
import controller.comparators.NameComparator;
import controller.comparators.OriginComparator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class FramePain extends BorderPane {

    private BandsDataControllerImpl bandsController = new BandsDataControllerImpl();

	// ComboBox
	private final ComboBox sortBox = new ComboBox();

	// buttons
	private final Button rightButton = new Button(">");
	private final Button leftButton = new Button("<");
	private final Button saveButton = new Button("Save");
	private final Button removeButton = new Button("Remove Band");
	private final Button undoButton = new Button("Undo");
	private final Button revertButton = new Button("Revert");

	// panes
    BandInfoPane bandInfoPane = new BandInfoPane();
	HBox hBox = new HBox();
	VBox vBox = new VBox();

	// animation
	private final Line line = new Line();
	private final Label timeLabel = new Label();

	private final int SIZE = 50;
	private final String BackgroundColor = "-fx-background-color: #000000";

	public FramePain() {
        this.setFocusTraversable(true);
        this.requestFocus();
		setCenter(bandInfoPane);
		setBottom(hBox);
		setTop(vBox);

		sortBox.setPromptText("Sort by...");
		
		sortBox.getItems().add("Sort By Name");
		sortBox.getItems().add("Sort By Fans");
		sortBox.getItems().add("Sort By Origin");

		
		vBox.getChildren().add(timeLabel);
 		vBox.getChildren().addAll(sortBox);

		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(SIZE / 6);

 		// line definition
		line.setStartX(-8 * SIZE);
		line.setEndX(SIZE * 10);
		line.setStrokeWidth(20);

		line.setFill(Color.BLUE);

		setRight(rightButton);
		setLeft(leftButton);

		hBox.getChildren().setAll(saveButton, removeButton, undoButton, revertButton);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(SIZE / 1.5);

		// Alignment
		setAlignment(rightButton, Pos.CENTER);
		setAlignment(leftButton, Pos.CENTER);

		setStyle(BackgroundColor);

		setPadding(new Insets(SIZE));

		timeLabel.setTextFill(Color.RED);

		
		//set dynamic date and time on label
		final Timeline digitalTime = new Timeline(
				new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = new Date();
						timeLabel.setText(dateFormat.format(date) + " METAL HALL OF FAME");
					}
				}), new KeyFrame(Duration.seconds(0.3)));
		digitalTime.setCycleCount(Animation.INDEFINITE);
		digitalTime.play();

		//label PathTransition
		PathTransition ptr = new PathTransition();
		ptr.setDuration(Duration.seconds(5));
		ptr.setPath(line);
		ptr.setNode(timeLabel);
		ptr.setCycleCount(Timeline.INDEFINITE);
		ptr.setAutoReverse(true);
		ptr.play();

		//stop and play the transition of label in mouse enter/exit event
		vBox.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
			ptr.pause();
		});

		vBox.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
			ptr.play();
		});
		sortBox.setOnAction(e ->{
            Comparator<Band> comparator = null;
		    if(sortBox.getValue().toString().contains("Name"))
                comparator = new NameComparator();
		    if(sortBox.getValue().toString().contains("Fans"))
                comparator = new FansComparator();
		    if(sortBox.getValue().toString().contains("Origin"))
                comparator = new OriginComparator();

		    bandsController.sort(comparator);
		    bandsController.goToStart();
		    bandInfoPane.getBandInfo();
                });


            rightButton.setOnAction(e -> {
            bandsController.next();
            bandInfoPane.getBandInfo();
        });

        leftButton.setOnAction(e -> {
            bandsController.previous();
            bandInfoPane.getBandInfo();
        });

        removeButton.setOnAction(e -> {
            bandsController.remove();
            bandsController.next();
            bandInfoPane.getBandInfo();
        });

        undoButton.setOnAction(e -> {
            bandsController.undo();
            bandInfoPane.getBandInfo();
        });

        revertButton.setOnAction(e -> {
            bandsController.revert();
            bandInfoPane.getBandInfo();
        });

        saveButton.setOnAction(e -> {
            try {
                bandsController.save();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            bandInfoPane.getBandInfo();
        });


        this.setOnKeyPressed(e ->
        {
            this.setFocusTraversable(true);
            this.requestFocus();
            switch (e.getCode()) {
                case RIGHT:
                    bandsController.next();
                    bandInfoPane.getBandInfo();
                    break;
                case LEFT:
                    bandsController.previous();
                    bandInfoPane.getBandInfo();
                case S:
                    try {
                        bandsController.save();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    bandInfoPane.getBandInfo();
                    break;
                case Z:
                    bandsController.undo();
                    bandInfoPane.getBandInfo();
                    break;
            }
        });

	}

    class BandInfoPane extends GridPane {

        private final int SIZE = 50;
        private final String BACKGROUND_COLOR = "-fx-background-color: #000000";
        private final Color RED_COLOR = Color.RED;

        // Text Fields
        private final TextField nameText = new TextField();
        private final TextField fansText = new TextField();
        private final TextField yearText = new TextField();
        private final TextField originText = new TextField();
        private final TextField styleText = new TextField();

        // Labels
        private final Label nameLabel = new Label("Band:");
        private final Label fansLabel = new Label("Fans:");
        private final Label yearLabel = new Label("Formed:");
        private final Label originLabel = new Label("Origin:");
        private final Label styleLabel = new Label("style:");
        private final Label splitLabel = new Label("Did They Split");

        // CheckBox
        private final CheckBox splitCBox = new CheckBox();

        public BandInfoPane() {

            setupSpacing();
            setPadding(new Insets(SIZE));

            nameText.setEditable(false);
            fansText.setEditable(false);
            yearText.setEditable(false);
            originText.setEditable(false);
            styleText.setEditable(false);
            splitCBox.setDisable(true);

            getBandInfo();

            nameLabel.setTextFill(RED_COLOR);
            fansLabel.setTextFill(RED_COLOR);
            yearLabel.setTextFill(RED_COLOR);
            originLabel.setTextFill(RED_COLOR);
            styleLabel.setTextFill(RED_COLOR);
            splitLabel.setTextFill(RED_COLOR);

            addRow(0, nameLabel, nameText);
            addRow(1, fansLabel, fansText);
            addRow(2, yearLabel, yearText);
            addRow(3, originLabel, originText);
            addRow(4, splitLabel, splitCBox);

            addRow(5, styleLabel, styleText);
            setStyle(BACKGROUND_COLOR);

        }

        private void setupSpacing() {
            setAlignment(Pos.CENTER);
            setVgap(SIZE / 2);
            setHgap(SIZE);

        }

        public void getBandInfo(){

            nameText.setText(bandsController.getCurrentBand().getName());
            fansText.setText(String.valueOf(bandsController.getCurrentBand().getNumOfFans()));
            yearText.setText(String.valueOf(bandsController.getCurrentBand().getFormedYear()));
            originText.setText(bandsController.getCurrentBand().getOrigin());
            styleText.setText(bandsController.getCurrentBand().getStyle());
            splitCBox.setSelected(bandsController.getCurrentBand().hasSplit());
        }

    }

}
