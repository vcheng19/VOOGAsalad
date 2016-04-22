package auth_environment.view.tabs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import auth_environment.IAuthEnvironment;
import auth_environment.Models.UnitView;
import auth_environment.view.UnitPicker;
import game_engine.TestingEngineWorkspace;
import game_engine.factories.UnitFactory;
import game_engine.game_elements.Unit;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ElementTab extends Tab{
	 
	private IAuthEnvironment myInterface;
	private Map<String, TextField> strTextMap = new HashMap<String, TextField>();
	private UnitFactory myUnitFactory  = new UnitFactory();
	
	public ElementTab(String name, IAuthEnvironment myInterface){
		super(name);
		this.myInterface = myInterface;
		init();
	}
	
	private void init(){
		BorderPane myPane = new BorderPane();
		TitledPane newPane = new TitledPane();
//		TitledPane editPane = new TitledPane();
		ScrollPane newScrollPane = new ScrollPane();
//		ScrollPane editScrollPane = new ScrollPane();
		BorderPane newBorderPane = new BorderPane();
		GridPane newAnimationInfo = new GridPane();					//*****	
//		FlowPane editInfo = new FlowPane();							//*****	
		myPane.setLeft(newPane);
		
		TestingEngineWorkspace test = new TestingEngineWorkspace();
		test.setUpEngine(1.0);
		Unit tower = test.getTerrains().get(0); 
		UnitView uv = new UnitView(tower, "smackCat.gif"); 
	    UnitPicker up = new UnitPicker(test.getTerrains());
	    
	    myPane.setRight(up.getRoot());
		
		newPane.setText("New");
		newPane.setContent(newScrollPane);
		newPane.setPrefSize(700.0, 800.0);
		newPane.setCollapsible(false);
		newScrollPane.setContent(newBorderPane);
		newBorderPane.setTop(newAnimationInfo);
		newScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		Button animationButton = new Button("ANIMATION");
		animationButton.setOnAction( e -> System.out.println("ANIMATION"));
		animationButton.setPrefSize(400.0,70.0);

		newAnimationInfo.getColumnConstraints().addAll(new ColumnConstraints(250), new ColumnConstraints(200), new ColumnConstraints(200));
		newAnimationInfo.getRowConstraints().addAll(new RowConstraints(70));
		newAnimationInfo.add(animationButton, 1, 0); //col, row
		
        GridPane newTableInfo = new GridPane();
        newTableInfo.getColumnConstraints().addAll(new ColumnConstraints(175),new ColumnConstraints(150),new ColumnConstraints(200),new ColumnConstraints(100) );
        newTableInfo.getRowConstraints().addAll(new RowConstraints(20));
        newTableInfo.setPrefSize(600, 200);
		newBorderPane.setLeft(newTableInfo);
		GridPane bottomInfo = new GridPane();
		bottomInfo.getColumnConstraints().addAll(new ColumnConstraints(600), new ColumnConstraints(70));
		Button ok = new Button("OK");
		ok.setOnAction(e -> createNewUnit());
		bottomInfo.add(ok, 1, 0);
		newBorderPane.setBottom(bottomInfo);
        
        
        Text propertiesTitle = new Text("Properties");
        propertiesTitle.setFont(new Font(20));
        newTableInfo.add(propertiesTitle, 0, 0);
        
        addTextFields(newTableInfo);
        
        //labels stuff
//      int index = 1;
//		newTableInfo.getRowConstraints().add(new RowConstraints(30));
//		String name = "Name";
//		newTableInfo.add(new Text(name), 1, index);
//		TextField myTextField = new TextField();
//		newTableInfo.add(myTextField, 2, index);
//		strTextMap.put(name, myTextField);
//		index++;
//		
//		newTableInfo.getRowConstraints().add(new RowConstraints(30));
//		String damage = "Attack";
//		newTableInfo.add(new Text(damage+":"), 1, index);
//		myTextField = new TextField();
//		newTableInfo.add(myTextField, 2, index);
//		index++;
//		
//		newTableInfo.getRowConstraints().add(new RowConstraints(30));
//		String affectors = "Affector(s) ";
//		newTableInfo.add(new Text(affectors), 1, index);
//		ComboBox<String> cbox = new ComboBox<String>();
//		cbox.getItems().addAll("ConstantHealthDamage", "ExpIncrHealthDamage", "HealthDamage", "HomingMove", "PathFollowPositionMove", "RandomPoisonHealthDamage", "StateChange");
//		newTableInfo.add(cbox, 2, index);
//		index++;
		//labels stuff
		
//		Button newAffectorButton = new Button("+ Add New Affector");
//		int num = index;
//		newAffectorButton.setOnAction(e-> addNewAffectorSpace(num, newTableInfo, newAffectorButton, cbox));
//		newTableInfo.add(newAffectorButton, 2, index);
		
        
        
//		editPane.setPrefSize(200.0, 800.0);
//		editScrollPane.setContent(editInfo);
//		editScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
//		editScrollPane.setFitToWidth(true);
//		
//		editPane.setText("Edit");
//		editPane.setContent(editScrollPane);
//		editPane.setCollapsible(false);
//		List<Unit> myList = myInterface.getTowers();
//		for(Unit unit: myList){
//			editInfo.getChildren().addAll(new ImageView(new Image(unit.toString())));
//		}
		this.setContent(myPane);
	}

	private void addTextFields(GridPane newTableInfo) {
		List<String> myFields = myUnitFactory.getFields();
		int index = 1;
		for(String s: myFields){
			newTableInfo.getRowConstraints().add(new RowConstraints(30));
			newTableInfo.add(new Text(s), 1, index);
			TextField myTextField = new TextField();
			newTableInfo.add(myTextField, 2, index);
			strTextMap.put(s, myTextField);
			index++;
		}
		
		
		//reminder to v: refactor this part ^ v
		myFields = myUnitFactory.getFields();
		for(String s: myFields){
			newTableInfo.getRowConstraints().add(new RowConstraints(30));
			newTableInfo.add(new Text(s), 1, index);
			TextField myTextField = new TextField();
			newTableInfo.add(myTextField, 2, index);
			strTextMap.put(s, myTextField);
			index++;
		}
		
		
	}

	private void createNewUnit() {
		
		//change to Mapvv
		HashMap<String, String> strToStrMap = new HashMap<String, String>();
		String name;
    	for(String str: strTextMap.keySet()){
    			strToStrMap.put(str, strTextMap.get(str).getText());
    	}
    	
    	myUnitFactory.createUnit(strToStrMap);
	}

	private void addNewAffectorSpace(int index, GridPane newTableInfo, Button AffectorButton, ComboBox cbox) {
		if(cbox.getValue() != null){
			newTableInfo.getChildren().remove(AffectorButton);
			ComboBox<String> newcbox = new ComboBox<String>();
			newcbox.getItems().addAll("ConstantHealthDamage", "ExpIncrHealthDamage", "HealthDamage", "HomingMove", "PathFollowPositionMove", "RandomPoisonHealthDamage", "StateChange");
			newTableInfo.add(newcbox, 2, index);
			index++;
			Button newAffectorButton = new Button("+ Add New Affector");
			int num = index;
			newAffectorButton.setOnAction(e-> addNewAffectorSpace(num, newTableInfo, newAffectorButton, newcbox));
			newTableInfo.add(newAffectorButton, 2, index);	
		}
	}
	

	
}