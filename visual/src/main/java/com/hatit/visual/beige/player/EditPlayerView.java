package com.hatit.visual.beige.player;

import com.hatit.data.player.Player;
import com.hatit.data.tournament.Tournament;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.common.BoxListView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EditPlayerView extends HBox {
    //_______________________________________________ Parameters
    private static final String ID = "edit-player-view";

    //_______________________________________________ Initialize
    public EditPlayerView(Tournament tournament) {
        setId(ID);
        BoxListView<Player> playerListView = new BoxListView<>(tournament.propPlayers(), Player::of, PlayerRegion::new);
        PlayerView playerView = new PlayerView(playerListView.propSelectedItem(), tournament.propCriteria());

        getChildren().addAll(playerListView, playerView);
    }

    //_______________________________________________ Methods
    //_______________________________________________ Inner Classes
    private static class PlayerRegion extends VBox {
        private final Label nameLabel = StyleUtil.h0("Name");

        private final Player player;

        public PlayerRegion(Player player) {
            this.player = player;
            getChildren().addAll(nameLabel);
            sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
        }

        private void onShow() {
            nameLabel.textProperty().bind(player.propName());
        }

        private void onHide() {
            nameLabel.textProperty().unbind();
        }
    }

    //_______________________________________________ End
}
