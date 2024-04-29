package view.game.cellsgraphics;

import org.json.JSONObject;

import model.config.cells.CellType;
import view.game.GamePanel;

public final class CellGraphicsFactory {
    public static CellGraphics createCellGraphics(final GamePanel gamePanel, final JSONObject jObject) {
        final int cellID = jObject.getInt("id");
        final CellType cellType = CellType.valueOf(jObject.getString("type").toUpperCase());
        // TODO MVC appel avec controller pour cellType au lieu de direct appeler le model par la view
        final int yPos = jObject.getInt("yPos");
        final int xPos = jObject.getInt("xPos");
        if (cellType == CellType.PROPERTY) {
            return new RoundedRectCell(gamePanel, cellID, xPos, yPos);
        }
        return new OvalCell(gamePanel, cellID, xPos, yPos);
    }

}
