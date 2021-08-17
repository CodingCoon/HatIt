package com.hatit.visual.blue;

import com.hatit.data.tournament.Tournament;
import com.hatit.visual.Enviroment;
import com.hatit.visual.ResourceUtil;
import com.hatit.visual.StyleUtil;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ExplorerView extends VBox {
    //_______________________________________________ Parameters
    private final Enviroment enviroment;

    //_______________________________________________ Initialize
    public ExplorerView(Enviroment enviroment) {
        this.enviroment = enviroment;
        setSpacing(12);

        initUI();
    }

    //_______________________________________________ Methods
    private void initUI() {
        getChildren().add(new ImageButton(enviroment));
        for (Tournament tournament : enviroment.getAllTournaments()) {
            TournamentBox box = new TournamentBox(enviroment, tournament);
            getChildren().add(box);
        }
    }

    //_______________________________________________ Inner Classes
    private static final class ImageButton extends StackPane {
        private ImageButton(Enviroment enviroment) {
            getStyleClass().add("add-button");
            getChildren().add(new ImageView(ResourceUtil.ADD_ITEM));
            setOnMouseClicked(event -> enviroment.addTournament(Tournament.of()));
        }
    }

    private static final class TournamentBox extends VBox {

        private TournamentBox(Enviroment enviroment, Tournament tournament) {
            getStyleClass().add("item-view");
            Label nameLabel = StyleUtil.h1(tournament.propName().get());
            Label criteriaLabel = StyleUtil.h3(tournament.propCriteria().size() + " Kriterien");
            Label playerLabel = StyleUtil.h3(tournament.propPlayers().size() + " Spieler");
            Label preferencesLabel = StyleUtil.h3(tournament.getPreferences().propTeamCount().get() + " Teams");

            getChildren().addAll(nameLabel, criteriaLabel, playerLabel, preferencesLabel);

            setOnMousePressed(event -> pseudoClassStateChanged(PseudoClass.getPseudoClass("armed"), true));
            setOnMouseReleased(event -> pseudoClassStateChanged(PseudoClass.getPseudoClass("armed"), false));
            setOnMouseClicked(event -> enviroment.propCurrentTournament().set(tournament));
        }
    }

    //_______________________________________________ End
}
