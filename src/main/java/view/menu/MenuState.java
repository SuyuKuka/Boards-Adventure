package view.menu;

public enum MenuState {
    MAINMENU, // -> Settings ou SelectMode

    SETTINGS, // -> MainMenu

    SELECTMODE, // -> MainMenu ou GameConfig ou JoinGame

    GAMECONFIG, // -> MainMenu ou PlayerConfig

    JOINGAME, // -> SelectMode ou PlayerConfig

    PLAYERCONFIG // -> GameConfig
}
