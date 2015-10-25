package ui;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import ui.Tools;
import javafx.animation.Animation.Status;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class PasswordProtector extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	private static final String pseudoPass = "henry";
	
	private static final Image screw = Tools.createImage("screw.png");
	private static final Image textIn = Tools.createImage("OverOn.png");
	private static final Image textInPressed = Tools.createImage("ClickOn.png");
	private static final Image textNone = Tools.createImage("Off.png");
	private static final Image shinyBack = Tools.createImage("background.jpg");
	private static final Image background = Tools.createImage("tile.png");
	
	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	private final double dispWidth = displayRes.getWidth();
	private final double dispHeight = displayRes.getHeight();
	
	private final double wRatio = dispWidth / 800.0;
	private final double hRatio = dispHeight / 450.0;
	public double sRatio;
	
	private PasswordField passwordField;
	private ImageView button;
	private Text info;
	



	public PasswordProtector() {
		sRatio = checkSmallRatio();
		
	}

	public double checkSmallRatio() {
		return (wRatio > hRatio) ? hRatio : wRatio;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		Scene trans = new Scene(new AnchorPane(), dispWidth, dispHeight);
		trans.setFill(null);
		primaryStage.setScene(trans);
		primaryStage.show();
		primaryStage.setOpacity(0.0);
		
		Stage s = initIntro();
		s.initOwner(primaryStage);
		s.show();
	}


	private Stage initIntro() {
		
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		AnchorPane root = new AnchorPane();
		Scene scene = new Scene(root, 400 * wRatio, 160 * hRatio);

		InnerShadow smallShade = new InnerShadow(2.0, Color.BLACK);
		InnerShadow largeShade = new InnerShadow(5.0, Color.BLACK);
		DropShadow out = new DropShadow(2.0, Color.BLACK);
		DropShadow out2 = new DropShadow(3.0, Color.rgb(40,100,120));
		
		ImageView screw1 = Tools.createImageView(screw, 15, 15, 3, 0.0, sRatio, wRatio, hRatio, out);
		ImageView screw2 = Tools.createImageView(screw, 15, 15, 385, 0.0, sRatio, wRatio, hRatio, out);
		ImageView screw3 = Tools.createImageView(screw, 15, 15, 3, 145, sRatio, wRatio, hRatio, out);
		ImageView screw4 = Tools.createImageView(screw, 15, 15, 385, 145, sRatio, wRatio, hRatio, out);

		passwordField = new PasswordField();
		
		passwordField.setLayoutX(60*wRatio);
		passwordField.setLayoutY(110*hRatio);
		passwordField.setPrefSize(200*wRatio, 15*hRatio);
		passwordField.setFont(Tools.createBoldFont(12, sRatio));

		button = Tools.createImageView(textNone, 60, 60, 280, 85, sRatio, wRatio, hRatio, out);
		button.setOnMousePressed(e->
		{
			if(passwordField.getText().isEmpty())
			{
				return;
			}
			button.setImage(textInPressed);
		}
		);
		button.setOnMouseReleased(e->
		{
			if(passwordField.getText().isEmpty())
			{
				return;
			}
			button.setImage(textIn);
			process();
		});
		
		Text name = Tools.createText(50, 15, wRatio, hRatio, "Password Protector",Color.rgb(80,215,240), smallShade, Tools.createBoldFont(26, sRatio));
		Text instruction = Tools.createText(50, 85, wRatio, hRatio, "Enter Your Password:", Color.rgb(80,215,240), smallShade, Tools.createRegularFont(18, sRatio));
		info = Tools.createText(25, 55.0, wRatio, hRatio, "One Password to Save Them All! ", Color.rgb(200,220,230), null, Tools.createRegularFont(13, sRatio));
		info.setWrappingWidth(350*wRatio);
		info.setTextAlignment(TextAlignment.CENTER);
		
		BackgroundImage back = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(scene.getWidth(), scene.getHeight(), false, false, false, false));
		BackgroundImage back2 = new BackgroundImage(shinyBack, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(scene.getWidth(), scene.getHeight(), false, false, false, false));
		
		passwordField.textProperty().addListener(e->
		{
			if(!passwordField.getText().isEmpty())
			{
				button.setImage(textIn);
			}
			else
			{
				button.setImage(textNone);
			}
		});
		passwordField.setOnKeyPressed(e-> 
		{
			if(passwordField.getText().isEmpty())
			{
				return;
			}
			if(e.getCode() == KeyCode.ENTER)
			{
				button.setImage(textInPressed);
			}
		});
		
		passwordField.setOnKeyReleased(e-> 
		{
			if(passwordField.getText().isEmpty())
			{
				return;
			}
			if(e.getCode() == KeyCode.ENTER)
			{
				button.setImage(textIn);
				process();
			}
		});
		passwordField.setBackground(new Background(back2));
		passwordField.setEffect(smallShade);
		
		Rectangle panel = Tools.createRoundedRectangle(350, 25, 5, 5, 25, 50, sRatio, wRatio, hRatio, Color.DARKGRAY.darker().darker(), largeShade);
		
		root.getChildren().addAll(screw1, screw2, screw3, screw4, panel,name, instruction,passwordField,button,info);
		root.setBackground(new Background(back));
		root.setEffect(largeShade);
		primaryStage.setScene(scene);

		return primaryStage;

	}
	public void process()
	{
		if(passwordField.getText().equals(pseudoPass))
		{
			info.setText("Valid Password!");
			passwordField.setText("");
		}
		else
		{
			info.setText("Invalid Password! Please try again.");
			passwordField.setText("");
		}
		button.setImage(textNone);
	}
}