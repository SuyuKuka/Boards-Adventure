package view.game.boardpanel.entitygraphics.cellsgraphics;

import org.json.JSONObject;

import model.config.cells.CellType;
import view.game.boardpanel.BoardPanel;

public final class CellGraphicsFactory {
    public static CellGraphics createCellGraphics(final BoardPanel boardPanel, final JSONObject jObject) {
        final int cellID = jObject.getInt("id");
        final int yPos = jObject.getInt("yPos");
        final int xPos = jObject.getInt("xPos");
        final CellType cellType = CellType.valueOf(jObject.getString("type").toUpperCase());
        // TODO MVC appel avec controller pour cellType au lieu de direct appeler le model par la view
        switch (cellType) {
            case PROPERTY:
                return new PropertyGraphics(boardPanel, cellID, xPos, yPos);
            default:
                break;
        }
        return new DefaultCellGraphics(boardPanel, cellID, xPos, yPos);
    }

    public static CellGraphics createIntersectionChoice(final BoardPanel boardPanel, final CellGraphics cellTarget) {
        return new IntersectionChoiceGraphics(boardPanel, cellTarget.getCellID(),
            cellTarget.getXPos(), cellTarget.getYPos());
    }

}
