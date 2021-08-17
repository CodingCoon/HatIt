package com.hatit.visual.beige.player;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.CriteriaType;
import com.hatit.data.criteria.Setting;
import com.hatit.data.player.Player;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class PlayerView extends GridPane {
    //_______________________________________________ Parameters
    private static final String ID = "player-view";
    private static final Map<CriteriaType, Function<Criteria, CriteriaViewBuilder<?>>> VIEW_CREATOR = new HashMap<>();
    static {
        VIEW_CREATOR.put(CriteriaType.CONSTRAINED_RATING, ConstrainedRatingViewBuilder::new );
        VIEW_CREATOR.put(CriteriaType.OPEN_RATING       , OpenRatingViewBuilder::new);
        VIEW_CREATOR.put(CriteriaType.TAGGING           , TaggingViewBuilder::new);
    }

    private final ReadOnlyObjectProperty<Player> selectedPlayer;
    private final List<Criteria> criteriaList;

    private final TextField nameField = new TextField("Name");
    private final List<CriteriaViewBuilder<?>> criteriaViewBuilderList = new ArrayList<>();

    private final ChangeListener<Player> selectedPlayerChanged = (observable, oldValue, newValue) -> updateContent(oldValue, newValue);

    //_______________________________________________ Initialize
    PlayerView(ReadOnlyObjectProperty<Player> selectedPlayer, List<Criteria> criteriaList) {
        setId(ID);
        this.selectedPlayer = selectedPlayer;
        this.criteriaList = criteriaList;

        initUI();
        sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
    }

    //_______________________________________________ Methods
    private void initUI() {
        add(StyleUtil.createLabel("Name:"), 0, 0);
        add(nameField, 1, 0);

        int row = 1;
        for (Criteria criteria : criteriaList) {
            CriteriaViewBuilder<?> criteriaViewBuilder = VIEW_CREATOR.get(criteria.getType()).apply(criteria);
            criteriaViewBuilderList.add(criteriaViewBuilder);
            criteriaViewBuilder.addUI(this, row++);
        }
    }

    private void onShow() {
        selectedPlayer.addListener(selectedPlayerChanged);
        disableProperty().bind(selectedPlayer.isNull());
    }

    private void onHide() {
        disableProperty().unbind();
        selectedPlayer.removeListener(selectedPlayerChanged);
        criteriaViewBuilderList.forEach(cvb -> cvb.updatePlayer(selectedPlayer.get(), null));
    }

    private void updateContent(Player oldSelection, Player newSelection) {
        if (oldSelection != null) {
            oldSelection.propName().unbind();
        }

        if (newSelection != null) {
            nameField.setText(newSelection.propName().get());
            newSelection.propName().bind(nameField.textProperty());
        }

        criteriaViewBuilderList.forEach(cvb -> cvb.updatePlayer(oldSelection, newSelection));
    }

    //_______________________________________________ Inner Classes
    static abstract class CriteriaViewBuilder<T extends Setting> {
        private final Criteria criteria;
        private final T setting;

        CriteriaViewBuilder(Criteria criteria, T setting) {
            this.criteria = criteria;
            this.setting = setting;
        }

        Criteria getCriteria() {
            return criteria;
        }

        T getSetting() {
            return setting;
        }

        abstract void addUI(GridPane gridPane, int row);

        final void updatePlayer(Player oldPlayer, Player newPlayer) {
            if (oldPlayer != null) {
                deactivatePlayer(oldPlayer);
            }

            if (newPlayer != null) {
                activatePlayer(newPlayer);
            }
        }

        abstract void deactivatePlayer(Player oldPlayer);

        abstract void activatePlayer(Player newPlayer);
    }

    //_______________________________________________ End
}
