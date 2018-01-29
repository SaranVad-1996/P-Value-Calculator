import org.apache.commons.math3.distribution.NormalDistribution;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PValueCalculator extends Application{
	
	Label getNullMean = new Label("Enter mean:");
	TextField userInputNullAvg = new TextField("Null hypothesis");
	TextField userInputAltAvg = new TextField("Alternative hypothesis");
	
	Button goBtn = new Button("Go!");
	
	double sum = 0;
	
	
	double mean1;
	double mean2;
	
	double lowerNull;
	double upperNull;
	double lowerAlt;
	double upperAlt;
	
	int n;
	
	double dev1;
	double dev2;
	
	Label getDev = new Label("Enter deviation:");
	TextField userInputNullDev = new TextField("Null hypothesis");
	TextField userInputAltDev = new TextField("Alternative hypothesis");
	
	double pValue;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane gp = new GridPane();
		
		gp.setPadding(new Insets(11.5, 12, 13.5, 14.5));
		gp.setHgap(5.5);
		gp.setVgap(5.5);
		
		gp.add(goBtn, 0, 0);
		gp.add(getNullMean, 0, 1);
		gp.add(userInputNullAvg, 1, 1);
		gp.add(userInputAltAvg, 2, 1);
		gp.add(getDev, 0, 2);
		gp.add(userInputNullDev, 1, 2);
		gp.add(userInputAltDev, 2, 2);
		
		goBtn.setOnAction(e -> showNewStage());
		
		Scene scene = new Scene(gp,450,150);
		primaryStage.setTitle("Lab3-Dashboard");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * The initial window
	 */
	private void showNewStage() {
	    mean1 = Double.parseDouble(userInputNullAvg.getText());
		mean2 = Double.parseDouble(userInputAltAvg.getText());
		
		dev1 = Double.parseDouble(userInputNullDev.getText());
		dev2 = Double.parseDouble(userInputAltDev.getText());
		
		NormalDistribution nDist = new NormalDistribution(mean1, dev1);	
		
	    pValue = nDist.cumulativeProbability(mean2);
	    Stage newStage = new Stage();
		TextField tf = new TextField();
		
		tf.setText("Mean1: " + mean1 + "  Sigma1: " + dev1 +"  Mean2: " + mean2 + " P-Value: " + pValue);
		
		VBox root2 = new VBox(tf, getLineChart());
	    Scene newScene = new Scene(root2, 700,300);
		newStage.setScene(newScene);
		newStage.setTitle("P-Value Calculator");
		newStage.show();
	}
	
	/**
	 * This function shows the visual data determining if a hypothesis is rejected or not
	 * @return lineChart
	 */
	private Node getLineChart() {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        //creating the chart
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
        
		NormalDistribution ndNull = new NormalDistribution(mean1, dev1);
		NormalDistribution ndAlt = new NormalDistribution(mean2, dev2);
        
		lowerNull = ndNull.getMean() - (3* ndNull.getStandardDeviation());
		upperNull = ndNull.getMean() + (3* ndNull.getStandardDeviation());

		lowerAlt = ndAlt.getMean() - (3* ndAlt.getStandardDeviation());
		upperAlt = ndAlt.getMean() + (3* ndAlt.getStandardDeviation());
        
        //defining a series
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        
        series1.setName("Null Hypothesis(Turns Blue if accepted)");
        series2.setName("Alternative Hypothesis(Turns green if Null Hypothesis is rejected)");
        for(double i = lowerNull;	 i < upperNull;	i += (upperNull - lowerNull) / 30 )
		{    series1.getData().add(new XYChart.Data(i, ndNull.density(i)));      }
        
        for(double i = lowerAlt;	 i < upperAlt;	i += (upperAlt - lowerAlt) / 30 )
		{    series2.getData().add(new XYChart.Data(i, ndAlt.density(i)));      }
        lineChart.getData().add(series1);
        lineChart.getData().add(series2);
        
        if(pValue>0.025&&pValue<0.975){Node line = series1.getNode().lookup(".chart-series-line");        
            line.setStyle("-fx-stroke: #0000FFFF;-fx-stroke-width: 10px;");	
        	//Represents the alternative hypothesis
            Node line1 = series2.getNode().lookup(".chart-series-line");        
            line1.setStyle("-fx-stroke: #FF0000B0;-fx-stroke-width: 2px;"); 
        	//Represents the Null hypothesis
        	
        }else{ 
        	Node line = series1.getNode().lookup(".chart-series-line");  
        	line.setStyle("-fx-stroke: #FF00FFB0;-fx-stroke-width: 2px;");


        Node line1 = series2.getNode().lookup(".chart-series-line"); 
        	line1.setStyle("-fx-stroke: #00FF00FF;-fx-stroke-width: 10px;"); 
        }
    		return lineChart;
        
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
