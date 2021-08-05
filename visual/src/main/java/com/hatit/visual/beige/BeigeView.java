package com.hatit.visual.beige;

import com.hatit.data.tournament.Tournament;
import com.hatit.data.tournament.TournamentState;
import com.hatit.visual.Enviroment;
import com.hatit.visual.ResourceUtil;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.beige.criteria.EditCriteriaView;
import com.hatit.visual.beige.player.EditPlayerView;
import com.hatit.visual.beige.preferences.PreferencesOverview;
import com.hatit.visual.beige.teams.TeamOverview;
import com.hatit.visual.beige.tournament.EditTournamentView;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BeigeView extends VBox {
    //_______________________________________________ Parameters
    private static final String ID = "beige-view";

    private static final Map<TournamentState, Function<Tournament, Node>> UI_CREATION = new HashMap<>();
    static {
        UI_CREATION.put(TournamentState.TOURNAMENT  , EditTournamentView::new); // TODO: all to ...Overview
        UI_CREATION.put(TournamentState.CRITERIA    , EditCriteriaView::new);
        UI_CREATION.put(TournamentState.PLAYER      , EditPlayerView::new);
        UI_CREATION.put(TournamentState.PREFERENCES , PreferencesOverview::new);
        UI_CREATION.put(TournamentState.TEAM        , TeamOverview::new);
    }

    private final Enviroment enviroment;

    private final StackPane contentView = new StackPane();

    private final ChangeListener<TournamentState> stateChangedListener = (observable, oldValue, newValue) -> updateContent(newValue);

    //_______________________________________________ Initialize
    public BeigeView(Enviroment enviroment) {
        setId(ID);
        this.enviroment = enviroment;

        initUI();

        enviroment.propCurrentTournament().addListener((observable, oldValue, newValue) -> selectionChanged(oldValue, newValue));
        selectionChanged(null, enviroment.propCurrentTournament().get());
    }

    //_______________________________________________ Methods
    private void initUI() {
        Button nextButton = StyleUtil.createImageButton(ResourceUtil.NEXT, "Weiter", e -> enviroment.propCurrentTournament().get().nextState());
        nextButton.visibleProperty().bind(new NextButtonVisibility(enviroment.propCurrentTournament()));

        getChildren().addAll(contentView, nextButton);
        setVgrow(contentView, Priority.ALWAYS);
    }

    private  void selectionChanged(Tournament oldSelection, Tournament newSelection) {
        if (oldSelection != null) {
            contentView.getChildren().clear();
            oldSelection.getState().removeListener(stateChangedListener);
        }

        if (newSelection != null) {
            newSelection.getState().addListener(stateChangedListener);
            updateContent(newSelection.getState().get());
        }
    }

    private void updateContent(TournamentState state) {
        contentView.getChildren().clear();
        Tournament currentTournament = enviroment.propCurrentTournament().get();
        Function<Tournament, Node> tournamentNodeFunction = UI_CREATION.get(state);
        Node content = tournamentNodeFunction.apply(currentTournament);
        contentView.getChildren().add(content);
    }

    //_______________________________________________ Inner Classes
    private static class NextButtonVisibility extends BooleanBinding implements ChangeListener<Tournament> {
        private final ObjectProperty<Tournament> currentTournament;
        private final InvalidationListener stateChangedListener =  observable -> invalidate();

        public NextButtonVisibility(ObjectProperty<Tournament> currentTournament) {
            this.currentTournament = currentTournament;
            currentTournament.addListener(this);
            changed(currentTournament, null, currentTournament.get());
        }

        @Override
        public void dispose() {
            super.dispose();
        }

        @Override
        protected boolean computeValue() {
            Tournament tournament = currentTournament.get();
            return tournament != null && tournament.getState().get().hasNext();
        }

        @Override
        public void changed(ObservableValue<? extends Tournament> observable, Tournament oldValue, Tournament newValue) {
            invalidate();
            if (oldValue != null) {
                oldValue.getState().removeListener(stateChangedListener);
            }
            if (newValue != null) {
                newValue.getState().addListener(stateChangedListener);
            }
        }
    }

    //_______________________________________________ End
}
