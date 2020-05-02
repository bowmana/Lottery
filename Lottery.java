/*Alex Bowman
 * HW#10 
 * Professor Silvestri
 * 5/1/2020
 * 
 */
package lotto;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Lottery extends Application {
	private TextArea ta;
	private PMLPane pml;

	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(new AppGUI(), 900, 600);
		scene.getStylesheets().add("appstyle.css");
		primaryStage.setTitle("Lottery Quick Picks");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public class AppGUI extends FlowPane {
		public AppGUI() {

			this.setVgap(20);
			this.setHgap(20);
			this.setPadding(new Insets(60, 20, 40, 20));
			this.setAlignment(Pos.CENTER);
			pml = new PMLPane();
			ComboBox<String> cbo = new ComboBox<>();
			for (int i = 1; i <= 10; i++) {
				if (i == 1 || i == 2 || i == 5 || i == 10) {
					cbo.getItems().add(i + "");
				}
			}
			cbo.setValue("5");
			cbo.setPrefWidth(80);

			ta = new TextArea();

			ta.setPrefSize(800, 400);
			ta.setWrapText(true);
			ta.setEditable(false);
			ta.getStyleClass().add("text-color");
			Text txt = new Text("Number of Quick Picks");
			Label lbl = new Label("Lottery Quick-Pick System (Alex Bowman)");
			lbl.setAlignment(Pos.CENTER);
			lbl.setPrefWidth(900);
			lbl.setPrefHeight(40);

			Button button = new Button("Generate");

			button.setPrefWidth(200);
			button.setOnAction(e -> {
				ta.appendText(cbo.getValue() + " ");
				ta.appendText("-" + pml.getSelectedText() + "\n");
				pml.sendInfoToServer();

			});
			this.getChildren().addAll(txt, cbo, pml, button, ta, lbl);

		}
	}

	public class PMLPane extends VBox {
		private RadioButton pwrBall;
		private RadioButton megaMill;
		private RadioButton lFL;

		public PMLPane() {

			this.setPrefSize(200, 100);
			this.setSpacing(10);
			this.setAlignment(Pos.CENTER_LEFT);

			pwrBall = new RadioButton("Power Ball");
			pwrBall.getStyleClass().add("text-color");
			pwrBall.setSelected(true);
			megaMill = new RadioButton("MegaMillions");
			megaMill.getStyleClass().add("text-color");
			lFL = new RadioButton("Lucky For Life");
			lFL.getStyleClass().add("text-color");

			ToggleGroup group = new ToggleGroup();
			pwrBall.setToggleGroup(group);
			megaMill.setToggleGroup(group);
			lFL.setToggleGroup(group);

			this.getChildren().addAll(pwrBall, megaMill, lFL);

		}

		public String getSelectedText() {
			if (pwrBall.isSelected())
				return pwrBall.getText();
			if (megaMill.isSelected())
				return megaMill.getText();
			if (lFL.isSelected())
				return lFL.getText();
			return null;
		}

		public String enteredQuickPicks() {
			int numOfQp;
			try {
				numOfQp = Integer.parseInt(ta.getText());
			} catch (Exception ex) {
				numOfQp = -1;
			}

			String Qp = "";
			Qp += numOfQp;
			return Qp;
		}

		public void pStr(String p) {
			System.out.println(p);
		}

		public void sendInfoToServer() {

			Socket client = null;
			PrintWriter output = null;
			Scanner input = null;

			try (Scanner sc = new Scanner(System.in);) {
				pStr("Creating Client Socket ");
				client = new Socket("cs.stcc.edu", 5000);
				pStr("SUCCESS!!!");

				input = new Scanner(client.getInputStream());
				output = new PrintWriter(client.getOutputStream());

				output.println(enteredQuickPicks());
				output.flush();

				output.println(getSelectedText());
				output.flush();
				String ansLines = input.nextLine();

				System.out.println(ansLines);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					input.close();
				} catch (Exception e) {
				}
				try {
					output.close();
				} catch (Exception e) {
				}
				try {
					client.close();
				} catch (Exception e) {
				}
			}

		}
	}
}