package ee.tmtu.ld27;

import ee.tmtu.libludum.graphics.Font;
import ee.tmtu.libludum.graphics.SpriteBatch;
import ee.tmtu.libludum.ui.Button;

public class HiScoreButton extends Button {

    public HiScore hiScore;

    public HiScoreButton(HiScore hiScore, Font font) {
        super("", font);
        this.hiScore = hiScore;
    }

    @Override
    public void draw(SpriteBatch batch, double lerp) {
        if(this.drawable != null) {
            this.drawable.draw(batch, this.x, this.y, this.width, this.height);
        }
        this.font.draw(batch, this.x + 8, this.y + padding.top - 2, this.hiScore.name, this.currentColor, Font.Orientation.LEFT);
        this.font.draw(batch, this.x + this.width - 6, this.y + padding.top - 2, ""+this.hiScore.score, this.currentColor, Font.Orientation.RIGHT);
    }

}
