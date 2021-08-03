package com.hatit.visual.beige.player;

import com.hatit.data.criteria.*;
import com.hatit.data.player.Player;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PlayerView extends VBox {
    //_______________________________________________ Parameters

    private final ReadOnlyObjectProperty<Player> currentPlayer;

    private final TextField nameField = new TextField("Name");
    private final StatsView statsView;

    private final ChangeListener<Player> selectedPlayerChanged = (observable, oldValue, newValue) -> updateContent(oldValue, newValue);

    //_______________________________________________ Initialize
    public PlayerView(ReadOnlyObjectProperty<Player> currentPlayer, List<Criteria> criteriaList) {
        this.currentPlayer = currentPlayer;

        this.statsView = new StatsView(criteriaList);
        setSpacing(12);
        getChildren().addAll(StyleUtil.h1("Name:"), nameField, statsView);

        sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
    }

    //_______________________________________________ Methods
    private void onShow() {
        currentPlayer.addListener(selectedPlayerChanged);
    }

    private void onHide() {
        currentPlayer.removeListener(selectedPlayerChanged);
    }

    private void updateContent(Player oldSelection, Player newSelection) {
        if (oldSelection != null) {
            oldSelection.propName().unbind();
        }

        if (newSelection != null) {
            nameField.setText(newSelection.propName().get());

            newSelection.propName().bind(nameField.textProperty());
            statsView.update(oldSelection, newSelection);
        }
    }

    //_______________________________________________ Inner Classes
    private static final class StatsView extends VBox {
        private static final Map<CriteriaType, Function<Criteria, CriteriaView<?>>> VIEW_CREATOR = new HashMap<>();
        static {
            VIEW_CREATOR.put(CriteriaType.QUALITATIVE, QualitativeCriteriaView::new );
            VIEW_CREATOR.put(CriteriaType.QUANTITATIVE, QuantitativeCriteriaView::new);
            VIEW_CREATOR.put(CriteriaType.TAGGING, TaggingCriteriaView::new);
        }

        private final Map<Criteria, CriteriaView<?>> criteriaViewMap = new HashMap<>();

        public StatsView(List<Criteria> criteriaList) {
            setSpacing(12);
            for (Criteria criteria : criteriaList) {
                CriteriaView<?> criteriaView = VIEW_CREATOR.get(criteria.propType().get()).apply(criteria);
                criteriaViewMap.put(criteria, criteriaView);
                getChildren().add(criteriaView);
            }
        }

        private void update(Player oldSelectedPlayer, Player newSelectedPlayer) {
            for (CriteriaView<?> criteriaView : criteriaViewMap.values()) {

                if (oldSelectedPlayer != null) {
                    criteriaView.uninstall(oldSelectedPlayer);
                }
                if (newSelectedPlayer != null) {
                    criteriaView.install(newSelectedPlayer);
                }
            }
        }
    }

    private static abstract class CriteriaView<T extends Setting> extends VBox {

        final Criteria criteria;
        final T setting;

        CriteriaView(Criteria criteria, T setting) {
            this.criteria = criteria;
            this.setting = setting;

            setSpacing(12);

            getChildren().add(StyleUtil.h1(criteria.propName().get()));
        }

        @SuppressWarnings("unchecked")
        <V> ObjectProperty<V> getPlayerValueProperty(Player player) {
            return (ObjectProperty<V>) player.propStats().computeIfAbsent(criteria.getID(), uuid -> new SimpleObjectProperty<>(setting.getDefaultValue()));
        }

        abstract void install(Player player);

        void uninstall(Player player) {
            getPlayerValueProperty(player).unbind();
        }

    }

    private static class TaggingCriteriaView extends CriteriaView<TaggingSetting> {
        private final ToggleGroup group;

        private final ObjectProperty<String> selectedOption = new SimpleObjectProperty<>();
        private final List<RadioButton> radioButtons = new ArrayList<>();

        private TaggingCriteriaView(Criteria criteria) {
            super(criteria, (TaggingSetting) criteria.propSettings().get());
            this.group = new ToggleGroup();

            for (TaggingSetting.Option option : setting.propOptions()) {
                RadioButton button = new RadioButton(option.getOption());
                button.setToggleGroup(group);
                button.setUserData(option.getOption());
                radioButtons.add(button);
                getChildren().add(button);
            }
            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> selectedOption.set((String) newValue.getUserData()));
        }

        @Override
        void install(Player player) {
            ObjectProperty<String> playerValueProperty = getPlayerValueProperty(player);
            for (RadioButton radioButton : radioButtons) {
                if (radioButton.getText().equals(playerValueProperty.get())) {
                    group.selectToggle(radioButton);
                }
            }

            playerValueProperty.bind(selectedOption);
        }
    }

    private static class QualitativeCriteriaView extends CriteriaView<QualitativeSetting> {
        private final Slider slider;

        private QualitativeCriteriaView(Criteria criteria) {
            super(criteria, (QualitativeSetting) criteria.propSettings().get());

            QualitativeSetting.Range range = setting.propRange().get();
            this.slider = new Slider(range.getMin(), range.getMax(), range.getMin());
            slider.setBlockIncrement(1);
            slider.setSnapToTicks(true);
            slider.setShowTickLabels(true);
            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(0);

            getChildren().add(slider);
        }

        @Override
        void install(Player player) {
            ObjectProperty<Number> playerValueProperty = getPlayerValueProperty(player);
            slider.valueProperty().setValue(playerValueProperty.get());

            playerValueProperty.bind(slider.valueProperty().asObject());
        }
    }

    private static class QuantitativeCriteriaView extends CriteriaView<QuantitativeSetting> {
        private final TextField valueField = new TextField();

        private QuantitativeCriteriaView(Criteria criteria) {
            super(criteria, (QuantitativeSetting) criteria.propSettings().get());

            Label unitLabel = new Label(setting.propUnit().get());

            HBox inputView = new HBox(valueField, unitLabel);
            inputView.setSpacing(8);
            getChildren().add(inputView);
        }

        @Override
        void install(Player player) {
            ObjectProperty<String> playerValueProperty = getPlayerValueProperty(player);
            valueField.textProperty().setValue(playerValueProperty.get());

            playerValueProperty.bind(valueField.textProperty());
        }
    }

    //_______________________________________________ End
}
