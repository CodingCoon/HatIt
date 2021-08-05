package com.hatit.visual.beige.player;

import com.hatit.data.criteria.*;
import com.hatit.data.player.Player;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;
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
        private static final Map<CriteriaType, Function<Criteria, CriteriaSettingView<?>>> VIEW_CREATOR = new HashMap<>();
        static {
            VIEW_CREATOR.put(CriteriaType.QUALITATIVE, QualitativeCriteriaSettingView::new );
            VIEW_CREATOR.put(CriteriaType.QUANTITATIVE, QuantitativeCriteriaSettingView::new);
            VIEW_CREATOR.put(CriteriaType.TAGGING, TaggingCriteriaSettingView::new);
        }

        private final Map<Criteria, CriteriaSettingView<?>> criteriaViewMap = new HashMap<>();

        public StatsView(List<Criteria> criteriaList) {
            setSpacing(12);
            for (Criteria criteria : criteriaList) {
                CriteriaSettingView<?> criteriaSettingView = VIEW_CREATOR.get(criteria.propType().get()).apply(criteria);
                criteriaViewMap.put(criteria, criteriaSettingView);
                getChildren().add(criteriaSettingView);
            }
        }

        private void update(Player oldSelectedPlayer, Player newSelectedPlayer) {
            for (CriteriaSettingView<?> criteriaSettingView : criteriaViewMap.values()) {

                if (oldSelectedPlayer != null) {
                    criteriaSettingView.uninstall(oldSelectedPlayer);
                }
                if (newSelectedPlayer != null) {
                    criteriaSettingView.install(newSelectedPlayer);
                }
            }
        }
    }

    private static abstract class CriteriaSettingView<T extends Setting> extends VBox {
        private static final String STYLE_CLASS = "criteria-setting-view";
        final Criteria criteria;
        final T setting;

        CriteriaSettingView(Criteria criteria, T setting) {
            getStyleClass().add(STYLE_CLASS);
            this.criteria = criteria;
            this.setting = setting;

            setSpacing(12); // TODO:

            getChildren().add(StyleUtil.h1(criteria.propName().get()));
        }

        abstract void install(Player player);

        abstract void uninstall(Player player);

    }

    private static class TaggingCriteriaSettingView extends CriteriaSettingView<TaggingSetting> {       // TODO: css
        private final ToggleGroup group;

        private final StringProperty selectedOption = new SimpleStringProperty();
        private final List<RadioButton> radioButtons = new ArrayList<>();

        private TaggingCriteriaSettingView(Criteria criteria) {
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
            StringProperty playerValueProperty = player.propTaggingStat(criteria);
            for (RadioButton radioButton : radioButtons) {
                if (radioButton.getText().equals(playerValueProperty.get())) {
                    group.selectToggle(radioButton);
                }
            }
            playerValueProperty.bind(selectedOption);
        }

        @Override
        void uninstall(Player player) {
            player.propTaggingStat(criteria).unbind();
        }
    }

    private static class QualitativeCriteriaSettingView extends CriteriaSettingView<QualitativeSetting> {
        private final Slider slider;

        private QualitativeCriteriaSettingView(Criteria criteria) {
            super(criteria, (QualitativeSetting) criteria.propSettings().get());

            QualitativeSetting.Range range = setting.propRange().get();
            this.slider = new Slider(range.getMin(), range.getMax(), range.getMin());
            slider.setBlockIncrement(1);    // TODO:
            slider.setSnapToTicks(true);
            slider.setShowTickLabels(true);
            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(0);

            getChildren().add(slider);
        }

        @Override
        void install(Player player) {
            IntegerProperty playerValueProperty = player.propQualitativStat(criteria);
            slider.valueProperty().setValue(playerValueProperty.get());

            playerValueProperty.bind(slider.valueProperty().asObject());
        }

        @Override
        void uninstall(Player player) {
            player.propQualitativStat(criteria).unbind();
        }
    }

    private static class QuantitativeCriteriaSettingView extends CriteriaSettingView<QuantitativeSetting> {
        private static final String STYLE_CLASS = "quantitative-criteria-setting";
        private final TextField valueField = new TextField();

        private QuantitativeCriteriaSettingView(Criteria criteria) {
            super(criteria, (QuantitativeSetting) criteria.propSettings().get());
            getStyleClass().add(STYLE_CLASS);

            HBox inputView = new HBox(valueField, new Label(setting.getUnit()));
            inputView.setSpacing(8); // TODO:
            getChildren().add(inputView);
        }

        @Override
        void install(Player player) {
            DoubleProperty playerValueProperty = player.propQualitativStat(criteria);
            valueField.textProperty().setValue("" + playerValueProperty.get());
            playerValueProperty.bind(new TextToDoubleBinding(valueField.textProperty()));
        }

        @Override
        void uninstall(Player player) {
            player.propQualitativStat(criteria).unbind();
        }
    }

    private static final class TextToDoubleBinding extends DoubleBinding implements InvalidationListener {
        private final StringProperty textProperty;

        private TextToDoubleBinding(StringProperty textProperty) {
            this.textProperty = textProperty;
            textProperty.addListener(this);
        }

        @Override
        public void dispose() {
            textProperty.removeListener(this);
        }

        @Override
        public void invalidated(Observable observable) {
            invalidate();
        }

        @Override
        protected double computeValue() {
            return Double.parseDouble(textProperty.get());
        }
    }

    //_______________________________________________ End
}
