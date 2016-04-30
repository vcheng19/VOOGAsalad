package auth_environment.view.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import auth_environment.Models.Interfaces.IAuthModel;
import auth_environment.delegatesAndFactories.NodeFactory;
import auth_environment.view.tabs.StoreTab.NameAffectorCostSet;
import game_engine.affectors.Affector;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditUpgradeWindow extends Observable {
	private BorderPane mainPane = new BorderPane();
	private GridPane upgradePane = new GridPane();
	private IAuthModel authModel;
	private NodeFactory nodeFactory = new NodeFactory();
	private int rowIndex = 0;
	private List<ComboBox<String>> comboBoxes = new ArrayList<>();
	private List<TextField> textFields = new ArrayList<>();
	private NameAffectorCostSet NACS;
	private Stage stage;

	public EditUpgradeWindow(IAuthModel iAuthModel, StoreTab.NameAffectorCostSet nacs) {
		NACS = nacs;
		authModel = iAuthModel;
		stage = new Stage();
		Group root = new Group();
		mainPane.setCenter(upgradePane);
		root.getChildren().add(mainPane);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Edit Upgrades");
		stage.show();

		Button addNew = new Button("Add new upgrade");
		Button ok = new Button("Ok");
		ok.setOnAction(e -> saveAndClose());
		addNew.setOnAction(e -> addUpgradeSlot());
		
		BorderPane buttons = new BorderPane();
		buttons.setTop(addNew);
		buttons.setBottom(ok);
		mainPane.setBottom(buttons);

		addUpgradeSlot();
	}

	private void addUpgradeSlot() {
		ComboBox<String> combo = new ComboBox<>();
		List<String> affectorNames = new ArrayList<String>();
		
		for (Affector a : authModel.getIAuthEnvironment().getAffectors())
			affectorNames.add(a.getClass().toString());

		combo.getItems().addAll(affectorNames);
		TextField cost = nodeFactory.buildTextFieldWithPrompt("Upgrade cost");

		upgradePane.addRow(rowIndex++, combo, cost);
		
		comboBoxes.add(combo);
		textFields.add(cost);
	}

	private void saveAndClose() {
		for(int i = 0; i < comboBoxes.size(); i++)
		{
			if(comboBoxes.get(i).getValue() == null || textFields.get(i).getText().length() == 0)
				continue;
			
			String affectorName = comboBoxes.get(i).getValue();
			int upgradeCost = Integer.parseInt(textFields.get(i).getText());
			
			NACS.affectorCost.add(upgradeCost);
			NACS.affectorNames.add(affectorName);
		}
		stage.close();
	}
}